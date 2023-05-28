package com.codenesia.donasein.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codenesia.donasein.R
import com.codenesia.donasein.databinding.FragmentHomeBinding
import com.codenesia.donasein.ui.ViewModelFactory
import com.codenesia.donasein.ui.submission.SubmissonActivity
import com.codenesia.donasein.ui.user.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val user = auth.currentUser
        if (user != null) {
            binding.homeTvGreeting.text = "Hello, ${user.displayName}!"
            binding.homeTvHistory.text= "Donation History"
        }

        binding.homeCardHistory.setOnClickListener { historyAction(user) }
        binding.homeImgNotification.setOnClickListener {
            showMessage(false, "Dalam Tahap Pengembangan")
        }
        binding.homeCardGalangdana.setOnClickListener {
            if (user != null) {
                intentAction("submission")
            } else {
                showMessage(false, "Login Terlebih Dahulu")
            }
        }


    }

    private fun intentAction(route: String) {
        if (route == "submission") {
            startActivity(Intent(requireActivity(), SubmissonActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity()).toBundle())
        } else {
            showMessage(false, "Dalam Tahap Pengembangan")
        }
    }

    private fun historyAction(user: FirebaseUser?) {
        if (user!=null) {
            showMessage(false, "Dalam Tahap Pengembangan")
        } else {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMessage(check: Boolean, message: String) {
        if (check) {
            MotionToast.createColorToast(
                requireActivity(),
                "Success",
                message,
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(requireContext(),R.font.poppins_medium)
            )
        } else {
            MotionToast.createColorToast(
                requireActivity(),
                "Warning",
                message,
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(requireContext(),R.font.poppins_medium)
            )
        }
    }

}