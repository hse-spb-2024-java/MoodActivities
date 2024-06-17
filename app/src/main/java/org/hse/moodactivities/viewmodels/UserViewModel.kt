package org.hse.moodactivities.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.jsonwebtoken.Jwts
import org.hse.moodactivities.R
import org.hse.moodactivities.models.AuthType
import org.hse.moodactivities.models.User
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

class UserViewModel : ViewModel() {
    private fun saveUser(context: Context, user: User) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putLong("userId", user.id)
            putString("authType", user.authType.toString())
            apply()
        }
    }

    fun getUser(context: Context): User? {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", -1)
        val authTypeStr = sharedPreferences.getString("authType", null)

        return if (userId != -1L && authTypeStr != null) {
            var authType = AuthType.PLAIN
            when (authTypeStr) {
                "PLAIN" -> authType = AuthType.PLAIN
                "GOOGLE" -> authType = AuthType.GOOGLE
            }
            User(userId, authType)
        } else {
            null
        }
    }

    fun updateUserFromJwt(context: Context, token: String, authType: AuthType) {
        val publicKeyBytes = Base64.getDecoder().decode(context.resources.getString(R.string.public_key));
        val keySpec = X509EncodedKeySpec(publicKeyBytes);
        val keyFactory = KeyFactory.getInstance("RSA");
        val publicKey = keyFactory.generatePublic(keySpec);

        val parser = Jwts.parser()
            .verifyWith(publicKey)
            .build()

        val claims = parser.parseSignedClaims(token)

        val newUser = User(id=claims.payload.subject.toLong(), authType)
        saveUser(context, newUser)
    }
}
