package ru.netology.nmedia.data.impr

import androidx.lifecycle.map
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun savePost(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) dao.insert(post.toEntity())
        else dao.updateContentById(post.id, post.content)
    }

    override fun like(postId: Long) = dao.likedById(postId)

    override fun share(postId: Long) = dao.share(postId)

    override fun deletePost(postId: Long) = dao.removeById(postId)
}