package org.hse.moodactivities.activities

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hse.moodactivities.R
import org.hse.moodactivities.fragments.MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER
import org.hse.moodactivities.fragments.MAXIMAL_SIZE_OF_USER_ANSWER
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA


class ChatActivity : AppCompatActivity() {
    private lateinit var messageInput: EditText
    private lateinit var sendButton: AppCompatImageButton
    private lateinit var messagesView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat) // Устанавливаем макет активности

        // Получаем ссылки на визуальные элементы из макета
        messagesView = findViewById(R.id.messages_view)
        messageInput = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)

        val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(MAXIMAL_SIZE_OF_USER_ANSWER))
        messageInput.filters = filters
        messageInput.maxLines = MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER
        messageInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (messageInput.text.isNotEmpty()) {
                    sendButton.alpha = BUTTON_ENABLED_ALPHA
                    sendButton.isEnabled = true
                } else {
                    sendButton.alpha = BUTTON_DISABLED_ALPHA
                    sendButton.isEnabled = false
                }
            }
        })

        // Listen for send button click
        sendButton.setOnClickListener {
            val userMessage = messageInput.text.toString()
            messageInput.isEnabled = false // Disable input field

            // Simulate system response
            GlobalScope.launch(Dispatchers.Main) {
                // Add animation while waiting for response
                messageInput.setText("Sending...")
                delay(1000) // Simulate system processing time

                // Replace animation with actual response
                messageInput.setText("System: $userMessage")
                messageInput.isEnabled = true // Enable input field
            }
        }
    }
}
