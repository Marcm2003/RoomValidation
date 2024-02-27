package com.mp08.a07roomValidation

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.mp08.a07roomValidation.room.Friend
import com.mp08.a07roomValidation.room.FriendDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendDetailActivity : AppCompatActivity() {

    private lateinit var friendDao: FriendDao
    private lateinit var friend: Friend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_detail)

        friendDao = (applicationContext as App).db.friendDao()
        friend = intent.getSerializableExtra("friend") as? Friend ?: return

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnUp = findViewById<ImageButton>(R.id.btnUp)

        btnUp.setOnClickListener {
            increaseAge()
        }

        btnSave.setOnClickListener {
            saveFriendData()
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }

        loadFriendData()
    }

    private fun increaseAge() {
        val actualAge = findViewById<EditText>(R.id.textFriendAge).text.toString()
        val age = actualAge.toIntOrNull()

        if (age != null && age in 15..80) {
            val newAge = age + 1
            findViewById<EditText>(R.id.textFriendAge).setText(newAge.toString())
        }
        saveFriendData()
    }

    private fun loadFriendData() {
        val textFriendName = findViewById<EditText>(R.id.textFriendName)
        val textFriendLastName = findViewById<EditText>(R.id.textFriendLastName)
        val textFriendAge = findViewById<EditText>(R.id.textFriendAge)
        val textFriendPhone = findViewById<EditText>(R.id.textFriendPhone)

        textFriendName.setText(friend.name)
        textFriendLastName.setText(friend.lastName)
        textFriendAge.setText(friend.age.toString())
        textFriendPhone.setText(friend.phone)
    }

    private fun saveFriendData() {
        val textFriendName = findViewById<EditText>(R.id.textFriendName)
        val textFriendLastName = findViewById<EditText>(R.id.textFriendLastName)
        val textFriendAge = findViewById<EditText>(R.id.textFriendAge)
        val textFriendPhone = findViewById<EditText>(R.id.textFriendPhone)

        val name = textFriendName.text.toString()
        val lastName = textFriendLastName.text.toString()
        val ageText = textFriendAge.text.toString()
        val phone = textFriendPhone.text.toString()

        val age = ageText.toIntOrNull()

        if (name.isNotBlank() && lastName.isNotBlank() && age != null && age in 15..80 && phone.length == 9) {
            CoroutineScope(Dispatchers.IO).launch {
                friend.name = name
                friend.lastName = lastName
                friend.age = age
                friend.phone = phone
                friend.email = friend.email
                friendDao.updateFriend(friend)
            }
        } else {
            // Mostrar un mensaje de error o notificar al usuario que algún campo está vacío
            Log.d("FriendDetailActivity", "Algún campo está vacío o la edad o teléfono no es válido")
        }
    }

}
