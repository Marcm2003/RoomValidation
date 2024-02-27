package com.mp08.a07roomValidation

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mp08.a07roomValidation.adapters.FriendAdapter
import com.mp08.a07roomValidation.room.Friend
import com.mp08.a07roomValidation.room.FriendDao
import kotlinx.coroutines.*
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var friendDao: FriendDao
    private lateinit var parentLayout: View

    private val FRIEND_DETAIL_REQUEST_CODE = 1


    lateinit var myRecyclerView : RecyclerView
    val mAdapter : FriendAdapter = FriendAdapter()

    var lstNames: List<String> = ArrayList()
    var lstLastNames: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        friendDao = (applicationContext as App).db.friendDao()
        parentLayout = findViewById<View>(android.R.id.content)

        setContentView(R.layout.activity_main)

        setUpRecyclerView()
        setUpLists()
        showFriends()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FRIEND_DETAIL_REQUEST_CODE) {
            showFriends()
        }
    }

    private fun setUpLists() {
        lstNames = arrayListOf(
            "María",
            "Juan",
            "Ana",
            "Pedro",
            "Lucía",
            "Carlos",
            "Laura",
            "José",
            "Isabel",
            "Miguel",
            "Carmen",
            "Francisco",
            "Elena",
            "Javier",
            "Sofía",
            "Antonio",
            "Rosa",
            "Pablo",
            "Beatriz",
            "David")

        lstLastNames = arrayListOf(
            "García",
            "Fernández",
            "González",
            "López",
            "Martínez",
            "Pérez",
            "Rodríguez",
            "Sánchez",
            "Alonso",
            "Álvarez",
            "Blanco",
            "Castro",
            "Díaz",
            "Gómez",
            "Hernández",
            "Jiménez",
            "Molina",
            "Moreno",
            "Muñoz",
            "Ortega"
        )



    }

    fun setUpRecyclerView(){
        myRecyclerView = findViewById(R.id.rvDatos) as RecyclerView
        myRecyclerView.setHasFixedSize(true)
        myRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    // Menu ToolBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }



    fun setLocale(languageCode: String, context: Context) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


    // Opciones del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miAddFriend -> {
                addFriend()
                true
            }

            R.id.deleteFriend -> {
                showDeleteFriendsDialog()
                true
            }

            R.id.miChangeLanguage -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.change_language))

                val languages = arrayOf("English", "Spanish")
                val checkedItem = 0
                builder.setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                    when (which) {
                        0 -> {
                            setLocale("en", this) // Pasando 'this' como contexto
                            recreate()
                        }
                        1 -> {
                            setLocale("es", this) // Pasando 'this' como contexto
                            recreate()
                        }
                    }
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteFriendsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.delete_friends_by_age))

        val view = layoutInflater.inflate(R.layout.dialog_delete_friends, null)
        val etAge = view.findViewById<EditText>(R.id.etAge)
        builder.setView(view)

        builder.setPositiveButton(getString(R.string.delete), DialogInterface.OnClickListener { dialog, which ->
            val age = etAge.text.toString().toInt()
            deleteFriendsByAge(age)
        })

        builder.setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->  })

        builder.show()
    }

    private fun updateActionBarTitleWithSumOfAges() {
        CoroutineScope(Dispatchers.IO).launch {
            val sumOfAges = friendDao.getSumOfAges()
            withContext(Dispatchers.Main) {
                supportActionBar?.title = getString(R.string.sum_of_ages)+"$sumOfAges"
            }
        }
    }

    private fun deleteFriendsByAge(age: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            friendDao.deleteByAge(age)
            withContext(Dispatchers.Main) {
                showFriends()
            }
        }
    }

    private fun addFriend() {
        runBlocking {
            val randomName = Random.nextInt(20)
            val randomLastName = Random.nextInt(20)
            val randomAge = Random.nextInt(15, 80)
            val randomPhone = Random.nextLong(600000000, 699999999)


            // Construimos el objeto
            var friend: Friend = Friend(0,
                lstNames[randomName],
                lstLastNames[randomLastName],
                randomPhone.toString(),
                lstNames[randomName].toLowerCase() + "." + lstLastNames[randomLastName].toLowerCase() + "@gmail.com",
                randomAge
            )

            // Insertamos el objeto
            friend.id = async {
                friendDao.insert(friend)
            }.await()

            Snackbar.make(parentLayout,"Friend add" + friend.id.toString() ,Snackbar.LENGTH_SHORT)
                .show()

            showFriends()
        }
    }

    private fun showFriends() {

        runBlocking {
            var friends: MutableList<Friend> = async {
                friendDao.getFriends()
            }.await()

            mAdapter.friendAdapter(friends, this@MainActivity)
            myRecyclerView.adapter = mAdapter

            updateActionBarTitleWithSumOfAges()
        }
    }

}


