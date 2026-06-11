package com.nodocivico.app.network

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ---- Sistema ----
    @GET("health")
    suspend fun health(): Response<Map<String, String>>

    // ---- Reportes ----
    @GET("reports")
    suspend fun getReports(): Response<List<ReportDto>>

    @GET("reports/{id}")
    suspend fun getReport(@Path("id") id: Int): Response<ReportDto>

    @POST("reports")
    suspend fun createReport(@Body body: ReportRequest): Response<ReportDto>

    @PUT("reports/{id}")
    suspend fun updateReport(@Path("id") id: Int, @Body body: ReportRequest): Response<ReportDto>

    @PATCH("reports/{id}/status")
    suspend fun updateStatus(@Path("id") id: Int, @Body body: StatusRequest): Response<ReportDto>

    @DELETE("reports/{id}")
    suspend fun deleteReport(@Path("id") id: Int): Response<DeleteResponse>
}
