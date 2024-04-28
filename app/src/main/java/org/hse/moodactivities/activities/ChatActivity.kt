package org.hse.moodactivities.activities

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hse.moodactivities.R
import org.hse.moodactivities.adapters.MessageAdapter
import org.hse.moodactivities.fragments.MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER
import org.hse.moodactivities.fragments.MAXIMAL_SIZE_OF_USER_ANSWER
import org.hse.moodactivities.models.Message
import org.hse.moodactivities.services.GptService
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA
import java.net.HttpURLConnection.HTTP_BAD_REQUEST


class ChatActivity : AppCompatActivity() {
    private lateinit var messageInput: EditText
    private lateinit var sendButton: AppCompatImageButton
    private lateinit var messagesView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var gptService: GptService
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messagesView = findViewById(R.id.messages_view)
        var layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        messagesView.layoutManager = layoutManager
        messageInput = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)
        gptService = GptService(this as AppCompatActivity)
        gptService.onCreate()

        messageAdapter = MessageAdapter(this, mutableListOf<Message>())
        messagesView.adapter = messageAdapter

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

            var message = Message(userMessage, true)
            messageAdapter.addMessage(message)
            messages.add(message)
            messagesView.smoothScrollToPosition(messages.size - 1)
            messageInput.setText("")

            runBlocking {
                val job = launch {
                    gptService.sendRequest(userMessage)
                    gptService.waitForResponse()
                    var response = gptService.getResponse()
                    if (response.first < HTTP_BAD_REQUEST) {
                        var message = Message(response.second, false)
                        messageAdapter.addMessage(message)
                        messages.add(message)
                        messagesView.smoothScrollToPosition(messages.size - 1)
                        messageInput.isEnabled = true
                        sendButton.alpha = BUTTON_ENABLED_ALPHA
                        sendButton.isEnabled = true
                    } else {
                        // TODO: Handle errors
                        var message = Message(response.second, false)
                        messageAdapter.addMessage(message)
                        messages.add(message)
                        messagesView.smoothScrollToPosition(messages.size - 1)
                        messageInput.isEnabled = true
                        sendButton.alpha = BUTTON_ENABLED_ALPHA
                        sendButton.isEnabled = true
                    }
                }
            }
        }
    }
}
