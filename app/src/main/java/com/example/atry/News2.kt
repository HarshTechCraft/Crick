package com.example.atry

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class News2 : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news2)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val id = intent.getStringExtra("newsId")
        val byteArray = intent.getByteArrayExtra("imgByteArray")
        val image = findViewById<ImageView>(R.id.image)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        image.visibility = View.GONE
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            image.setImageBitmap(bitmap)
        }

        val apiKey = getString(R.string.apiKey)
        val rec = findViewById<RecyclerView>(R.id.rec)
        rec.isNestedScrollingEnabled = false
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val url = "https://cricbuzz-cricket.p.rapidapi.com/news/v1/detail/$id"
        val listener = Response.Listener<JSONObject> { response -> handleResponse(response, rec)
            progressBar.visibility = View.GONE
            image.visibility = View.VISIBLE

            }
        val errorListener = Response.ErrorListener { error -> handleError(error) }

        val jsonObjectRequest = object : JsonObjectRequest(url, listener, errorListener) {
            override fun getHeaders(): Map<String, String> {
                return mapOf(
                    "x-rapidapi-key" to apiKey,
                    "x-rapidapi-host" to "cricbuzz-cricket.p.rapidapi.com"
                )
            }
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(ContextCompat.getDrawable(this@News2, R.drawable.ic_custom_back))
            setDisplayShowTitleEnabled(false)
        }

        toolbarTitle.text = "CrickLive"
        val volley = Volley.newRequestQueue(this)
        volley.add(jsonObjectRequest)
    }

    private fun handleResponse(response: JSONObject, rec: RecyclerView) {
        try {
            val list = ArrayList<String>()
            val formatMap = LinkedHashMap<String, String>()
            if (response.has("format")) {
                val formatArray = response.getJSONArray("format")
                for (i in 0 until formatArray.length()) {
                    val formatObject = formatArray.getJSONObject(i)
                    val valueArray = formatObject.getJSONArray("value")
                    for (j in 0 until valueArray.length()) {
                        val valueObject = valueArray.getJSONObject(j)
                        if (valueObject.has("id") && valueObject.has("value")) {
                            val id = valueObject.getString("id")
                            val value = valueObject.getString("value")
                            formatMap[id] = value
                        } else {
                            Log.d("qwert", "Skipping valueObject without 'id' or 'value': $valueObject")
                        }
                    }
                }
            }
            val headline = response.getString("headline")
            val contents = response.getJSONArray("content")
            val header = findViewById<TextView>(R.id.header)
            header.text = headline
            for (i in 0 until contents.length()) {
                val content = contents.getJSONObject(i)
                if (content.has("content")) {
                    val contentObject = content.getJSONObject("content")
                    if (contentObject.has("contentValue")) {
                        val contentValue = contentObject.getString("contentValue")
                        val formattedContent = replaceTags(contentValue, formatMap)
                        list.add(formattedContent)
                    } else {
                        Log.d("qwert", "Skipping contentObject without 'contentValue': $contentObject")
                    }
                }
            }
            val adapter = AdapterNewsActivity(this, list)
            rec.layoutManager = LinearLayoutManager(this)
            rec.adapter = adapter
        } catch (e: JSONException) {
            Log.d("qwert", "JSON parsing error: ${e.message}")
            Toast.makeText(this, "JSON parsing error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    private fun handleError(error: VolleyError) {
        Log.d("qwert", "Error: ${error.message}")
        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
    }

    private fun replaceTags(content: String, formatMap: Map<String, String>): String {
        var formattedContent = content
        for ((tag, value) in formatMap) {
            formattedContent = formattedContent.replace(tag, value)
        }
        return formattedContent
    }
}
