package org.hse.moodactivities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.adapters.ItemAdapter
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.models.ActivityItem

class EmotionsChoosingFragment : Fragment() {
    private lateinit var communicator : Communicator
    private var activeActivityIndex: Int = -1
    private var recyclerView: RecyclerView? = null
    private var charItem: ArrayList<ActivityItem>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var itemsAdapters: ItemAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities_choosing, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        gridLayoutManager =
            GridLayoutManager(activity?.applicationContext, 3, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        charItem = ArrayList()
        charItem = createEmotions()
        itemsAdapters = activity?.applicationContext?.let { ItemAdapter(it, charItem!!) }
        recyclerView?.adapter = itemsAdapters

        communicator = activity as Communicator

        //
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            Log.i("fragment_changing", "return to day rating")
            communicator.replaceFragment(DayRatingFragment())
        }

        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            Log.d("next button", "clicked!")
            if (activeActivityIndex != -1) {
                communicator.replaceFragment(ActivitiesChoosingFragment())
//                val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
//                ft.replace(R.id.details, NewFragmentToReplace(), "NewFragmentTag")
//                val mainActivityIntent = Intent(this.activity, ActivitiesChoosingFragment::class.java)
//                startActivity(mainActivityIntent)
            }
        }
    }

    private fun createEmotions(): ArrayList<ActivityItem> {
        return ArrayList()
    }
}
