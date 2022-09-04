package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

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

        setFragmentResultListener(
            requestKey = NewPostFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != NewPostFragment.REQUEST_KEY) return@setFragmentResultListener

            val newPostContent = bundle.getString(
                NewPostFragment.POST_CONTENT_EXTRA_KEY
            ) ?: return@setFragmentResultListener
            val newPostVideo = bundle.getString(
                NewPostFragment.POST_VIDEO_EXTRA_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent, newPostVideo)
        }
        viewModel.navigateToPostContentScreenEvent.observe(this) { initial ->
            val direction = FeedFragmentDirections.actionFeedFragmentToNewPostFragment(
                initial?.newContent,
                initial?.newVideo
            )
            findNavController().navigate(direction)
        }
        viewModel.navigateToPostFragmentEvent.observe(this) { postId ->
            val direction = FeedFragmentDirections.actionFeedFragmentToPostFragment(postId.toInt())
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
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