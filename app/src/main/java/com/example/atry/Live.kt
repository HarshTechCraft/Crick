package com.example.atry

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject
import java.util.Objects

class Live : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.live, container, false)
        val bundle = arguments ?: return view

        val matchId = bundle.getString("matchId")
        val team1 = bundle.getString("team1")
        val team2 = bundle.getString("team2")
        val team1Img = bundle.getString("team1img")
        val team2Img = bundle.getString("team2img")
        val team1Score = bundle.getString("team1Score")
        val team2Score = bundle.getString("team2Score")
        val team1Over = bundle.getDouble("team1Over")
        val team2Over = bundle.getDouble("team2Over")

        val team1ScoreView: TextView = view.findViewById(R.id.team1score)
        val team2ScoreView: TextView = view.findViewById(R.id.team2score)
        val team1OverView: TextView = view.findViewById(R.id.team1over)
        val team2OverView: TextView = view.findViewById(R.id.team2over)
        val timeView: TextView = view.findViewById(R.id.time)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.team1).text = team1
        view.findViewById<TextView>(R.id.team2).text = team2
        Glide.with(requireContext()).load(team1Img).into(view.findViewById(R.id.team1img))
        Glide.with(requireContext()).load(team2Img).into(view.findViewById(R.id.team2img))

        if (team1Score == "" || team2Score == "") {
            Log.d("testkar1", "$team1Score")
            team1ScoreView.text = ""
            team2ScoreView.text = ""
            team1OverView.text = ""
            team2OverView.text = ""
            timeView.text = bundle.getString("date")
            progressBar.visibility = View.GONE

        }
        else {
            Log.d("testkar2", "$team1Score")
            team1ScoreView.text = team1Score
            team2ScoreView.text = team2Score
            team1OverView.text = adjustOvers(team1Over)
            team2OverView.text = adjustOvers(team2Over)
            timeView.text = bundle.getString(NotificationCompat.CATEGORY_STATUS)

            val recComm: RecyclerView = view.findViewById(R.id.rec_comm)
            val apiKey = requireContext().getString(R.string.apiKey)
            val url = "https://cricbuzz-cricket.p.rapidapi.com/mcenter/v1/$matchId/comm"

            val request = object:JsonObjectRequest(Method.GET, url, null,
                { response ->
                    handleResponse(response, recComm)
                    progressBar.visibility = View.GONE

                },
                { error ->
                    handleVolleyError(error)
                    progressBar.visibility = View.GONE

                }
            ){
                override fun getHeaders(): Map<String, String> {
                    return mapOf(
                        "x-rapidapi-key" to apiKey,
                        "x-rapidapi-host" to "cricbuzz-cricket.p.rapidapi.com"
                    )
                }
            }

            Volley.newRequestQueue(requireContext()).add(request)
        }

        return view
    }

    private fun handleResponse(response: JSONObject, recComm: RecyclerView) {
        try {
            val commentry = mutableListOf<DataComm>()
            val commentaryList = response.getJSONArray("commentaryList")

            for (i in 0 until commentaryList.length()) {
                val commentaryData = commentaryList.getJSONObject(i)
                val commText = commentaryData.getString("commText").replace("\\n", "\n")
                val commentaryFormats = commentaryData.getJSONObject("commentaryFormats")

                if (commentaryFormats.has("bold")) {
                    val boldFormats = commentaryFormats.getJSONObject("bold")
                    val formatId = boldFormats.getJSONArray("formatId").getString(0)
                    val head = boldFormats.getJSONArray("formatValue").getString(0)
                    val text = commText.replace(formatId, "")
                    commentry.add(DataComm(head, text))
                } else {
                    commentry.add(DataComm("", commText))
                }
            }

            val adapter = AdapterComm(requireContext(), commentry)
            recComm.layoutManager = LinearLayoutManager(context)
            recComm.adapter = adapter

        } catch (e: JSONException) /* no-op */{
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

    private fun adjustOvers(overs: Double): String {
        return if (overs % 1.0 == 0.6) {
            "${overs.toInt() + 1}.0"
        } else {
            overs.toString()
        }
    }
}
