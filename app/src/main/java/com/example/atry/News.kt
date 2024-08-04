package com.example.atry

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class News : Fragment() {
    lateinit var storyAdapter: Adapter_news
    lateinit var progressBar: ProgressBar
    private val storyListData = ArrayList<Data_news>()
    private var volley: RequestQueue? = null
    private var pendingRequests = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.news, container, false)
        volley = Volley.newRequestQueue(requireContext())

        val url = "https://cricbuzz-cricket.p.rapidapi.com/news/v1/index"
        val rec = view.findViewById<RecyclerView>(R.id.rec)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val apiKey = requireContext().getString(R.string.apiKey)

        rec.layoutManager = LinearLayoutManager(requireContext())
        storyAdapter = Adapter_news(requireContext(), storyListData)
        rec.adapter = storyAdapter

        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
            { response -> handleResponse(response) },
            { error -> handleError(error) }) {
            override fun getHeaders(): Map<String, String> {
                return mapOf(
                    "x-rapidapi-key" to apiKey,
                    "x-rapidapi-host" to "cricbuzz-cricket.p.rapidapi.com"
                )
            }
        }

        volley?.add(jsonObjectRequest)
        return view
    }

    private fun handleResponse(response: JSONObject) {
        try {
            val storyList = response.getJSONArray("storyList")
            for (i in 0 until storyList.length()) {
                val storyObject = storyList.getJSONObject(i)
                if (storyObject.has("story")) {
                    val story = storyObject.getJSONObject("story")
                    val hline = story.getString("hline")
                    val newsId = story.getInt("id")
                    val intro = story.getString("intro")
                    val imageId = story.getString("imageId")
                    val newStory = Data_news(newsId, hline, intro, imageId, null)
                    storyListData.add(newStory)
                    pendingRequests++
                    fetchImage(newStory, i)
                }
            }
        } catch (e: JSONException) {
            Log.d("qwert", "JSON parsing error: ${e.message}")
            Toast.makeText(requireContext(), "JSON parsing error: ${e.message}", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
        }
    }

    private fun handleError(error: VolleyError) {
        Log.d("qwert", "Error: ${error.message}")
        Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
        progressBar.visibility = View.GONE
    }

    private fun fetchImage(story: Data_news, position: Int) {
        val apiKey2 = requireContext().getString(R.string.apiKey2)
        val imageUrl = "https://cricbuzz-cricket.p.rapidapi.com/img/v1/i1/c${story.imageId}/i.jpg?p=det&d=high"
        val imageRequest = createImageRequest(story, position, imageUrl, apiKey2)
        volley?.add(imageRequest)
    }

    private fun createImageRequest(story: Data_news, position: Int, imageUrl: String, apiKey: String): ImageRequest {
        return object : ImageRequest(imageUrl,
            { response -> handleImageResponse(story, position, response) },
            0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
            { error -> handleImageError(story, position, imageUrl, apiKey, error) }) {
            override fun getHeaders(): Map<String, String> {
                return mapOf(
                    "x-rapidapi-key" to apiKey,
                    "x-rapidapi-host" to "cricbuzz-cricket.p.rapidapi.com"
                )
            }
        }
    }

    private fun handleImageResponse(story: Data_news, position: Int, response: Bitmap) {
        story.image = response
        pendingRequests--
        checkIfAllRequestsCompleted()
    }

    private fun handleImageError(story: Data_news, position: Int, imageUrl: String, apiKey: String, error: VolleyError) {
        val networkResponse = error.networkResponse
        if (networkResponse == null || networkResponse.statusCode != 429) {
            Log.d("qwert", "Volley error: ${error.message}")
            Toast.makeText(requireContext(), "Volley error: ${error.message}", Toast.LENGTH_LONG).show()
            pendingRequests--
            checkIfAllRequestsCompleted()
        } else {
            Log.d("qwert", "429 Too Many Requests: Retrying after a delay")
            Thread.sleep(1000)
            val imageRequest = createImageRequest(story, position, imageUrl, apiKey)
            volley?.add(imageRequest)
        }
    }

    private fun checkIfAllRequestsCompleted() {
        if (pendingRequests == 0) {
            storyAdapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        }
    }
}
