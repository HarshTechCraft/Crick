package com.example.atry

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Scorecard : Fragment() {

    lateinit var scoreCardRec: RecyclerView
    lateinit var scoreCardRec2: RecyclerView
    lateinit var scoreCardRec3: RecyclerView
    lateinit var scoreCardRec4: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.scorecard, container, false)
        val scrollView = view.findViewById<ScrollView>(R.id.scrollView)

        val matchId = arguments?.getString("matchId")
        val state = arguments?.getString("state")
        val team1 = arguments?.getString("team1")
        val team2 = arguments?.getString("team2")

        val scoreCard = view.findViewById<ConstraintLayout>(R.id.scoreCard)
        val name1 = view.findViewById<TextView>(R.id.inning1Header)
        val name2 = view.findViewById<TextView>(R.id.inning2Header)
        val upArrow = view.findViewById<ImageView>(R.id.up)
        val downArrow = view.findViewById<ImageView>(R.id.down)
        val upArrow2 = view.findViewById<ImageView>(R.id.up2)
        val downArrow2 = view.findViewById<ImageView>(R.id.down2)
        val not_available = view.findViewById<ImageView>(R.id.not_available)

        scoreCardRec = view.findViewById(R.id.scoreCardRec)
        scoreCardRec2 = view.findViewById(R.id.scoreCardRec2)
        scoreCardRec3 = view.findViewById(R.id.scoreCardRec3)
        scoreCardRec4 = view.findViewById(R.id.scoreCardRec4)

        val batheader1 = view.findViewById<TableLayout>(R.id.batheader1)
        val batheader2 = view.findViewById<TableLayout>(R.id.batheader2)
        val bowl1header = view.findViewById<TableLayout>(R.id.bowl1header)
        val bowl2header = view.findViewById<TableLayout>(R.id.bowl2header)

        listOf(scoreCardRec, scoreCardRec2, scoreCardRec3, scoreCardRec4, batheader1, batheader2, bowl1header, bowl2header).forEach {
            it.visibility = View.GONE
        }

        name1.text = team1
        name2.text = team2

        updateArrowsVisibility(scoreCardRec, scoreCardRec2, upArrow, downArrow)
        updateArrowsVisibility(scoreCardRec3, scoreCardRec4, upArrow2, downArrow2)

        name1.setOnClickListener {
            toggleVisibilityWithAnimation(scoreCardRec, scoreCardRec2, batheader1, bowl1header)
            updateArrowsVisibility(scoreCardRec, scoreCardRec2, upArrow, downArrow)
        }

        name2.setOnClickListener {
            toggleVisibilityWithAnimation(scoreCardRec3, scoreCardRec4, batheader2, bowl2header)
            updateArrowsVisibility(scoreCardRec3, scoreCardRec4, upArrow2, downArrow2)
            if (scoreCardRec3.visibility == View.VISIBLE) {
                scrollView.post {
                    scrollView.smoothScrollTo(0, scoreCardRec3.top)
                }
            }
        }

        val apiKey = getString(R.string.apiKey)
        val url = "https://cricbuzz-cricket.p.rapidapi.com/mcenter/v1/$matchId/scard"

        if (state != "Complete") {
            scoreCard.visibility = View.GONE
            not_available.visibility = View.VISIBLE
            return view
        }


        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
            { response ->
                handleResponse(response)
            },
            { error ->
                handleVolleyError(error)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return mapOf(
                    "x-rapidapi-key" to apiKey,
                    "x-rapidapi-host" to "cricbuzz-cricket.p.rapidapi.com"
                )
            }
        }
        jsonObjectRequest.setShouldCache(false)
        requestQueue.add(jsonObjectRequest)

        return view
    }

    private fun handleResponse(response: JSONObject) {
        if (isAdded) {
            try {
                val scoreCardArray = response.getJSONArray("scoreCard")
                val inning1Data = scoreCardArray.getJSONObject(0)
                val inning2Data = scoreCardArray.getJSONObject(1)

                val inning1Bat = inning1Data.getJSONObject("batTeamDetails")
                val inning1Bowl = inning1Data.getJSONObject("bowlTeamDetails")
                val inning2Bat = inning2Data.getJSONObject("batTeamDetails")
                val inning2Bowl = inning2Data.getJSONObject("bowlTeamDetails")

                val battingStatsList1 = battingStats(inning1Bat)
                val bowlingStatsList1 = bowlingStats(inning1Bowl)
                val battingStatsList2 = battingStats(inning2Bat)
                val bowlingStatsList2 = bowlingStats(inning2Bowl)

                setupRecyclerView(scoreCardRec, battingStatsList1, AdapterScoreCard::class.java)
                setupRecyclerView(scoreCardRec2, bowlingStatsList1, AdapterScoreCard2::class.java)
                setupRecyclerView(scoreCardRec3, battingStatsList2, AdapterScoreCard3::class.java)
                setupRecyclerView(scoreCardRec4, bowlingStatsList2, AdapterScoreCard4::class.java)
            } catch (e: JSONException) {
                Log.d("crickbuzz", "JSON parsing error: ${e.message}")
                Toast.makeText(requireContext(), "JSON parsing error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, data: List<*>, adapterClass: Class<out RecyclerView.Adapter<*>>) {
        recyclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
            adapter = adapterClass.getConstructor(Context::class.java, List::class.java)
                .newInstance(requireContext(), data) as RecyclerView.Adapter<*>
        }
    }

    private fun handleVolleyError(error: VolleyError) {
        error.networkResponse?.let {
            val statusCode = it.statusCode
            val responseData = String(it.data, Charsets.UTF_8)
            Log.d("crickbuzz", "Volley error. Status code: $statusCode. Response data: $responseData")
            Toast.makeText(requireContext(), "Volley error: $statusCode\n$responseData", Toast.LENGTH_SHORT).show()
        } ?: run {
            Log.d("crickbuzz", "Volley error: ${error.message}")
            Toast.makeText(requireContext(), "Volley error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateArrowsVisibility(scoreCardRec: RecyclerView, scoreCardRec2: RecyclerView, upArrow: ImageView, downArrow: ImageView) {
        if (scoreCardRec.visibility == View.VISIBLE && scoreCardRec2.visibility == View.VISIBLE) {
            upArrow.visibility = View.VISIBLE
            downArrow.visibility = View.GONE
        } else {
            upArrow.visibility = View.GONE
            downArrow.visibility = View.VISIBLE
        }
    }

    private fun updateArrowsVisibility2(scoreCardRec3: RecyclerView, scoreCardRec4: RecyclerView, upArrow2: ImageView, downArrow2: ImageView) {
        if (scoreCardRec3.visibility == View.VISIBLE && scoreCardRec4.visibility == View.VISIBLE) {
            upArrow2.visibility = View.VISIBLE
            downArrow2.visibility = View.GONE
        } else {
            upArrow2.visibility = View.GONE
            downArrow2.visibility = View.VISIBLE
        }
    }

    private fun battingStats(jsonObject: JSONObject): List<BattingStats> {
        val list = mutableListOf<BattingStats>()
        val batsmenData = jsonObject.getJSONObject("batsmenData")
        val keys = batsmenData.keys()

        while (keys.hasNext()) {
            val key = keys.next() as String
            val batsman = batsmenData.getJSONObject(key)

            if (batsman.getString("outDesc").isNotEmpty()) {
                val battingStats = BattingStats(
                    batsman.getString("batName"),
                    batsman.getInt("runs"),
                    batsman.getInt("balls"),
                    batsman.getInt("fours"),
                    batsman.getInt("sixes"),
                    batsman.getDouble("strikeRate"),
                    batsman.getString("outDesc")
                )
                list.add(battingStats)
            }
        }
        return list
    }

    private fun bowlingStats(jsonObject: JSONObject): List<BowlingStats> {
        val list = mutableListOf<BowlingStats>()
        val bowlersData = jsonObject.getJSONObject("bowlersData")
        val keys = bowlersData.keys()

        while (keys.hasNext()) {
            val key = keys.next() as String
            val bowler = bowlersData.getJSONObject(key)

            val bowlingStats = BowlingStats(
                bowler.getString("bowlName"),
                bowler.getDouble("overs"),
                bowler.getInt("maidens"),
                bowler.getInt("runs"),
                bowler.getInt("wickets"),
                bowler.getDouble("economy"),
                bowler.getInt("no_balls"),
                bowler.getInt("wides")

            )
            list.add(bowlingStats)
        }
        return list
    }

    private fun toggleVisibilityWithAnimation(vararg views: View) {
        views.forEach { view ->
            view.visibility = if (view.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }
}
