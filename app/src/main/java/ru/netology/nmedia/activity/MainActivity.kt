package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityEditPostBinding
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.util.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val adapter = PostAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) {posts ->
            adapter.submitList(posts)
        }
        binding.addButton.setOnClickListener {
            viewModel.addPostClicked()
        }

        viewModel.shareEvent.observe(this) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post)
            }
            val shareIntent = Intent.createChooser(intent, "Поделиться: ")
            startActivity(shareIntent)
        }

        val activityLauncher = registerForActivityResult(
            NewPostActivity.ResultContract
        ) { postText: String? ->
            postText?.let(viewModel::onSaveButtonClicked)
        }
        viewModel.navigateToPostContentScreenEvent.observe(this) {
            activityLauncher.launch()
        }
    }
}