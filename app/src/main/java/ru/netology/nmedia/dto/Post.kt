package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    var shares: Int = 0,
    val video: String? = null
)

class EditPostResult(
    val newContent: String?,
    val newVideo: String?
)