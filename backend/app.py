"""
Nodo Civico - API REST (Flask + SQLite)

Implementa todos los endpoints definidos en docs/API.pdf.
Ejecutar: python app.py
"""

from flask import Flask, request, jsonify
from flask_cors import CORS
import sqlite3
import time
import os

app = Flask(__name__)
CORS(app)

DB_PATH = os.path.join(os.path.dirname(__file__), "nodocivico.db")


def get_db():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    return conn


def init_db():
    conn = get_db()
    conn.executescript("""
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            email TEXT UNIQUE NOT NULL,
            sector TEXT DEFAULT '',
            password TEXT DEFAULT '',
            created_at TEXT DEFAULT (datetime('now'))
        );

        CREATE TABLE IF NOT EXISTS categories (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT UNIQUE NOT NULL,
            iconEmoji TEXT DEFAULT ''
        );

        CREATE TABLE IF NOT EXISTS reports (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            userId INTEGER DEFAULT 1,
            title TEXT NOT NULL,
            description TEXT NOT NULL,
            category TEXT NOT NULL,
            priority TEXT NOT NULL DEFAULT 'MEDIA',
            status TEXT NOT NULL DEFAULT 'OPEN',
            location TEXT DEFAULT '',
            pendingSync INTEGER DEFAULT 0,
            createdAt INTEGER,
            updatedAt INTEGER
        );
    """)

    # Insertar categorias por defecto si no existen
    cats = [
        ("Alumbrado", "💡"),
        ("Aseo", "🧹"),
        ("Seguridad", "🔒"),
        ("Servicios publicos", "🔧"),
    ]
    for name, icon in cats:
        conn.execute(
            "INSERT OR IGNORE INTO categories (name, iconEmoji) VALUES (?, ?)",
            (name, icon),
        )
    conn.commit()
    conn.close()


# ──────────────────── Sistema ────────────────────

@app.route("/", methods=["GET"])
def index():
    return jsonify({"message": "Nodo Civico API activa", "status": "ok"})


@app.route("/health", methods=["GET"])
def health():
    return jsonify({"status": "ok", "version": "2.0.0"})


# ──────────────────── Usuarios ────────────────────

@app.route("/users/register", methods=["POST"])
def register_user():
    data = request.get_json(force=True)
    name = data.get("name")
    email = data.get("email")
    if not name or not email:
        return jsonify({"error": "missing_fields", "fields": [f for f in ["name", "email"] if not data.get(f)]}), 400

    conn = get_db()
    existing = conn.execute("SELECT * FROM users WHERE email = ?", (email,)).fetchone()
    if existing:
        user = dict(existing)
        conn.close()
        return jsonify(user), 200

    conn.execute(
        "INSERT INTO users (name, email, sector, password) VALUES (?, ?, ?, ?)",
        (name, email, data.get("sector", ""), data.get("password", "")),
    )
    conn.commit()
    user = dict(conn.execute("SELECT * FROM users WHERE email = ?", (email,)).fetchone())
    conn.close()
    return jsonify(user), 201


@app.route("/users/login", methods=["POST"])
def login_user():
    data = request.get_json(force=True)
    email = data.get("email")
    conn = get_db()
    user = conn.execute("SELECT * FROM users WHERE email = ?", (email,)).fetchone()
    conn.close()
    if not user:
        return jsonify({"error": "user_not_found"}), 404
    return jsonify(dict(user)), 200


@app.route("/users/<int:uid>", methods=["GET"])
def get_user(uid):
    conn = get_db()
    user = conn.execute("SELECT * FROM users WHERE id = ?", (uid,)).fetchone()
    conn.close()
    if not user:
        return jsonify({"error": "user_not_found"}), 404
    return jsonify(dict(user)), 200


@app.route("/users", methods=["GET"])
def list_users():
    conn = get_db()
    users = [dict(r) for r in conn.execute("SELECT * FROM users").fetchall()]
    conn.close()
    return jsonify(users), 200


# ──────────────────── Categorias ────────────────────

@app.route("/categories", methods=["GET"])
def list_categories():
    conn = get_db()
    cats = [dict(r) for r in conn.execute("SELECT * FROM categories").fetchall()]
    conn.close()
    return jsonify(cats), 200


@app.route("/categories", methods=["POST"])
def create_category():
    data = request.get_json(force=True)
    name = data.get("name")
    if not name:
        return jsonify({"error": "missing_fields", "fields": ["name"]}), 400
    conn = get_db()
    conn.execute(
        "INSERT OR IGNORE INTO categories (name, iconEmoji) VALUES (?, ?)",
        (name, data.get("iconEmoji", "")),
    )
    conn.commit()
    cat = dict(conn.execute("SELECT * FROM categories WHERE name = ?", (name,)).fetchone())
    conn.close()
    return jsonify(cat), 201


