package com.example.atry

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class Wct20 : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.wct20, container, false)
        val apiKey = requireContext().getString(R.string.apiKey)
        val url = "https://cricbuzz-cricket.p.rapidapi.com/matches/v1/live"
        val rec: RecyclerView = view.findViewById(R.id.rec)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
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
        val volley = Volley.newRequestQueue(requireContext())
//        jsonObjectRequest.setShouldCache(false)
        volley.add(jsonObjectRequest)
        return view
    }

    private fun handleResponse(rec: RecyclerView, response: JSONObject) {
        try {
            val allMatches = mutableListOf<Data_match>()
            val matchDetails = response.getJSONArray("typeMatches")

            for (i in 0 until matchDetails.length()) {
                val typeMatche = matchDetails.getJSONObject(i)
                if (typeMatche.getString("matchType") == "League") {
                    continue
                }
                val seriesMatches = typeMatche.getJSONArray("seriesMatches")
                for (j in 0 until seriesMatches.length()) {
                    val seriesMatche = seriesMatches.getJSONObject(j)
                    if (seriesMatche.has("seriesAdWrapper")) {
                        val seriesAdWrapper = seriesMatche.getJSONObject("seriesAdWrapper")
                        val matches = seriesAdWrapper.getJSONArray("matches")
                        for (k in 0 until matches.length()) {
                            val match = matches.getJSONObject(k)
                            val matchInfo = match.getJSONObject("matchInfo")
                            val matchType = matchInfo.getString("seriesName") + "," + matchInfo.getString("matchDesc")
                            val formattedMatchType = matchType.replaceFirst(",", "\n")
                            val team1 = matchInfo.getJSONObject("team1").getString("teamName")
                            val team2 = matchInfo.getJSONObject("team2").getString("teamName")
                            val venue = matchInfo.getJSONObject("venueInfo").getString("ground") + "," + matchInfo.getJSONObject("venueInfo").getString("city")
                            val matchId = matchInfo.getString("matchId")
                            val status = matchInfo.getString("status")
                            val team1Short = matchInfo.getJSONObject("team1").getString("teamSName")
                            val team2Short = matchInfo.getJSONObject("team2").getString("teamSName")
                            val state = matchInfo.getString("state")
                            val seriesId = matchInfo.getString("seriesId")
                            var team1Score = ""
                            var team2Score = ""
                            var inngs1Over = 0.0
                            var inngs2Over = 0.0

                            if (match.has("matchScore")) {
                                val matchScore = match.getJSONObject("matchScore")
                                val team1ScoreObj = matchScore.getJSONObject("team1Score").getJSONObject("inngs1")
                                val team1ScoreRuns = team1ScoreObj.getInt("runs")
                                val team1ScoreWickets = team1ScoreObj.getInt("wickets")
                                inngs1Over = team1ScoreObj.getDouble("overs")

                                val team2ScoreObj = matchScore.getJSONObject("team2Score").getJSONObject("inngs1")
                                val team2ScoreRuns = team2ScoreObj.getInt("runs")
                                val team2ScoreWickets = team2ScoreObj.optInt("wickets")
                                inngs2Over = team2ScoreObj.getDouble("overs")

                                team1Score = "$team1ScoreRuns/$team1ScoreWickets"
                                team2Score = "$team2ScoreRuns/$team2ScoreWickets"
                            }

                            val matchData = Data_match(
                                matchId, team1, team2, formattedMatchType, "", 0, venue, status,
                                team1Short, team2Short, seriesId, state, team1Score, team2Score,
                                inngs1Over, inngs2Over
                            )
                            allMatches.add(matchData)
                        }
                    }
                }
            }
            val adapter = Adapter_wc2(requireContext(), allMatches)
            rec.layoutManager = LinearLayoutManager(requireContext())
            rec.adapter = adapter
        } catch (e: JSONException) {
            Log.d("crickbuzz", "JSON parsing error: ${e.message}")
            Toast.makeText(requireContext(), "JSON parsing error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleVolleyError(error: VolleyError) {
        val networkResponse = error.networkResponse
        if (networkResponse != null) {
            val statusCode = networkResponse.statusCode
            val responseData = String(networkResponse.data, Charsets.UTF_8)
            Log.d("crickbuzz", "Volley error. Status code: $statusCode. Response data: $responseData")
            Toast.makeText(requireContext(), "Volley error: $statusCode\n$responseData", Toast.LENGTH_LONG).show()
        } else {
            Log.d("crickbuzz", "Volley error: ${error.message}")
            Toast.makeText(requireContext(), "Volley error: ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}
