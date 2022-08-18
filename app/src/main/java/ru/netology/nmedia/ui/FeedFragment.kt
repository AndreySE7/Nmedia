package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.shareEvent.observe(this) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post)
            }
            val shareIntent = Intent.createChooser(intent, "Поделиться: ")
            startActivity(shareIntent)
        }

        viewModel.navigateToVideo.observe(this) { video ->
            val intent = Intent()
                .apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(video)
                }
            val videoIntent =
                Intent.createChooser(intent, "Видео: ")
            startActivity(videoIntent)
        }

        val activityLauncher = registerForActivityResult(
            NewPostFragment.ResultContract
        ) { PostResult ->
            PostResult?.newContent ?: return@registerForActivityResult
            viewModel.onSaveButtonClicked(PostResult.newContent, PostResult.newVideo)
        }
        viewModel.navigateToPostContentScreenEvent.observe(this) {
            activityLauncher.launch(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ActivityMainBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = PostAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        binding.addButton.setOnClickListener {
            viewModel.addPostClicked()
        }

    }.root
}