# ──────────────────── Reportes ────────────────────

def report_to_dict(row):
    d = dict(row)
    d["remoteId"] = str(d["id"])
    d["pendingSync"] = bool(d.get("pendingSync", 0))
    return d


@app.route("/reports", methods=["GET"])
def list_reports():
    conn = get_db()
    rows = conn.execute("SELECT * FROM reports ORDER BY createdAt DESC").fetchall()
    conn.close()
    return jsonify([report_to_dict(r) for r in rows]), 200


@app.route("/reports/all", methods=["GET"])
def list_reports_all():
    return list_reports()


@app.route("/reports/<int:rid>", methods=["GET"])
def get_report(rid):
    conn = get_db()
    row = conn.execute("SELECT * FROM reports WHERE id = ?", (rid,)).fetchone()
    conn.close()
    if not row:
        return jsonify({"error": "Not Found"}), 404
    return jsonify(report_to_dict(row)), 200


@app.route("/reports", methods=["POST"])
def create_report():
    data = request.get_json(force=True)
    required = ["title", "description", "category", "priority"]
    missing = [f for f in required if not data.get(f)]
    if missing:
        return jsonify({"error": "missing_fields", "fields": missing}), 400

    now = int(time.time() * 1000)
    conn = get_db()
    cur = conn.execute(
        """INSERT INTO reports (userId, title, description, category, priority, status, location, pendingSync, createdAt, updatedAt)
           VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?, ?)""",
        (
            data.get("userId", 1),
            data["title"],
            data["description"],
            data["category"],
            data["priority"],
            data.get("status", "OPEN"),
            data.get("location", ""),
            now,
            now,
        ),
    )
    conn.commit()
    row = conn.execute("SELECT * FROM reports WHERE id = ?", (cur.lastrowid,)).fetchone()
    conn.close()
    return jsonify(report_to_dict(row)), 201


@app.route("/reports/<int:rid>", methods=["PUT"])
def update_report(rid):
    conn = get_db()
    existing = conn.execute("SELECT * FROM reports WHERE id = ?", (rid,)).fetchone()
    if not existing:
        conn.close()
        return jsonify({"error": "Not Found"}), 404

    data = request.get_json(force=True)
    now = int(time.time() * 1000)
    conn.execute(
        """UPDATE reports SET
            title = COALESCE(?, title),
            description = COALESCE(?, description),
            category = COALESCE(?, category),
            priority = COALESCE(?, priority),
            status = COALESCE(?, status),
            location = COALESCE(?, location),
            updatedAt = ?
           WHERE id = ?""",
        (
            data.get("title"),
            data.get("description"),
            data.get("category"),
            data.get("priority"),
            data.get("status"),
            data.get("location"),
            now,
            rid,
        ),
    )
    conn.commit()
    row = conn.execute("SELECT * FROM reports WHERE id = ?", (rid,)).fetchone()
    conn.close()
    return jsonify(report_to_dict(row)), 200


@app.route("/reports/<int:rid>/status", methods=["PATCH"])
def patch_status(rid):
    data = request.get_json(force=True)
    status = data.get("status")
    if not status:
        return jsonify({"error": "status_required"}), 400

    conn = get_db()
    existing = conn.execute("SELECT * FROM reports WHERE id = ?", (rid,)).fetchone()
    if not existing:
        conn.close()
        return jsonify({"error": "Not Found"}), 404

    now = int(time.time() * 1000)
    conn.execute("UPDATE reports SET status = ?, updatedAt = ? WHERE id = ?", (status, now, rid))
    conn.commit()
    row = conn.execute("SELECT * FROM reports WHERE id = ?", (rid,)).fetchone()
    conn.close()
    return jsonify(report_to_dict(row)), 200


@app.route("/reports/<int:rid>", methods=["DELETE"])
def delete_report(rid):
    conn = get_db()
    existing = conn.execute("SELECT * FROM reports WHERE id = ?", (rid,)).fetchone()
    if not existing:
        conn.close()
        return jsonify({"error": "Not Found"}), 404
    conn.execute("DELETE FROM reports WHERE id = ?", (rid,))
    conn.commit()
    conn.close()
    return jsonify({"deleted": True, "id": rid}), 200


if __name__ == "__main__":
    init_db()
    port = int(os.environ.get("PORT", 8080))
    print(f"=== Nodo Civico API corriendo en http://0.0.0.0:{port} ===")
    app.run(host="0.0.0.0", port=port, debug=True)
