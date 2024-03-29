package org.hse.moodactivities.activities

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hse.moodactivities.R
import org.hse.moodactivities.adapters.MessageAdapter
import org.hse.moodactivities.fragments.MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER
import org.hse.moodactivities.fragments.MAXIMAL_SIZE_OF_USER_ANSWER
import org.hse.moodactivities.services.GptService
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA
import java.net.HttpURLConnection.HTTP_BAD_REQUEST


class ChatActivity : AppCompatActivity() {
    private lateinit var messageInput: EditText
    private lateinit var sendButton: AppCompatImageButton
    private lateinit var messagesView: ListView
    private lateinit var messageAdapter: MessageAdapter
    private var gptService: GptService = GptService(this as AppCompatActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Initialize views
        messagesView = findViewById(R.id.messages_view)
        messageInput = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)
        gptService.onCreate()

        // Set up adapter for messages ListView
        messageAdapter = MessageAdapter(this)
        messagesView.adapter = messageAdapter

        // Set up input field
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
            messageInput.isEnabled = false
            sendButton.alpha = BUTTON_DISABLED_ALPHA
            sendButton.isEnabled = false

            runBlocking {
                val job = launch {
                    gptService.sendRequest(userMessage)
                    gptService.waitForResponse()
                    var response = gptService.getResponse()
                    if (response.first < HTTP_BAD_REQUEST) {
                        messageAdapter.addMessage("Chat: ${response.second}")
                        messagesView.smoothScrollToPosition(messageAdapter.count - 1)
                        messageInput.isEnabled = true
                        sendButton.alpha = BUTTON_ENABLED_ALPHA
                        sendButton.isEnabled = true
                    } else {
                        // TODO: Handle errors
                        messageAdapter.addMessage("Chat: ${response.second}")
                        messagesView.smoothScrollToPosition(messageAdapter.count - 1)
                        messageInput.isEnabled = true
                        sendButton.alpha = BUTTON_ENABLED_ALPHA
                        sendButton.isEnabled = true
                    }
                }
                messageAdapter.addMessage("You: $userMessage")
                messagesView.smoothScrollToPosition(messageAdapter.count - 1)
                messageInput.setText("")
            }
        }
    }
}
