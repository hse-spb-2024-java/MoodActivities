package org.hse.moodactivities.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import org.hse.moodactivities.R
import org.hse.moodactivities.common.proto.responses.auth.OauthLoginResponse
import org.hse.moodactivities.databinding.ActivityWelcomeBinding
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.viewmodels.AuthViewModel
import org.hse.moodactivities.viewmodels.UserViewModel

class WelcomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var authViewModel: AuthViewModel


    private var RETURN_CODE_SIGN_IN = 9001;

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RETURN_CODE_SIGN_IN) {
            authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val responseLiveData = authViewModel.handleGoogleLogin(task)

            authViewModel.errorMessage.observe(this) {
                if (it != null) {
                    Snackbar.make(
                        findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG
                    ).show()
                    authViewModel.clearErrorMessage()
                }
            }

            responseLiveData.observe(this) { loginResponse ->
                if (loginResponse.type == OauthLoginResponse.responseType.ERROR) {
                    Log.i("oauth", loginResponse.message)
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        loginResponse.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                    return@observe
                }
                userViewModel.updateUserFromJwt(
                    applicationContext, loginResponse.token
                )

                authViewModel.saveToken(
                    getSharedPreferences("userPreferences", Context.MODE_PRIVATE),
                    loginResponse.token
                )

                userViewModel.user.observe(this) { user ->
                    Log.i("oauth", user.id.toString())
                }
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val signInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestIdToken(applicationContext.resources.getString(R.string.app_id)).build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions)

        binding.googleLoginButton.setOnClickListener {
            val oauthIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(oauthIntent, RETURN_CODE_SIGN_IN);
        }

        setColorTheme()
    }

    private fun setColorTheme() {
        // set color to status bar
        window.statusBarColor = ThemesService.getBackgroundColor()

        // set background color
        binding.welcomeScreenBackground.setBackgroundColor(ThemesService.getBackgroundColor())

        // set color to app name
        binding.appName.setTextColor(ThemesService.getDimmedColor4())

        // set buttons colors
        binding.registerButtonBackground.setCardBackgroundColor(ThemesService.getButtonColor())
        binding.registerButtonText.setTextColor(ThemesService.getButtonTextColor())
        binding.loginButtonBackground.setCardBackgroundColor(ThemesService.getButtonColor())
        binding.loginButtonText.setTextColor(ThemesService.getButtonTextColor())
        binding.googleButtonBackground.setCardBackgroundColor(ThemesService.getButtonColor())
        binding.googleButtonText.setTextColor(ThemesService.getButtonTextColor())
    }
}
