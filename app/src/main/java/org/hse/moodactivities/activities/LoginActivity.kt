package org.hse.moodactivities.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.hse.moodactivities.common.proto.responses.auth.LoginResponse
import org.hse.moodactivities.databinding.ActivityLoginBinding
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.viewmodels.AuthViewModel
import org.hse.moodactivities.viewmodels.UserViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerRedirectButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (password.isEmpty()) {
                Log.d("LoginResponse", "Password cannot be empty");
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Password cannot be empty",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val loginResponseLiveData = authViewModel.login(username, password);

            authViewModel.errorMessage.observe(this) {
                if (it != null) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        it,
                        Snackbar.LENGTH_LONG
                    ).show()
                    authViewModel.clearErrorMessage()
                }
            }

            loginResponseLiveData.observe(this) { loginResponse ->
                if (loginResponse.type == LoginResponse.responseType.ERROR) {
                    Log.d("LoginResponse", loginResponse.message)
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        loginResponse.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                    return@observe
                }
                userViewModel.updateUserFromJwt(
                    applicationContext,
                    loginResponse.token
                )

                authViewModel.saveToken(
                    getSharedPreferences("userPreferences", Context.MODE_PRIVATE),
                    loginResponse.token
                )

                userViewModel.user.observe(this) { user ->
                    Log.d("LoginResponse", user.id.toString())
                }
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
            }
        }

        setColorTheme()
    }

    private fun setColorTheme() {
        // set color to status bar
        window.statusBarColor = ThemesService.getBackgroundColor()

        // set background color
        binding.loginScreenLayout.setBackgroundColor(ThemesService.getBackgroundColor())

        // set tittle color
        binding.signupHeader.setTextColor(ThemesService.getFontColor())

        // set input fields colors
        binding.usernameInputBackground.setCardBackgroundColor(ThemesService.getColor3())
        binding.passwordInputBackground.setCardBackgroundColor(ThemesService.getColor3())

        // set register button color
        binding.loginButtonBackground.setCardBackgroundColor(ThemesService.getColor4())

        binding.registerRedirectText.setTextColor(ThemesService.getColor4())
        binding.loginText.setTextColor(ThemesService.getDimmedBackgroundColor())
    }
}
