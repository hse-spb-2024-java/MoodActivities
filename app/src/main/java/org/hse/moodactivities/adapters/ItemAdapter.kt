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
import org.hse.moodactivities.data_types.MoodFlowType
import org.hse.moodactivities.fragments.ChooseActivitiesFragment
import org.hse.moodactivities.fragments.ChooseEmotionsFragment
import org.hse.moodactivities.models.ActivityItem

class ItemAdapter(
    var context: Context,
    private var arrayList: ArrayList<ActivityItem>,
    private var fragmentType: MoodFlowType
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
        val activityItem: ActivityItem = arrayList[position]

        holder.icon.setImageResource(activityItem.getIconIndex())
        holder.title.text = activityItem.getText()
        holder.cardView.setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                activityItem.getIconColor()
            )
        )
        if (activityItem.getIsActive()) {
            Log.d("change", "set background")
            holder.cardView.alpha = 1.0f
        }

        holder.button.setOnClickListener {
            if (fragmentType == MoodFlowType.ACTIVITIES_CHOOSING) {
                val fragment: ChooseActivitiesFragment =
                    holder.activity.supportFragmentManager.fragments[0] as ChooseActivitiesFragment
                fragment.clickButton(holder.cardView, holder.title.text as String)
            } else {
                val fragment: ChooseEmotionsFragment =
                    holder.activity.supportFragmentManager.fragments[0] as ChooseEmotionsFragment
                fragment.clickButton(holder.cardView, holder.title.text as String)
            }
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
