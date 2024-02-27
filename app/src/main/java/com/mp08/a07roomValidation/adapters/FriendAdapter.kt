package com.mp08.a07roomValidation.adapters

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mp08.a07roomValidation.R
import com.mp08.a07roomValidation.room.Friend
import com.mp08.a07roomValidation.FriendDetailActivity
import com.google.android.material.snackbar.Snackbar

class FriendAdapter : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {
    var friends: MutableList<Friend> = ArrayList()
    lateinit var context: Context

    fun friendAdapter(friends: MutableList<Friend>, context: Context) {
        this.friends = friends
        this.context = context
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = friends[position]
        holder.bind(item, this.context)

        //holder.itemView.setOnClickListener {
        //    val intent = Intent(context, FriendDetailActivity::class.java)
        //    intent.putExtra("friendId", item.id)
        //    context.startActivity(intent)
        //    Log.d("FriendAdapter", "Putting friendId into intent: ${item.id}")
        //}

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FriendDetailActivity::class.java)
            intent.putExtra("friend", item)
            (context as Activity).startActivityForResult(intent, FRIEND_DETAIL_REQUEST_CODE)
        }

        holder.phone.setOnClickListener {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                openDialer(item.phone)
            } else {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE_PERMISSION)
            }
        }
    }

    private fun openDialer(phone: String?) {
        val prefix = "+34"
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$prefix$phone")
        context.startActivity(intent)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.friend_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre = view.findViewById<TextView>(R.id.tvNane)
        val apellidos = view.findViewById<TextView>(R.id.tvLastName)
        val id = view.findViewById<TextView>(R.id.tvId)
        val age = view.findViewById<TextView>(R.id.tvAge)
        val phone = view.findViewById<TextView>(R.id.tvPhone)

        fun bind(friend: Friend, context: Context) {
            id.text = friend.id.toString()
            nombre.text = friend.name
            apellidos.text = friend.lastName
            age.text = friend.age.toString()
            phone.text = friend.phone
        }
    }

    companion object {
        const val REQUEST_CALL_PHONE_PERMISSION = 123
        const val FRIEND_DETAIL_REQUEST_CODE = 1
    }
}
