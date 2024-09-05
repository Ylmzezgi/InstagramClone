package com.ezgiyilmaz.kotlininstagram.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezgiyilmaz.kotlininstagram.R
import com.ezgiyilmaz.kotlininstagram.adapter.FeedRecyclerAdapter
import com.ezgiyilmaz.kotlininstagram.databinding.ActivityFeedBinding
import com.ezgiyilmaz.kotlininstagram.model.Post
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var postArrayList: ArrayList<Post>
    var feedAdapter: FeedRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        postArrayList = ArrayList<Post>()

        feedAdapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = feedAdapter

        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.insta_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.add_post){
            val intent=Intent(this@FeedActivity, UploadActivity::class.java)
            startActivity(intent)
        }else if(item.itemId== R.id.signout){
            auth.signOut()
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getData() {
        db.collection("Posts").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(this, "Error fetching data: ${error.localizedMessage}", Toast.LENGTH_LONG).show()
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        val documents = value.documents

                        postArrayList.clear() // Listeyi temizle

                        for (document in documents) {

                            val name = document.getString("name") ?: ""
                            val useremail = document.getString("userEmail") ?: ""
                            val downloadUrl = document.getString("downloadUrl") ?: ""

                            val post = Post(useremail, name, downloadUrl)
                            postArrayList.add(post)
                        }

                        feedAdapter?.notifyDataSetChanged() // Yeni gelen verileri düzene sok ve göster
                    }}
            }
        }
    }
}