package ru.netology.nmedia.data.impr

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.dto.Post

class SQLiteRepository(
    private val dao: PostDao
) : PostRepository {

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override val data = MutableLiveData(dao.getAll())

    override fun savePost(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        data.value = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
    }

    override fun like(id: Long) {
        dao.likedById(id)
        data.value = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
    }

    override fun share(id: Long) {
        dao.share(id)
        data.value = posts.map {
            if (it.id == id) it.copy(
                shares = it.shares + 1
            )
            else it
        }
    }

    override fun deletePost(id: Long) {
        dao.removeById(id)
        data.value = posts.filter { it.id != id }
    }
}