package com.example.json_parsing_demo_r18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.json_parsing_demo_r18.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import android.util.Log.VERBOSE

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var jsonString: String? = ""
    private var jsonArray: JSONArray? = null
    private var person: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buGetJson.setOnClickListener {
            getJson()
        }

        binding.buChangePerson.setOnClickListener {
            changePerson()
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
            jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPerson() {
        // Get the nested json object
        val jsonObjectAddress = jsonArray!!.getJSONObject(person).getJSONObject("address")

        binding.tvUserName.text = jsonArray!!.getJSONObject(person).getString("name")
        binding.tvUserEmail.text = jsonArray!!.getJSONObject(person).getString("email")
        binding.tvUserPhone.text = jsonArray!!.getJSONObject(person).getString("phone")
        binding.tvUserCompany.text = jsonArray!!.getJSONObject(person).getJSONObject("company").getString("name")
        binding.tvUserAddress.text =  jsonObjectAddress!!.getString("city") +
                jsonObjectAddress!!.getString("street") +
                jsonObjectAddress!!.getString("suite")
    }

    private fun changePerson() {
        try {
            if(jsonArray != null) {
                if(person < jsonArray!!.length()-1) {
                    person++
                } else {
                    person = 0
                }
                getPerson()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

