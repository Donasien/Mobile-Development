package com.codenesia.donasein.ui.main.ui.profile

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.codenesia.donasein.R
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.data.remote.response.LogoutResponse
import com.codenesia.donasein.data.remote.response.UserResponse
import com.codenesia.donasein.databinding.FragmentProfileBinding
import com.codenesia.donasein.ui.ViewModelFactory
import com.codenesia.donasein.ui.aboutUs.AboutUsActivity
import com.codenesia.donasein.ui.faq.FaqActivity
import com.codenesia.donasein.ui.main.ui.donate.DonateViewModel
import com.codenesia.donasein.ui.profile.MyProfileActivity
import com.codenesia.donasein.ui.splash.StartActivity
import com.codenesia.donasein.ui.user.LoginActivity
import com.codenesia.donasein.ui.user.UserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.internal.wait
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user!=null) {
            setDataUser()
        }

        binding.profileBtnMyprofile.setOnClickListener {
            if (user!=null) {
                startActivity(Intent(requireContext(), MyProfileActivity::class.java))
            } else {
                showMessage(false, "You're not login yet")
            }
        }

        binding.profileAboutUs.setOnClickListener {
            startActivity(Intent(requireContext(), AboutUsActivity::class.java))
        }

        binding.profileFaq.setOnClickListener {
            startActivity(Intent(requireContext(), FaqActivity::class.java))
        }

        binding.profileBtnLogout.setOnClickListener {
            if (user!=null) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Confirmation")
                    .setMessage("Do you want to logout?")
                    .setNegativeButton("Cancel",) { dialog, which ->
                        false
                    }
                    .setPositiveButton("Logout") { dialog, which ->
                        logoutServer()
                    }
                    .show()

            } else {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                activity?.finish()
            }
        }

    }

    private fun logoutServer() {
        if (auth.currentUser!=null) {
            val token = auth.currentUser!!.getIdToken(false).result.token.toString()
            userViewModel.getLogoutResponse(token).observe(viewLifecycleOwner){result ->
                if (result!=null) {
                    when(result) {
                        is Results.Loading -> {

                        }

                        is Results.Success -> {
                            signOut(result.data)
                        }

                        is Results.Error -> {
                            Log.e("Error Logout", result.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun signOut(data: LogoutResponse) {
        auth.signOut()
        val user = UserPreferences(requireActivity())
        val userNull = UserResponse()
        userNull.let {
            it.userId = null
            it.name = null
            it.email = null
            it.image= null
            it.userId = null
            it.tokenId= null
        }
        Log.w("Logout", data.message.toString())
        user.setUser(userNull)
        showMessage(true, "Logout success")
        startActivity(Intent(requireActivity(), StartActivity::class.java))
        activity?.finish()
    }


    private fun setDataUser() {
        auth = Firebase.auth
        val user = UserPreferences(requireActivity()).getUser()
        val checkWithName = user.name.toString().trim()
        if (checkWithName.isNotEmpty()) {
            binding.profilTvUsername.text = user.name
            binding.profilTvEmail.text = user.email
            binding.profilImageAkun.imageTintMode = null
            binding.profileBtnLogout.text = "LOGOUT"
            binding.profileBtnLogout.setBackgroundColor(Color.RED)
            Glide.with(requireActivity())
                .load(Uri.parse(user.image))
                .skipMemoryCache(true)
                .into(binding.profilImageAkun)
        }

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
                ResourcesCompat.getFont(requireContext(), R.font.poppins_medium)
            )
        } else {
            MotionToast.createColorToast(
                requireActivity(),
                "Warning",
                message,
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(requireContext(), R.font.poppins_medium)
            )
        }
    }

}