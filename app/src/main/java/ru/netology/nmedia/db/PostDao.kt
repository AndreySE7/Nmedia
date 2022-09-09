package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun share(id: Long)
    fun likedById(id: Long)
    fun removeById(id: Long)
}