package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.util.focusAndShowKeyboard

class NewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentReceived = intent ?: return
        val text = intentReceived.getStringExtra(POST_CONTENT_EXTRA_KEY)
        binding.contentEditText.setText(text)
        binding.contentEditText.focusAndShowKeyboard()

        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.contentEditText.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.contentEditText.text.toString()
                intent.putExtra(POST_CONTENT_EXTRA_KEY, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    companion object {
        private const val POST_CONTENT_EXTRA_KEY = "postContent"
    }

    object ResultContract : ActivityResultContract<String?, String?>() {

        override fun createIntent(context: Context, input: String?): Intent {
            val intent = Intent(context, NewPostActivity::class.java)
            intent.putExtra(POST_CONTENT_EXTRA_KEY, input)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(POST_CONTENT_EXTRA_KEY)
            } else null
    }
}