package com.konovodov.diplomkt.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konovodov.diplomkt.databinding.NewQuoteFragmentBinding
import com.konovodov.diplomkt.dto.Quote
import com.konovodov.diplomkt.viewmodel.QuoteViewModel
import kotlinx.android.synthetic.main.new_quote_fragment.*
import java.io.IOException


class NewQuoteFragment : Fragment() {

    val GALLERY_REQUEST = 1

    val viewModel: QuoteViewModel by viewModels(ownerProducer = ::requireParentFragment)
    lateinit var quote: Quote
    lateinit var binding : NewQuoteFragmentBinding
    var selectedImageUri: Uri? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        savedInstanceState?.let {
            it.getString("imageUri")?.let { it1 ->
            selectedImageUri = Uri.parse(it1)
            }
        }

        binding = NewQuoteFragmentBinding.inflate(inflater, container, false)

        with(binding) {

            arguments?.let {
                val data = it
                quote = viewModel.getById(data.getLong("id", 0))

                selectedImageUri?.let {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, it)
                    contentImage.setImageDrawable(BitmapDrawable(bitmap)) }

                it.getString("author")?.let { author ->
                    authorEdit.setText(author)

                    data.getString("content")?.let { content ->
                        contentEdit.setText(content)

                        contentImage.setOnClickListener {

                            val photoPickerIntent = Intent().apply {
                                action = Intent.ACTION_GET_CONTENT
                                type = "image/*"
                            }

                            if (activity?.packageManager?.let { it1 ->
                                        photoPickerIntent.resolveActivity(it1)
                                    } != null) startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
                        }

                        cancelButton.setOnClickListener {
                            findNavController().popBackStack()
                        }
                        saveButton.setOnClickListener {
                            quote = Quote(quote.id, author = authorEdit.text.toString(), content = contentEdit.text.toString())
                            viewModel.saveQuote(quote)
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
        selectedImageUri?.let { outState.putString("imageUri", selectedImageUri.toString()) }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, returnedIntent: Intent?) {
        var bitmap: Bitmap
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST -> run {
                    selectedImageUri = returnedIntent?.data
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImageUri)
                        contentImage.setImageDrawable(BitmapDrawable(bitmap))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    return
                }
            }
        }


    }


}