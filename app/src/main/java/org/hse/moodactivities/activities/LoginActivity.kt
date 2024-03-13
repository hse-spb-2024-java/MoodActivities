package org.hse.moodactivities.activities

import org.hse.moodactivities.viewmodels.AuthViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.hse.moodactivities.common.proto.responses.auth.LoginResponse
import org.hse.moodactivities.databinding.ActivityLoginBinding
import org.hse.moodactivities.viewmodels.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val username = binding.etLoginUsername.text.toString()
            val password = binding.etLoginPassword.text.toString()

            authViewModel.login(username, password).observe(this, Observer<LoginResponse> { loginResponse ->
                if (loginResponse.type == LoginResponse.responseType.ERROR) {
                    Log.d("LoginResponse", loginResponse.message)
                } else {
                    userViewModel.updateUserFromJwt(applicationContext, loginResponse.token)

                    userViewModel.user.observe(
                        this, Observer {
                            user -> Log.d("LoginResponse", user.id.toString());
                        }
                    )
                    val intent = Intent(this, MainScreenActivity::class.java)
                    startActivity(intent)
                }
            })
        }
    }
}
