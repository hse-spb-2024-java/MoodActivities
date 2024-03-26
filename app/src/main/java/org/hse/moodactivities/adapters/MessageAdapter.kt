package org.hse.moodactivities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.hse.moodactivities.R

class MessageAdapter(context: Context) : ArrayAdapter<String>(context, R.layout.item_message) {

    private val messages: MutableList<String> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        }

        val messageTextView = view?.findViewById<TextView>(R.id.message_text)
        messageTextView?.text = getItem(position)

        return view!!
    }

    fun addMessage(message: String) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(position: Int): String? {
        return messages[position]
    }
}
