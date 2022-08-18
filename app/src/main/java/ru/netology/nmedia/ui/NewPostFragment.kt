package ru.netology.nmedia.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.util.focusAndShowKeyboard

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ActivityNewPostBinding.inflate(layoutInflater, container, false).also { binding ->

        val intent = Intent()
        val postText = intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
        val url = intent.getStringExtra(POST_VIDEO_EXTRA_KEY)
        binding.contentEditText.setText(postText)
        binding.contentEditText.focusAndShowKeyboard()
        binding.videoUrl.setText(url)

        binding.ok.setOnClickListener {
            if (!binding.contentEditText.text.isNullOrBlank()) {
                val resultBundle = Bundle(1)
                resultBundle.putString(POST_CONTENT_EXTRA_KEY, binding.contentEditText.text.toString())
                val content = binding.contentEditText.text.toString()
                val videoURL = binding.videoUrl.text.toString()
                intent.putExtra(POST_CONTENT_EXTRA_KEY, content)
                intent.putExtra(POST_VIDEO_EXTRA_KEY, videoURL)
                setResult(Activity.RESULT_OK, intent)
            }
            parentFragmentManager.popBackStack()
        }
    }.root


    companion object {
        const val REQUEST_KEY = "REQUEST"
        private const val POST_CONTENT_EXTRA_KEY = "POST_CONTENT_EXTRA_KEY"
        private const val POST_VIDEO_EXTRA_KEY = "POST_VIDEO_EXTRA_KEY"

    }

    class PostResult(
        val newContent: String?,
        val newVideo: String?
    )

    object ResultContract : ActivityResultContract<PostResult?, PostResult?>() {

        override fun createIntent(context: Context, input: PostResult?): Intent {
            val intent = Intent(context, NewPostFragment::class.java)
            intent.putExtra(POST_CONTENT_EXTRA_KEY, input?.newContent)
            intent.putExtra(POST_VIDEO_EXTRA_KEY, input?.newVideo)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): PostResult? =
            if (resultCode == Activity.RESULT_OK) {
                PostResult(
                newContent = intent?.getStringExtra(POST_CONTENT_EXTRA_KEY),
                newVideo = intent?.getStringExtra(POST_VIDEO_EXTRA_KEY)
                )
            } else null
    }
}