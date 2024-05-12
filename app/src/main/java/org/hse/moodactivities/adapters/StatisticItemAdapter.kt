package org.hse.moodactivities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.models.StatisticItem
import org.hse.moodactivities.services.ChartsService

class StatisticItemAdapter(
    var context: Context, private var arrayList: ArrayList<StatisticItem>
) : RecyclerView.Adapter<StatisticItemAdapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_statistic, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item: StatisticItem = arrayList[position]

        holder.position.text = createPositionTittle(position + 1)
        holder.name.text = item.getName()
        holder.icon.setImageResource(item.getIconId())
        holder.counter.text = ChartsService.createCounterText(item.getCounter())
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var position: TextView = itemView.findViewById(R.id.position)
        var name: TextView = itemView.findViewById(R.id.name)
        var icon: ImageView = itemView.findViewById(R.id.icon_image)
        var counter: TextView = itemView.findViewById(R.id.counter)
    }

    private fun createPositionTittle(position: Int): String {
        return buildString {
            append("#")
            append((position).toString())
        }
    }
}
