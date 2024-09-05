package com.ezgiyilmaz.kotlininstagram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezgiyilmaz.kotlininstagram.databinding.RecyclerRowBinding
import com.ezgiyilmaz.kotlininstagram.model.Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(val postArrayList: ArrayList<Post>) :RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {

    class PostHolder (val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context)) // oluşturmuş olduğumuz RecyclerRowBinding ile Adapterı bağlıyoruz
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postArrayList.size
    }


    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerEmailText.text=postArrayList.get(position).email
        holder.binding.recyclerCommentText.text=postArrayList.get(position).name
            Picasso.get().load(postArrayList.get(position).downloadUrl).into(holder.binding.recyclerViImageId)
    }
}