package ru.netology.nmedia.viewModel

import SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impr.InMemoryPostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    private val currentPost = SingleLiveEvent<Post?>()

    val shareEvent = SingleLiveEvent<String>()

    val navigateToPostContentScreenEvent = SingleLiveEvent<String?>()

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            postText = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            postText = content,
            postData = "Сегодня",
            postName = "Andrey"
        )
        repository.savePost(post)
        currentPost.value = null
    }

    fun addPostClicked() {
        navigateToPostContentScreenEvent.call()
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        shareEvent.value = post.postText
    }

    override fun onDeleteClicked(post: Post) =
        repository.deletePost(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = post.postText
    }

    // endregion PostInteractionListener

}