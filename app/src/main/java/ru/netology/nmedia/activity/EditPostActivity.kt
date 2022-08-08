package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.contentEditText.requestFocus()
        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.contentEditText.text
            if (binding.contentEditText.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.putExtra(POST_CONTENT_EXTRA_KEY, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    companion object {
        private const val POST_CONTENT_EXTRA_KEY = "editContent"
    }

    object ResultContract : ActivityResultContract<Unit, String?>() {

        override fun createIntent(context: Context, input: Unit) =
            Intent(context, EditPostActivity::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode != Activity.RESULT_OK) return null
            intent ?: return null

            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
        }
    }
}