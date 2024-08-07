package com.example.atry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class Squad : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.squad, container, false)
        val apiKey = requireContext().getString(R.string.apiKey)
        val bundle = arguments
        val matchId = bundle?.getString("matchId")
        val squad = bundle?.getString("squad")
        val seriesId = bundle?.getString("seriesId")
        val team1img = bundle?.getString("team1img")
        val team2img = bundle?.getString("team2img")
        val rec = view.findViewById<RecyclerView>(R.id.rec)
        val team1Long = bundle?.getString("team1Long")
        val team2Long = bundle?.getString("team2Long")
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        Log.d("squad", "squad value is $squad")
        Log.d("seriesId", "seriesId value is $seriesId")
        Log.d("matchId", "matchId value is $matchId")



        val url = "https://cricbuzz-cricket.p.rapidapi.com/mcenter/v1/$matchId"
        progressBar.visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.squadNot).visibility = View.GONE
        view.findViewById<ConstraintLayout>(R.id.squadmain).visibility = View.GONE
        val state = arguments?.getString("state")
        val not_available = view.findViewById<ImageView>(R.id.not_available)

        if (state == "") {
            progressBar.visibility = View.GONE
            not_available.visibility = View.VISIBLE

        }
        else{
            val team1Score = bundle?.getString("team1Score")
            if (team1Score == "null") {
                progressBar.visibility = View.GONE
            } else {
                val requestQueue = Volley.newRequestQueue(requireContext())
                fetchSquadData(requestQueue, url, apiKey, progressBar, view, team1img, team2img, rec)
            }
        }


        return view
    }

    private fun fetchSquadData(
        requestQueue: RequestQueue, url: String, apiKey: String, progressBar: ProgressBar,
        view: View, team1img: String?, team2img: String?, rec: RecyclerView
    ) {
        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener { response ->
                handleResponse(response, progressBar, view, team1img, team2img, rec)
            },
            Response.ErrorListener { error ->
                handleError(error)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return mapOf(
                    "x-rapidapi-key" to apiKey,
                    "x-rapidapi-host" to "cricbuzz-cricket.p.rapidapi.com"
                )
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    private fun handleResponse(
        response: JSONObject, progressBar: ProgressBar, view: View, team1img: String?,
        team2img: String?, rec: RecyclerView
    ) {
        progressBar.visibility = View.GONE
        val flattenedData = ArrayList<DataSquad>()
        view.findViewById<ConstraintLayout>(R.id.squadmain).visibility = View.VISIBLE

        val matchInfo = response.getJSONObject("matchInfo")
        val team1 = matchInfo.getJSONObject("team1")
        val playerDetails1 = team1.getJSONArray("playerDetails")
        val team2 = matchInfo.getJSONObject("team2")
        val playerDetails2 = team2.getJSONArray("playerDetails")

        val team1Name = team1.getString("name")
        val team2Name = team2.getString("name")

        flattenedData.add(DataSquad(true, team1Name, team2img, null, null))
        flattenedData.add(DataSquad(false, null, null, null, parsePlayer(playerDetails1)))
        flattenedData.add(DataSquad(true, team2Name, null, team1img, null))
        flattenedData.add(DataSquad(false, null, null, null, parsePlayer(playerDetails2)))

        Log.d("hummai", "$flattenedData")

        val adapter = AdapterSquad(requireContext(), flattenedData)
        rec.layoutManager = LinearLayoutManager(requireContext())
        rec.adapter = adapter
    }

    private fun handleError(error: VolleyError) {
        Log.d("squad2", "JSON parsing error: $error")
    }

    private fun parsePlayer(players: JSONArray): List<teamInfo> {
        val list = mutableListOf<teamInfo>()
        for (i in 0 until players.length()) {
            val obj = players.getJSONObject(i)
            val squad = teamInfo(
                obj.optInt("id"),
                obj.optString("fullName"),
                obj.optString("role"),
                obj.optString("battingStyle", ""),
                obj.optString("bowlingStyle", ""),
                obj.optBoolean("country"),
                obj.optInt("faceImageId")
            )
            list.add(squad)
        }
        return list
    }
}