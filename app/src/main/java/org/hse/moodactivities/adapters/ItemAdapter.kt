package org.hse.moodactivities.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.interfaces.ItemHolderFragment
import org.hse.moodactivities.models.ActivatedItem
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA

class ItemAdapter(
    var context: Context,
    private var arrayList: ArrayList<ActivatedItem>
) :
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
        val item : ActivatedItem = arrayList[position]

        holder.icon.setImageResource(item.getIconIndex())
        holder.title.text = item.getName()
        holder.cardView.setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                item.getIconColor()
            )
        )
        if (item.getIsActive()) {
            Log.d("change", "set background")
            holder.cardView.alpha = BUTTON_ENABLED_ALPHA
        }

        holder.button.setOnClickListener {
            val fragment: ItemHolderFragment =
                holder.activity.supportFragmentManager.fragments[0] as ItemHolderFragment
            fragment.clickButton(holder.cardView, holder.title.text as String)
        }
    }

    class ItemHolder(itemView: View, parent: ViewGroup) :
        RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.icon_image_view)
        var title: TextView = itemView.findViewById(R.id.title_text_view)
        var activity: MoodFlowActivity = parent.context as MoodFlowActivity
        var cardView: CardView = itemView.findViewById(R.id.icon_background)
        var button: Button = itemView.findViewById(R.id.button)
    }
}
