package com.codenesia.donasein.ui.user

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import com.codenesia.donasein.R
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.data.remote.response.UserResponse
import com.codenesia.donasein.databinding.ActivityLoginBinding
import com.codenesia.donasein.ui.ViewModelFactory
import com.codenesia.donasein.ui.main.MainActivity
import com.codenesia.donasein.ui.splash.StartActivity
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)


        supportActionBar?.hide()
        loginBinding.loginBtnBack.setOnClickListener { intentFunction("back") }
        loginBinding.loginTvCtaForgotPass.setOnClickListener { intentFunction("forgot_password") }
        loginBinding.loginTvCta2.setOnClickListener { intentFunction("register") }
        loginBinding.loginBtnSignIn.setOnClickListener { manualSignIn() }
        loginBinding.loginBtnSignInWithGoogle.setOnClickListener { signInWithGoogle() }


        /*Initialize section*/
        auth = Firebase.auth
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleChoose.launch(signInIntent)
    }


    private var googleChoose = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!, account.email.toString())
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        } else {
            Log.e(TAG, result.data.toString())
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, email: String) {
        loginBinding.loadingLottie.visibility = View.VISIBLE
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val newCredential = EmailAuthProvider.getCredential(email, "12345678")

                    // provider google dan email
                    if (user?.providerData?.size!! > 2) {
                        auth.signInWithCredential(newCredential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                loginBinding.loadingLottie.visibility = View.GONE
                                sendToServer(user, true)
                            } else {
                                Log.e(TAG, it.result.toString())
                            }
                        }
                    }

                    // provider google
                    else {
                        user.linkWithCredential(newCredential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                loginBinding.loadingLottie.visibility = View.GONE
                                sendToServer(user, true)
                            } else {
                                Log.e(TAG, it.result.toString())
                            }
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    loginBinding.loadingLottie.visibility = View.GONE
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun sendToServer(user: FirebaseUser?, check: Boolean) {
        // check true = google, check false = manual
        if (user != null) {
            val tokenId = user.getIdToken(false).result.token.toString()
            if (check) {
                userViewModel.getLoginGoogleResponse(user.email.toString(), user.displayName.toString(), tokenId, user.photoUrl.toString()).observe(this){ result ->
                    if (result != null) {
                        when(result) {
                            is Results.Loading -> {

                            }

                            is Results.Success -> {
                                Log.d("berhasil login google", result.data.data.toString())
                                setDataUser()
                                showMessage(true, "Let's Explore Donasians")
                                updateUI(user)
                            }

                            is Results.Error -> {
                                showMessage(false, result.error.toString())
                                updateUI(null)
                                Log.e("Login Response Error", result.error.toString())
                            }
                        }
                    }
                }
            } else {
                userViewModel.getLoginResponse(user.email.toString(), tokenId).observe(this){ result ->
                    if (result != null) {
                        when(result) {
                            is Results.Loading -> {

                            }

                            is Results.Success -> {
                                Log.d("berhasil login manual", result.data.data.toString())
                                setDataUser()

                            }

                            is Results.Error -> {
                                Log.e("Login Response Error", result.error.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }



    private fun manualSignIn() {
        loginBinding.loadingLottie.visibility = View.VISIBLE
        val emailLayout = loginBinding.loginEtEmailText.text.toString()
        val passLayout = loginBinding.loginEtPassText.text.toString()
        if (emailLayout.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailLayout).matches()){
            loginBinding.loadingLottie.visibility = View.GONE
            loginBinding.loginEtEmail.error = "Email tidak valid"
        } else {
            if (passLayout.length < 8) {
                loginBinding.loadingLottie.visibility = View.GONE
                loginBinding.loginEtPass.error = "Password tidak valid"
            } else {
                signInWithUser(emailLayout, passLayout)
            }
        }
    }

    private fun signInWithUser(email: String, pass: String) {
        try {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    loginBinding.loadingLottie.visibility = View.GONE
                    val user = auth.currentUser
                    setDataUser()
                    sendToServer(user, false)
                    updateUI(user)
                }
            }.addOnFailureListener {
                loginBinding.loadingLottie.visibility = View.GONE
                showMessage(false, "Akun Tidak Ditemukan") }
        } catch (e: Exception){
            loginBinding.loadingLottie.visibility = View.GONE
            showMessage(false, e.message.toString())
        }
    }

    private fun setDataUser(){
        val data = UserResponse()
        val userFirebase = auth.currentUser
        if (userFirebase != null) {
            data.let {
                it.userId = userFirebase.uid
                it.name = userFirebase.displayName
                it.email = userFirebase.email
                it.image= userFirebase.photoUrl.toString()
                it.userId = userFirebase.uid
                it.tokenId= userFirebase.getIdToken(false).result.token.toString()
            }
            val user = UserPreferences(this)
            user.setUser(data)
        }

    }

    private fun intentFunction(message: String) {
        if (message == "back") {
            startActivity(Intent(this, StartActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        } else if (message == "register") {
            startActivity(Intent(this, RegisterActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        } else if (message == "forgot_password"){
            startActivity(Intent(this, ForgotPasswordActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    private fun showMessage(check: Boolean, message: String) {
        if (check) {
            MotionToast.createColorToast(
                this,
                "Success",
                message,
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this,R.font.poppins_medium)
            )
        } else {
            MotionToast.createColorToast(
                this,
                "Failed",
                message,
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this,R.font.poppins_medium)
            )
        }
    }



    companion object {
        private const val TAG = "LoginActivity"
    }
}