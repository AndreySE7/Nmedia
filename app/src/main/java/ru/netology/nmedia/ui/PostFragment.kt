package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.PostFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private val args by navArgs<PostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PostFragmentBinding.inflate(layoutInflater)

        val postId = args.postId
        val viewHolder = PostAdapter.ViewHolder(binding.postFragment, viewModel)

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId.toLong() } ?: return@observe
                viewHolder.bind(post)

            binding.postFragment.options.setOnClickListener {
                PopupMenu(it.context, it).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                viewModel.onDeleteClicked(post)
                                findNavController().navigateUp()
                                true
                            }
                            R.id.edit -> {
                                viewModel.onEditClicked(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }

        viewModel.navigateToVideo.observe(viewLifecycleOwner) { video ->
            val intent = Intent()
                .apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(video)
                }
            val videoWatchingIntent = Intent.createChooser(intent, "Смотреть")
            startActivity(videoWatchingIntent)
        }

        viewModel.shareEvent.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, "Поделиться: ")
            startActivity(shareIntent)
        }

        viewModel.navigateToPostContentScreenEvent.observe(viewLifecycleOwner) { editPostResult ->
            val direction =
                PostFragmentDirections.actionPostFragmentToNewPostFragment(
                    editPostResult?.newContent,
                    editPostResult?.newVideo
                )
            findNavController().navigate(direction)
        }
        return binding.root
    }
}