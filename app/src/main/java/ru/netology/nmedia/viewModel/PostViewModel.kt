package ru.netology.nmedia.viewModel

import SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impr.PostRepositoryImpl
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.EditPostResult
import ru.netology.nmedia.dto.Post

class PostViewModel(application: Application) : AndroidViewModel(application),
    PostInteractionListener {

    private val repository: PostRepository = PostRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).postDao
    )

    val data by repository::data

    private val currentPost = MutableLiveData<Post?>(null)

    val shareEvent = SingleLiveEvent<String>()

    val navigateToPostContentScreenEvent = SingleLiveEvent<EditPostResult?>()

    val navigateToPostFragmentEvent = SingleLiveEvent<Long>()

    val navigateToVideo = SingleLiveEvent<String?>()

    fun onSaveButtonClicked(content: String, video: String?) {
        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            content = content,
            video = video
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            content = content,
            published = "Сегодня",
            author = "Andrey",
            video = video
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
        shareEvent.value = post.content
    }

    override fun onDeleteClicked(post: Post) =
        repository.deletePost(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = EditPostResult(post.content, post.video)
    }

    override fun onVideoClicked(post: Post) {
        navigateToVideo.value = post.video
    }

    override fun onPostClicked(post: Post) {
        navigateToPostFragmentEvent.value = post.id
    }
    // endregion PostInteractionListener
}