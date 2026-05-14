package com.nodocivico.app.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nodocivico.app.R
import com.nodocivico.app.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Splash de marca.
 *
 * Muestra el logotipo y el nombre del producto durante 1.2 s antes de
 * delegar a HomeFragment. Como el sistema ya proyecta el splash nativo,
 * este fragment funciona como una transición visual hacia la app.
 *
 * En el Entregable 2 esta pantalla aprovechará el tiempo para inicializar
 * Room en segundo plano y cargar el perfil del usuario; en el Entregable 3,
 * para disparar la primera verificación de conectividad y de pendientes
 * de sincronización.
 */
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1200)
            // Navegamos con popUpTo para que el back no vuelva al splash.
            findNavController().navigate(
                R.id.action_splashFragment_to_homeFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
