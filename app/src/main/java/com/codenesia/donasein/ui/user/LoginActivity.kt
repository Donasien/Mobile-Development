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
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import com.codenesia.donasein.R
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.data.remote.response.UserResponse
import com.codenesia.donasein.databinding.ActivityLoginBinding
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)


        supportActionBar?.hide()
        loginBinding.loginBtnBack.setOnClickListener { intentFunction("back") }
        loginBinding.loginTvCtaForgotPass.setOnClickListener { intentFunction("forgot_password") }
        loginBinding.loginTvCta2.setOnClickListener { intentFunction("register") }
        loginBinding.loginBtnSignIn.setOnClickListener { manualSignIn() }
        loginBinding.loginBtnSignInWithGoogle.setOnClickListener { signIn() }


        /*Initialize section*/
        auth = Firebase.auth
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!, account.email.toString())
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
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
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val newCredential = EmailAuthProvider.getCredential(email, "12345678")

                    if (user?.providerData?.size!! > 2) {
                        auth.signInWithCredential(newCredential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                loginBinding.loadingLottie.visibility = View.GONE
                                setDataUser()
                                showMessage(true, "Let's Explore Donasians")
                                updateUI(user)
                            }
                        }
                    } else {
                        user.linkWithCredential(newCredential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                loginBinding.loadingLottie.visibility = View.GONE
                                setDataUser()
                                showMessage(true, "Let's Explore Donasians")
                                updateUI(user)
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

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
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



    private fun manualSignIn() {
        loginBinding.loadingLottie.visibility = View.VISIBLE
        val emailLog = loginBinding.loginEtEmailText.text.toString()
        val passLog = loginBinding.loginEtPassText.text.toString()
        if (emailLog.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailLog).matches()){
            loginBinding.loadingLottie.visibility = View.GONE
            loginBinding.loginEtEmail.error = "Email tidak valid"
        } else {
            if (passLog.length < 8) {
                loginBinding.loadingLottie.visibility = View.GONE
                loginBinding.loginEtPass.error = "Password tidak valid"
            } else {
                signInWithUser(emailLog, passLog)
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

    private fun intentFunction(s: String) {
        if (s == "back") {
            startActivity(Intent(this, StartActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        } else if (s == "register") {
            startActivity(Intent(this, RegisterActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        } else if (s == "forgot_password"){
            startActivity(Intent(this, ForgotPasswordActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    private fun setDataUser(){
        val data = UserResponse()
        val dataFromFb = auth.currentUser
        if (dataFromFb != null) {
            data.let {
                it.userId = dataFromFb.uid
                it.name = dataFromFb.displayName
                it.email = dataFromFb.email
                it.image= dataFromFb.photoUrl.toString()
                it.userId = dataFromFb.uid
                it.tokenId= dataFromFb.getIdToken(false).result.token.toString()
            }
            val user = UserPreferences(this)
            user.setUser(data)
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}