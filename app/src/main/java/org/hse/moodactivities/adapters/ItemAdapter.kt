package org.hse.moodactivities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.fragments.ActivitiesChoosingFragment
import org.hse.moodactivities.models.ActivityItem

class ItemAdapter(var context: Context, private var arrayList: ArrayList<ActivityItem>) :
    RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_view_layout_items, parent, false)
        return ItemHolder(viewHolder, parent)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val activityItem: ActivityItem = arrayList[position]

        holder.icon.setImageResource(activityItem.getIconIndex())
        holder.title.text = activityItem.getText()
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, activityItem.getIconColor()))

        holder.title.setOnClickListener {
            Toast.makeText(context, activityItem.getText(), Toast.LENGTH_LONG).show()
        }

        holder.button.setOnClickListener {
            val fragment : ActivitiesChoosingFragment = holder.activity.supportFragmentManager.fragments[0] as ActivitiesChoosingFragment
            fragment.clickButton(holder.cardView, holder.title.text as String)
        }
    }

    class ItemHolder(itemView: View, parent: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.icon_image_view)
        var title: TextView = itemView.findViewById(R.id.title_text_view)
        var cardView: CardView = itemView.findViewById(R.id.icon_background)
        var button: Button = itemView.findViewById(R.id.button)
        var activity: MoodFlowActivity = parent.context as MoodFlowActivity
    }
}