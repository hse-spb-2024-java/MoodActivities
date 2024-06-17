package org.hse.moodactivities.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.hse.moodactivities.common.proto.responses.auth.LoginResponse
import org.hse.moodactivities.databinding.ActivityLoginBinding
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.showCustomToast
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
                Toast(this).showCustomToast(
                    "Password cannot be empty", this
                )
                return@setOnClickListener
            }

            val loginResponseLiveData = authViewModel.login(username, password);

            authViewModel.errorMessage.observe(this) {
                if (it != null) {
                    Toast(this).showCustomToast(
                        it, this
                    )
                    authViewModel.clearErrorMessage()
                }
            }

            loginResponseLiveData.observe(this) { loginResponse ->
                if (loginResponse.type == LoginResponse.responseType.ERROR) {
                    Log.d("LoginResponse", loginResponse.message)
                    Toast(this).showCustomToast(
                        loginResponse.message, this
                    )
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
                    Log.d("LoginResponse", user.id.toString())
                }
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
