package de.xembalo.doorcontrol

import android.content.Context
import androidx.preference.PreferenceManager
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object WSUtils {
    suspend fun callWS(gate: String, direction: String, context: Context, path: String): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = sharedPreferences.getString("token", "")
        val serverURL = sharedPreferences.getString("url", "")

        val url = URL(serverURL + path)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connectTimeout = 300000
        connection.doOutput = true

        val message: String = "{\"token\": \"" + token + "\",\"direction\":\"" + direction + "\",\"gate\":\"" + gate + "\"}"
        val postData: ByteArray = message.toByteArray(StandardCharsets.UTF_8)

        connection.setRequestProperty("charset", "utf-8")
        connection.setRequestProperty("Content-length", postData.size.toString())
        connection.setRequestProperty("Content-Type", "application/json")

        try {
            val outputStream: DataOutputStream = DataOutputStream(connection.outputStream)
            outputStream.write(postData)
            outputStream.flush()
        } catch (exception: Exception) {

        }

        return if (connection.responseCode != 200) {
            connection.responseMessage
        } else {
            "".toString()
        }
    }
}

class Constants {
    companion object {
        const val UP = "up"
        const val DOWN = "down"
        const val IMPULSE = "impulse"
        const val CLIMATE = "climate"
        const val HALF = "half"
        const val OPEN = "open"
        const val CLOSE = "close"
        const val GARAGE = "garage"
        const val FENCE = "fence"
        const val PATH_MOVE = "/move"
    }
}

class DoorState {
    private var state: Int = 0

    fun getState(): Int {
        return state
    }

    fun setState(newState: Int) {
        state = newState
    }

    fun switch(): Int {
        state = if (state == 0) 1 else 0
        return state
    }
}
