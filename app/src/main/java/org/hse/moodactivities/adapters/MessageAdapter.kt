package org.hse.moodactivities.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.models.Message

class MessageAdapter(private val context: Context, private val messages: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val USER_MESSAGE = 1
        private const val SYSTEM_MESSAGE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            USER_MESSAGE -> {
                val view = inflater.inflate(R.layout.item_user_message, parent, false)
                UserMessageViewHolder(view)
            }

            SYSTEM_MESSAGE -> {
                val view = inflater.inflate(R.layout.item_system_message, parent, false)
                SystemMessageViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            USER_MESSAGE -> (holder as UserMessageViewHolder).bind(message)
            SYSTEM_MESSAGE -> (holder as SystemMessageViewHolder).bind(message)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.isFromUser) {
            USER_MESSAGE
        } else {
            SYSTEM_MESSAGE
        }
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun getView(position: Int): View {
        val layout =
            if (getItemViewType(position) == USER_MESSAGE) R.layout.item_user_message else R.layout.item_system_message
        return LayoutInflater.from(context).inflate(
            layout,
            null,
            false
        )
    }

    internal class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.text_message_body)

        fun bind(message: Message) {
            messageText.text = message.text
            itemView.foregroundGravity = Gravity.END
        }
    }

    internal class SystemMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.text_message_body)

        fun bind(message: Message) {
            messageText.text = message.text
            itemView.foregroundGravity = Gravity.START
        }
    }
}
