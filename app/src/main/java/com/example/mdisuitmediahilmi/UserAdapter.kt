package com.example.mdisuitmediahilmi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mdisuitmediahilmi.R
import com.example.mdisuitmediahilmi.models.User

class UserAdapter(
    private var users: MutableList<User>,
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    fun addUsers(newUsers: List<User>) {
        val startPosition = users.size
        users.addAll(newUsers)
        notifyItemRangeInserted(startPosition, newUsers.size)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullNameTextView: TextView = itemView.findViewById(R.id.fullusername)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailuser)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.profImg2)

        fun bind(user: User) {
            fullNameTextView.text = "${user.first_name} ${user.last_name}"
            emailTextView.text = user.email
            Glide.with(itemView.context)
                .load(user.avatar)
                .into(avatarImageView)
        }
    }
}