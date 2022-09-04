package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.NewPostFragmentBinding
import ru.netology.nmedia.util.focusAndShowKeyboard

class NewPostFragment : Fragment() {

    private val args by navArgs<NewPostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = NewPostFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->

        binding.contentEditText.setText(args.initialContent)
        binding.contentEditText.focusAndShowKeyboard()
        binding.video.setText(args.initialVideo)

        binding.ok.setOnClickListener {
            val text = binding.contentEditText.text
            val video = binding.video.text
            if (!text.isNullOrBlank()) {
                val resultBundle = Bundle(2)
                resultBundle.putString(POST_CONTENT_EXTRA_KEY, text.toString())
                resultBundle.putString(POST_VIDEO_EXTRA_KEY, video.toString())
                setFragmentResult(REQUEST_KEY, resultBundle)
            }
            findNavController().popBackStack()
        }
    }.root

    companion object {

        const val REQUEST_KEY = "REQUEST"
        const val POST_CONTENT_EXTRA_KEY = "POST_CONTENT_EXTRA_KEY"
        const val POST_VIDEO_EXTRA_KEY = "POST_VIDEO_EXTRA_KEY"
    }
}