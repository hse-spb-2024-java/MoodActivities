package org.hse.moodactivities.activities

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
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
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA
import java.net.HttpURLConnection.HTTP_BAD_REQUEST


class ChatActivity : AppCompatActivity() {
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var sendButtonBackground: CardView
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
        sendButtonBackground = findViewById(R.id.send_button_background)
        gptService = GptService(this as AppCompatActivity)
        gptService.onCreate()

        messageAdapter = MessageAdapter(this, mutableListOf())
        messagesView.adapter = messageAdapter

        val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(MAXIMAL_SIZE_OF_USER_ANSWER))
        messageInput.filters = filters
        messageInput.maxLines = MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER
        messageInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (messageInput.text.isNotEmpty()) {
                    sendButtonBackground.alpha = BUTTON_ENABLED_ALPHA
                    sendButton.isEnabled = true
                } else {
                    sendButtonBackground.alpha = BUTTON_DISABLED_ALPHA
                    sendButton.isEnabled = false
                }
            }
        })

        // listen for send button click
        sendButton.setOnClickListener {
            val userMessage = messageInput.text.toString()
            messageInput.isEnabled = false
            sendButtonBackground.alpha = BUTTON_DISABLED_ALPHA
            sendButton.isEnabled = false

            val message = Message(userMessage, true)
            messageAdapter.addMessage(message)
            messages.add(message)
            messagesView.smoothScrollToPosition(messages.size - 1)
            messageInput.setText("")

            runBlocking {
                launch {
                    gptService.sendRequest(userMessage)
                    gptService.waitForResponse()
                    val response = gptService.getResponse()
                    if (response.first < HTTP_BAD_REQUEST) {
                        val message = Message(response.second, false)
                        messageAdapter.addMessage(message)
                        messages.add(message)
                        messagesView.smoothScrollToPosition(messages.size - 1)
                        messageInput.isEnabled = true
                        sendButtonBackground.alpha = BUTTON_ENABLED_ALPHA
                        sendButton.isEnabled = true
                    } else {
                        // TODO: Handle errors
                        val message = Message(response.second, false)
                        messageAdapter.addMessage(message)
                        messages.add(message)
                        messagesView.smoothScrollToPosition(messages.size - 1)
                        messageInput.isEnabled = true
                        sendButtonBackground.alpha = BUTTON_ENABLED_ALPHA
                        sendButton.isEnabled = true
                    }
                }
            }
        }

        setColorTheme()
    }

    private fun setColorTheme() {
        val colorTheme = ThemesService.getColorTheme()

        // set color to status bar
        window.statusBarColor = colorTheme.getBackgroundColor()

        // set bar color
        findViewById<LinearLayout>(R.id.input_bar).setBackgroundColor(colorTheme.getDimmedBackgroundColor())

        // set color to background
        findViewById<ConstraintLayout>(R.id.activity_chat).setBackgroundColor(colorTheme.getBackgroundColor())

        // set screen name
        findViewById<TextView>(R.id.app_name).setTextColor(colorTheme.getFontColor())

        // set message input color
        findViewById<CardView>(R.id.message_input_background).setCardBackgroundColor(colorTheme.getMessageInputColor())

        // set message input text color
        findViewById<EditText>(R.id.message_input).setTextColor(colorTheme.getMessageInputTextColor())

        // set message input hint text color
        findViewById<EditText>(R.id.message_input).setHintTextColor(colorTheme.getMessageInputHintTextColor())

        // set send button color
        sendButtonBackground.setCardBackgroundColor(colorTheme.getColor4())
    }
}
