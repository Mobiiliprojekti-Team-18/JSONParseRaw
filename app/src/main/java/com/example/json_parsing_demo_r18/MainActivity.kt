package com.example.json_parsing_demo_r18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.json_parsing_demo_r18.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var jsonString: String? = ""
    private var jsonArray: JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buGetJson.setOnClickListener {
            getJson()
        }

        binding.buChangePerson.setOnClickListener {

        }
    }

    private fun getJson() {
        val thread = Thread {
            try {
                val url = URL("https://jsonplaceholder.typicode.com/users")
                val connection = url.openConnection() as HttpURLConnection
                BufferedReader(InputStreamReader(connection.inputStream)).use { inp ->
                    var line: String?
                    while (inp.readLine().also { line = it } != null) {
                        jsonString += line
                    }
                }
                buildJsonArray()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()
    }

    private fun buildJsonArray() {
        try {
            println(jsonString)
            jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
            for(i in 0 until jsonArray!!.length()) {
                val name = jsonArray!!.getJSONObject(i).getString("name")
                println(name)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

