package com.example.mdisuitmediahilmi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mdisuitmediahilmi.adapters.UserAdapter
import com.example.mdisuitmediahilmi.api.ApiService
import com.example.mdisuitmediahilmi.api.UserResponse
import com.example.mdisuitmediahilmi.models.User
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ThirdScreen : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var emptyStateTextView: TextView

    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_third_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        recyclerView = findViewById(R.id.userRecyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        emptyStateTextView = findViewById(R.id.emptyStateTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter
        userAdapter = UserAdapter(mutableListOf()) { selectedUser ->
            returnToSecondScreen(selectedUser)
        }
        recyclerView.adapter = userAdapter

        // Set up pull-to-refresh
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        // Set up infinite scrolling
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        currentPage++
                        fetchUsers(currentPage)
                    }
                }
            }
        })

        // Fetch initial data
        fetchUsers(currentPage)

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun fetchUsers(page: Int) {
        isLoading = true

        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getUsers(page = page, perPage = 6).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                isLoading = false
                swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {
                    val newUsers = response.body()?.data ?: emptyList()

                    if (page == 1) {
                        userAdapter.updateUsers(newUsers)
                    } else {
                        userAdapter.addUsers(newUsers)
                    }

                    // Check if it's the last page
                    isLastPage = newUsers.isEmpty()

                    // Show empty state if no data
                    if (userAdapter.itemCount == 0) {
                        emptyStateTextView.visibility = View.VISIBLE
                    } else {
                        emptyStateTextView.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                isLoading = false
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@ThirdScreen, "Failed to load data", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", t.message.toString())
            }
        })
    }

    private fun refreshData() {
        currentPage = 1
        fetchUsers(currentPage)
    }

    private fun returnToSecondScreen(user: User) {
        val intent = Intent(this, SecondScreen::class.java).apply {
            putExtra("SELECTED_NAME", "${user.first_name} ${user.last_name}")
            putExtra("SELECTED_EMAIL", user.email)
            putExtra("SELECTED_AVATAR", user.avatar)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        finish()
    }
}