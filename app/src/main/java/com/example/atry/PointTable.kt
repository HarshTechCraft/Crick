package com.example.atry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlin.text.Charsets
import org.json.JSONObject

class PointTable : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_point_table, container, false)
        val bundle = arguments
        val seriesId = bundle?.getString("seriesId")
        val apiKey = requireContext().getString(R.string.apiKey)
        val url = "https://cricbuzz-cricket.p.rapidapi.com/stats/v1/series/$seriesId/points-table"
        val rec = view.findViewById<RecyclerView>(R.id.rec)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val team1Score = bundle?.getString("team1Score")

        progressBar.visibility = View.VISIBLE
        val warn = view.findViewById<TextView>(R.id.warn)

        if (team1Score == "null") {
            progressBar.visibility = View.GONE
        } else {
            warn.visibility = View.GONE
            rec.visibility = View.VISIBLE

            val listener = Response.Listener<JSONObject> { response ->
                handleResponse(response, progressBar, rec)
            }

            val errorListener = Response.ErrorListener { error ->
                handleVolleyError(error)
            }

            val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, listener, errorListener) {
                override fun getHeaders(): Map<String, String> {
                    return mapOf(
                        "x-rapidapi-key" to apiKey,
                        "x-rapidapi-host" to "cricbuzz-cricket.p.rapidapi.com"
                    )
                }
            }

            val volley = Volley.newRequestQueue(requireContext())
            volley.add(jsonObjectRequest)
        }

        return view
    }

    private fun handleResponse(response: JSONObject, progressBar: ProgressBar, rec: RecyclerView) {
        progressBar.visibility = View.GONE
        rec.visibility = View.VISIBLE
        val pointsTables = response.getJSONArray("pointsTable")
        val flattenedData = mutableListOf<FlattenedData>()

        for (i in 0 until pointsTables.length()) {
            val pointTable = pointsTables.getJSONObject(i)
            val groupName = pointTable.getString("groupName")
                flattenedData.add(FlattenedData(true, groupName))

            val pointsTableInfos = pointTable.getJSONArray("pointsTableInfo")

            for (j in 0 until pointsTableInfos.length()) {
                val pointsTableInfo = pointsTableInfos.getJSONObject(j)
                val nrr = pointsTableInfo.optString("nrr", "0")
                val teamFullName = pointsTableInfo.getString("teamFullName")
                val teamId = pointsTableInfo.getInt("teamId")
                val teamImageId = pointsTableInfo.getInt("teamImageId")
                val teamName = pointsTableInfo.getString("teamName")
                val matchesPlayed = pointsTableInfo.optInt("matchesPlayed", 0)
                val matchesWon = pointsTableInfo.optInt("matchesWon", 0)
                val matchesLost = pointsTableInfo.optInt("matchesLost", 0)
                val points = pointsTableInfo.optInt("points", 0)
                val noRes = pointsTableInfo.optInt("noRes", 0)

                val temp = PointsTableInfo(nrr, teamFullName, teamId, teamImageId, teamName, matchesPlayed, matchesWon, matchesLost, points, noRes)
                flattenedData.add(FlattenedData(false , null ,temp))
            }
        }

        val adapter = AdapterPt(requireContext(), flattenedData)
        rec.layoutManager = LinearLayoutManager(requireContext())
        rec.adapter = adapter
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