package com.example.atry

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Upcoming : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.upcoming, container, false)
        val apiKey = requireContext().getString(R.string.apiKey)
        val url = "https://cricbuzz-cricket.p.rapidapi.com/schedule/v1/international"
        val rec = view.findViewById<RecyclerView>(R.id.rec)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
            { response ->
                handleResponse(rec, response)
                progressBar.visibility = View.GONE

            },
            { error ->
                handleVolleyError(error)
            }) {

            override fun getHeaders(): Map<String, String> {
                return mapOf(
                    "x-rapidapi-key" to apiKey,
                    "x-rapidapi-host" to "cricbuzz-cricket.p.rapidapi.com"
                )
            }
        }

        val volley = Volley.newRequestQueue(context)
        volley.add(jsonObjectRequest)
        return view
    }

    private fun handleResponse(rec: RecyclerView?, response: JSONObject?) {
        try {
            val matchScheduleMaps = response?.getJSONArray("matchScheduleMap")
            val list = ArrayList<Data_upcoming>()

            if (matchScheduleMaps != null) {
                for (i in 0 until matchScheduleMaps.length()) {
                    val matchScheduleMap = matchScheduleMaps.getJSONObject(i)
                    if (matchScheduleMap.has("scheduleAdWrapper")) {
                        val scheduleAdWrapper = matchScheduleMap.getJSONObject("scheduleAdWrapper")
                        val date = scheduleAdWrapper.getString("date")
                        val matchScheduleLists = scheduleAdWrapper.getJSONArray("matchScheduleList")

                        for (j in 0 until matchScheduleLists.length()) {
                            val matchScheduleList = matchScheduleLists.getJSONObject(j)
                            val seriesName = matchScheduleList.getString("seriesName")
                            val matchInfos = matchScheduleList.getJSONArray("matchInfo")

                            for (k in 0 until matchInfos.length()) {
                                val matchInfo = matchInfos.getJSONObject(k)
                                val matchId = matchInfo.getInt("matchId")
                                val seriesId = matchInfo.getInt("seriesId")
                                val matchDesc = matchInfo.getString("matchDesc")
                                val matchFormat = matchInfo.getString("matchFormat")
                                val team1Obj = matchInfo.getJSONObject("team1")
                                val team2Obj = matchInfo.getJSONObject("team2")
                                val team1Name = team1Obj.getString("teamName")
                                val team2Name = team2Obj.getString("teamName")
                                val team1Short = team1Obj.getString("teamSName")
                                val team2Short = team2Obj.getString("teamSName")
                                val team1Img = team1Obj.getInt("imageId")
                                val team2Img = team2Obj.getInt("imageId")
                                val venueInfo = matchInfo.getJSONObject("venueInfo")
                                val ground = venueInfo.getString("ground")
                                val city = venueInfo.getString("city")
                                val country = venueInfo.getString("country")

                                val data = Data_upcoming(
                                    matchId, seriesId, matchDesc, matchFormat, team1Name, team2Name,
                                    team1Short, team2Short, team1Img, team2Img, ground, city, country,
                                    date, seriesName
                                )
                                list.add(data)
                            }
                        }
                    }
                }
            }


            val adapter = Adapter_upcoming(requireContext(), list)
            if (rec != null) {
                rec.layoutManager = LinearLayoutManager(requireContext())
                rec.adapter = adapter
            }

        } catch (e: JSONException) {
            Log.d("qwert", "JSON parsing error: ${e.message}")
            Toast.makeText(requireContext(), "JSON parsing error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleError(error: VolleyError) {
        Log.d("qwert", "Error: ${error.message}")
        Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
    }

    private fun handleVolleyError(error: VolleyError) {
        val networkResponse = error.networkResponse
        if (networkResponse != null) {
            val statusCode = networkResponse.statusCode
            val data = networkResponse.data
            val responseData = String(data, Charsets.UTF_8)
            Log.d("crickbuzz", "Volley error. Status code: $statusCode. Response data: $responseData")
            Toast.makeText(requireContext(), "Volley error: $statusCode\n$responseData", Toast.LENGTH_LONG).show()
        } else {
            Log.d("crickbuzz", "Volley error: ${error.message}")
            Toast.makeText(requireContext(), "Volley error: ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}
