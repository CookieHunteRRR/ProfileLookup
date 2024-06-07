package com.cookiehunterrr.profilelookup.ui.search

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cookiehunterrr.profilelookup.MainActivity
import com.cookiehunterrr.profilelookup.R
import com.cookiehunterrr.profilelookup.database.User
import org.json.JSONObject


class SearchViewModel : ViewModel() {
    private var _inputText = MutableLiveData<String>()
    val inputText : LiveData<String> = _inputText

    fun updateTextInInput(text: String)
    {
        _inputText.value = text
    }

    fun tryFindUser(activity: MainActivity) {
        // Отправляем запрос в апи
        getApiResponse(activity, inputText.value!!) {
            val requestResult = it.getBoolean("success")
            // Если получаем ответ "не существует" - выдаем тост с ошибкой
            if (!requestResult)
            {
                Toast.makeText(activity, "Player not found", Toast.LENGTH_SHORT).show()
                return@getApiResponse
            }
            val playerInfo = it.getJSONObject("data").getJSONObject("player")
            val meta = playerInfo.getJSONObject("meta")
            // Собираем класс User из полученного жсона
            val user = User(
                id = playerInfo.getString("id"),
                name = playerInfo.getString("username"),
                realName = meta.getString("realname"),
                locCountryCode = meta.getString("loccountrycode"),
                profileUrl = meta.getString("profileurl"),
                avatarLink = playerInfo.getString("avatar"),
                avatarHash = meta.getString("avatarhash")
            )
            // Отправляем класс User в addUser и addAvatar в бд
            activity.DB.addUser(user)
            activity.DB.addUserAvatarData(user)
            // Класс бд обрабатывает эту информацию
            // (если чего то не существует - добавляет, если что-то существует но устарело - обновляет, если все уже есть - ниче не делает)
            // Наконец - переходим на новый фрагмент с выводом всех данных
            updateDisplayedUser(activity, user)
            activity.findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_home)
        }
    }

    fun getApiResponse(context: Context, userId: String, callback: (result: JSONObject) -> Unit) {
        val queue = Volley.newRequestQueue(context)
        val url = "https://playerdb.co/api/player/steam/$userId"

        val jsonObjectRequest = JsonObjectRequest(url, { response ->
            callback(response)
        }, { errorListener ->
            Toast.makeText(context, errorListener.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add(jsonObjectRequest)
    }

    fun updateDisplayedUser(activity: MainActivity, user: User) {
        activity.currentUser = user
    }
}