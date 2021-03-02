package com.konovodov.diplomkt.activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konovodov.diplomkt.databinding.NewQuoteFragmentBinding
import com.konovodov.diplomkt.dto.Quote
import com.konovodov.diplomkt.viewmodel.QuoteViewModel

class NewQuoteFragment : Fragment() {

    val viewModel: QuoteViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = NewQuoteFragmentBinding.inflate(inflater, container, false)

        with(binding) {

            arguments?.let {
                val data = it
                val id = data.getLong("id", 0)
                it.getString("author")?.let { author ->
                    authorEdit.setText(author)

                    data.getString("content")?.let { content ->
                        contentEdit.setText(content)

                        undoButton.setOnClickListener {
                            authorEdit.setText(author)
                            contentEdit.setText(content)
                        }

                        cancelButton.setOnClickListener {
                            findNavController().popBackStack()
                        }
                        saveButton.setOnClickListener {
                            viewModel.saveQuote(Quote(id = id, author = authorEdit.text.toString(), content = contentEdit.text.toString()))
                            findNavController().popBackStack()
                        }
                    }
                }

            }
        }

        return binding.root
    }
}