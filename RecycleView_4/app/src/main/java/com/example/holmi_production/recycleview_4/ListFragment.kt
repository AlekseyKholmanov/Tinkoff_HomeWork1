package com.example.holmi_production.recycleview_4

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.holmi_production.recycleview_4.NewsItems.HeaderItem
import com.example.holmi_production.recycleview_4.NewsItems.ListItem
import com.example.holmi_production.recycleview_4.Adapters.NewsAdapter
import com.example.holmi_production.recycleview_4.Interfaces.onNewsClickListener
import com.example.holmi_production.recycleview_4.NewsItems.NewsItem
import com.example.holmi_production.recycleview_4.Model.News
import com.example.holmi_production.recycleview_4.utils.DateUtils
import java.util.*
import kotlin.collections.ArrayList

class ListFragment : Fragment() {

    private var news: ArrayList<ListItem> = arrayListOf()
    companion object {
        private const val ARG_IS_FAVORITE = "isFavorite"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.listRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val isFav = if(arguments == null) false else arguments!!.getBoolean(ARG_IS_FAVORITE)
        val events = toMap(loadNews())

        setHeader(events)
        val adapter = NewsAdapter(news)
        recyclerView.adapter = adapter
        return view
    }

    private fun setHeader(events: Map<Date, List<News>>) {
        for (date in events.keys) {
            val header = HeaderItem(date)
            news.add(header)
            for (event in events.getValue(date)) {
                val item = NewsItem(event)
                news.add(item)
            }
        }
    }

    private fun loadNews():List<News>{
        val events = ArrayList<News>()
        val content = resources.getString(R.string.lorem)
        for (i in 1..49) {
            events.add(News("Why is lorem theme $i ?",
                buildRandomDateInCurrentMonth(),
                content,
                false))
        }
        return events
    }

    private fun buildRandomDateInCurrentMonth(): Date {
        val random = Random()
        val todayDay= DateUtils().currentDay()
        return DateUtils().buildDate(random.nextInt(todayDay) + 1)
    }

    private fun toMap(events: List<News>): Map<Date, List<News>> {
        val map = TreeMap<Date, MutableList<News>>()
        for (event in events) {
            var value: MutableList<News>? = map[event.date]
            if (value == null) {
                value = ArrayList()
                map[event.date] = value
            }
            value.add(event)
        }
        return map.descendingMap()
    }


}