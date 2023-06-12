package com.codenesia.donasein.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codenesia.donasein.R
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.databinding.FragmentHomeBinding
import com.codenesia.donasein.ui.ViewModelFactory
import com.codenesia.donasein.ui.donateStatus.StatusDonateActivity
import com.codenesia.donasein.ui.main.ui.donate.DonateViewModel
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
    private var statusDonate: String? = null
    private var statusUser: Boolean? = false

    private val donateViewModel: DonateViewModel by viewModels {
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
            binding.homeTvHistory.text = "Donation History"

            setStatusDonate()
        }

        binding.homeCardHistory.setOnClickListener { historyAction(user) }
        binding.homeImgNotification.setOnClickListener {
            showMessage(false, "Dalam Tahap Pengembangan")
        }
        binding.homeCardGalangdana.setOnClickListener {
            if (user != null) {
                if (statusUser != false) {
                    if (statusDonate == "accept" || statusDonate == "pending") {
                        showMessage(false, "Kamu telah melakukan pengajuan donasi")
                    } else {
                        intentAction("submission")
                    }
                } else {
                    showMessage(false, "Lengkapi profile terlebih dahulu")
                }
            } else {
                showMessage(false, "Login terlebih dahulu")
            }
        }

        binding.homeCardStatus.setOnClickListener {
            if (user != null) {
                intentAction("status")
            } else {
                showMessage(false, "Login terlebih dahulu")
            }
        }


    }

    private fun setStatusDonate() {
        val user = UserPreferences(requireContext())
        val data = user.getUser()
        donateViewModel.getStatusDonate(data.tokenId.toString()).observe(requireActivity()){ result ->
            if (result != null) {
                when(result) {
                    is Results.Success -> {
                        val response = result.data.data
                        statusDonate = response!!.statusDonation
                        statusUser = response.statusData
                        Log.e("Status Donasi", statusDonate.toString())
                    }
                    is Results.Loading -> { }
                    is Results.Error -> {
                        Log.e("Status Donasi", result.error.toString())
                    }
                }
            }
        }
    }

    private fun intentAction(route: String) {
        if (route == "submission") {
            startActivity(Intent(requireActivity(), SubmissonActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity()).toBundle())
        } else if (route == "status") {
            val intentStatus = Intent(requireActivity(), StatusDonateActivity::class.java)
            intentStatus.putExtra(StatusDonateActivity.RESULT_STATUS_DONATE, statusDonate)
            startActivity(intentStatus)
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