package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MainActivity
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.adapters.ItemAdapter
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.interfaces.ItemHolderFragment
import org.hse.moodactivities.models.Item
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA
import kotlin.collections.HashSet


class ChatFragment : Fragment() {
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var messagesView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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