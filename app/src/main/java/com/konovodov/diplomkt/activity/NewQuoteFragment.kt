package com.konovodov.diplomkt.activity

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konovodov.diplomkt.R
import com.konovodov.diplomkt.databinding.NewQuoteFragmentBinding
import com.konovodov.diplomkt.dto.Quote
import com.konovodov.diplomkt.util.AndroidUtils
import com.konovodov.diplomkt.viewmodel.QuoteViewModel
import kotlinx.android.synthetic.main.new_quote_fragment.*
import java.io.IOException


class NewQuoteFragment : Fragment() {

    companion object {
        private const val GALLERY_REQUEST = 1
    }

    private val viewModel: QuoteViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private lateinit var quote: Quote
    private lateinit var binding: NewQuoteFragmentBinding
    private var selectedImageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        savedInstanceState?.let {
            selectedImageUri = it.getParcelable("imageUri")
        }

        binding = NewQuoteFragmentBinding.inflate(inflater, container, false)

        with(binding) {

            arguments?.let { data ->
                quote = viewModel.getById(data.getLong("id", 0))

                selectedImageUri?.let {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, it)
                    contentImage.setImageDrawable(BitmapDrawable(bitmap))
                }

                authorTextEdit.text = context?.resources?.getString(R.string.user_name)

                data.getString("content")?.let { content ->
                    contentEdit.setText(content)

                    contentImage.setOnClickListener {

                        val photoPickerIntent = Intent().apply {
                            action = Intent.ACTION_GET_CONTENT
                            type = "image/*"
                        }

                        if (activity?.packageManager?.let { it1 ->
                                photoPickerIntent.resolveActivity(it1)
                            } != null) startActivityForResult(
                            photoPickerIntent,
                            GALLERY_REQUEST
                        )
                    }

                    cancelButton.setOnClickListener {
                        AndroidUtils.hideKeyboard(requireView())
                        findNavController().popBackStack()
                    }
                    saveButton.setOnClickListener {
                        if (contentEdit.text.isNotEmpty()) {
                            quote = quote.copy(
                                author = authorTextEdit.text.toString(),
                                content = contentEdit.text.toString(),
                                link = linkEdit.text.toString()
                            )
                            viewModel.saveQuote(quote)
                            AndroidUtils.hideKeyboard(requireView())
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }

        return binding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedImageUri?.let { outState.putParcelable("imageUri", selectedImageUri) }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, returnedIntent: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST -> run {
                    val imageUri = returnedIntent?.data
                    try {
                        imageUri?.let {
                            selectedImageUri = imageUri
                            quote = if (Build.VERSION.SDK_INT < 28) {
                                val bm = MediaStore.Images.Media.getBitmap(
                                    activity?.contentResolver, imageUri
                                )
                                val drawable = BitmapDrawable(activity?.resources, bm)
                                contentImage.setImageDrawable(drawable)
                                quote.copy(imagePath = viewModel.saveImage(drawable))
                            } else {
                                val source =
                                    ImageDecoder.createSource(activity?.contentResolver!!, imageUri)
                                val drawable = ImageDecoder.decodeDrawable(source)
                                contentImage.setImageDrawable(drawable)
                                quote.copy(imagePath = viewModel.saveImage(drawable))
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    return
                }
            }
        }


    }


}