package com.codenesia.donasein.ui.main.ui.profile

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.data.remote.response.UserResponse
import com.codenesia.donasein.databinding.FragmentProfileBinding
import com.codenesia.donasein.ui.splash.StartActivity
import com.codenesia.donasein.ui.user.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.internal.wait

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        setDataUser(true)

        auth = Firebase.auth
        binding.profileBtnLogout.setOnClickListener { intentFunction("logout") }
    }

    private fun intentFunction(s: String) {
        if (s == "logout"){
            auth.signOut()
            setDataUser(false)
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setDataUser(check: Boolean) {
        /*cek 1 untuk setData, cek 0 untuk logout*/
        if (check) {
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
        } else {
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

            user.setUser(userNull)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}