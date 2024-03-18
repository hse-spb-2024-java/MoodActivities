package org.hse.moodactivities.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.jsonwebtoken.Jwts
import org.hse.moodactivities.R
import org.hse.moodactivities.models.User
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun updateUser(newUser: User) {
        _user.value = newUser
    }

    fun updateUserFromJwt(context: Context, token: String) {
        val publicKeyBytes = Base64.getDecoder().decode(context.resources.getString(R.string.public_key));
        val keySpec = X509EncodedKeySpec(publicKeyBytes);
        val keyFactory = KeyFactory.getInstance("RSA");
        val publicKey = keyFactory.generatePublic(keySpec);

        val parser = Jwts.parser()
            .verifyWith(publicKey)
            .build()

        val claims = parser.parseSignedClaims(token)

        val newUser = User(id=claims.payload.subject.toLong())
        _user.value = newUser
    }
}
