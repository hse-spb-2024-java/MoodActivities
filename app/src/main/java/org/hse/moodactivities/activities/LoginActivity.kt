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
import org.hse.moodactivities.models.AuthType


class LoginActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // return to welcome activity button
        binding.returnButton.setOnClickListener {
            this.finish()
        }

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
                    loginResponse.token,
                    AuthType.PLAIN,
                )

                authViewModel.saveToken(
                    getSharedPreferences("userPreferences", Context.MODE_PRIVATE),
                    loginResponse.token
                )
                Log.d("LoginResponse", userViewModel.getUser(applicationContext)!!.id.toString())
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
            }
        }

        setColorTheme()
    }

    private fun setColorTheme() {
        val colorTheme = ThemesService.getColorTheme()

        // set color to status bar
        window.statusBarColor = colorTheme.getBackgroundColor()

        // set background color
        binding.layout.setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to return button
        binding.returnImage.setColorFilter(colorTheme.getFontColor())

        // set title color
        binding.signupHeader.setTextColor(colorTheme.getFontColor())

        // set color to app name
        binding.appName.setTextColor(colorTheme.getFontColor())

        // set input fields colors
        binding.usernameInputBackground.setCardBackgroundColor(colorTheme.getInputBackgroundColor())
        binding.usernameInput.setTextColor(colorTheme.getInputTextColor())
        binding.usernameInput.setHintTextColor(colorTheme.getInputHintTextColor())
        binding.usernameImage.setColorFilter(colorTheme.getInputHintTextColor())

        binding.passwordInputBackground.setCardBackgroundColor(colorTheme.getInputBackgroundColor())
        binding.passwordInput.setTextColor(colorTheme.getInputTextColor())
        binding.passwordInput.setHintTextColor(colorTheme.getInputHintTextColor())
        binding.passwordImage.setColorFilter(colorTheme.getInputHintTextColor())
        // todo: set colors to cursors

        // set login button color
        binding.loginButtonBackground.setCardBackgroundColor(colorTheme.getButtonColor())
        binding.loginText.setTextColor(colorTheme.getButtonTextColor())

        // set redirect text color
        binding.account.setTextColor(colorTheme.getFontColor())
        binding.registerRedirectText.setTextColor(colorTheme.getButtonColor())
    }
}
