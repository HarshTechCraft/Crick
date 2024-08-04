package com.example.atry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject

class KeyMoments : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.key_moments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        val recyclerView1 = view.findViewById<RecyclerView>(R.id.rec)
        val recyclerView2 = view.findViewById<RecyclerView>(R.id.rec2)
        val card = view.findViewById<CardView>(R.id.card)
        val not_available = view.findViewById<ImageView>(R.id.not_available)
        val state = arguments?.getString("state")


        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val team1 = bundle?.getString("team1Long")
        val team2 = bundle?.getString("team2Long")
        val s = bundle?.getString("NotificationCompat.CATEGORY_STATUS")
        Log.d("valam","$s")
        tabLayout.addTab(tabLayout.newTab().setText(team1))
        tabLayout.addTab(tabLayout.newTab().setText(team2))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        recyclerView1.visibility = View.VISIBLE
                        recyclerView2.visibility = View.GONE
                    }
                    1 -> {
                        recyclerView1.visibility = View.GONE
                        recyclerView2.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        progressBar.visibility = View.VISIBLE
        val team1Score = bundle?.getString("team1Score")
        val started = bundle?.getBoolean("started")
        Log.d("ekvar","$state $team1Score")
        if (state == "") {
            progressBar.visibility = View.GONE
            card.visibility = View.GONE
            not_available.visibility = View.VISIBLE
        } else {
            val apikey2 = "37c6e5cca6msh60a9a2752d57f87p107407jsn6b7549f1cc88"
            val matchId = bundle?.getString("matchId")
            val volley = Volley.newRequestQueue(requireContext())
            fetchHighlights(
                volley,
                "https://unofficial-cricbuzz.p.rapidapi.com/matches/get-highlights?matchId=$matchId&iid=1",
                recyclerView1,
                "a6944d57e9mshc8c9235b339cbefp1c77abjsn8c3215551da3"
            ) {
                Log.d("after1st", "just fun")
                progressBar.visibility = View.GONE
                fetchHighlights(
                    volley,
                    "https://unofficial-cricbuzz.p.rapidapi.com/matches/get-highlights?matchId=$matchId&iid=2",
                    recyclerView2,
                    apikey2
                )
                Log.d("after2nd", "just fun")
            }
        }
    }

    private fun fetchHighlights(
        volley: RequestQueue,
        url: String,
        recyclerView: RecyclerView,
        apikey: String,
        onComplete: (() -> Unit)? = null
    ) {
        val listener = Response.Listener<JSONObject> { response ->
            handleResponse(response, recyclerView, onComplete)
        }

        val errorListener = Response.ErrorListener { error ->
            handleError(error, onComplete)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, listener, errorListener) {
            override fun getHeaders(): Map<String, String> {
                return mapOf(
                    "x-rapidapi-key" to apikey,
                    "x-rapidapi-host" to "unofficial-cricbuzz.p.rapidapi.com"
                )
            }
        }
        volley.add(jsonObjectRequest)
    }

    private fun handleResponse(response: JSONObject, recyclerView: RecyclerView, onComplete: (() -> Unit)?) {
        try {
            val commentaryLines = response.getJSONArray("commentaryLines")
            val highlights = ArrayList<Data_km>()
            for (i in 0 until commentaryLines.length()) {
                val commentaryLine = commentaryLines.getJSONObject(i)
                val commentary = commentaryLine.getJSONObject("commentary")
                val overNum = commentary.getDouble("overNum")
                val eventType = commentary.getString("eventType")
                var commtxt = commentary.getString("commtxt")
                if (commentary.has("commentaryFormats")) {
                    val commentaryFormats = commentary.getJSONArray("commentaryFormats")
                    for (j in 0 until commentaryFormats.length()) {
                        val format = commentaryFormats.getJSONObject(j)
                        if (format.has("type") && format.getString("type") == "bold") {
                            val valuesArray = format.getJSONArray("value")
                            for (k in 0 until valuesArray.length()) {
                                val valueObj = valuesArray.getJSONObject(k)
                                val id = valueObj.getString("id")
                                val value = valueObj.getString("value")
                                commtxt = commtxt.replace(id, value)
                            }
                        }
                    }
                }
                val filteredEventType = eventType.split(",").last().trim()
                highlights.add(Data_km(commtxt, filteredEventType, overNum))
                Log.d("KeyMoments", "OverNum: $overNum, EventType: $filteredEventType, CommText: $commtxt")
            }
            val context = requireContext()
            val adapter = Adapter_km(context, highlights)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            onComplete?.invoke()
        } catch (e: Exception) {
            Log.d("KeyMoments", "JSON parsing error: ${e.message}")
            if (isAdded) {
                Toast.makeText(requireContext(), "JSON parsing error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleError(error: VolleyError, onComplete: (() -> Unit)?) {
        Log.d("KeyMoments", "Request error: ${error.message}")
        if (isAdded) {
            Toast.makeText(requireContext(), "Request error: ${error.message}", Toast.LENGTH_LONG).show()
        }
        onComplete?.invoke()
    }
}
