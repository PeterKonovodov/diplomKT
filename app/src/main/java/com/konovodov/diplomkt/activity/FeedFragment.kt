package com.konovodov.diplomkt.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.konovodov.diplomkt.R
import com.konovodov.diplomkt.adapter.QuoteAdapter
import com.konovodov.diplomkt.databinding.FeedFragmentBinding
import com.konovodov.diplomkt.viewmodel.QuoteViewModel

class FeedFragment : Fragment() {

    private val viewModel: QuoteViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private var backPressedCount: Int = 0
    private var timeout: Long = System.currentTimeMillis()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FeedFragmentBinding.inflate(inflater, container, false)

        val adapter = QuoteAdapter(
            onLikeListener = { viewModel.likeById(it.id) },
            onDislikeListener = { viewModel.dislikeById(it.id) },
            onShareListener = { viewModel.shareQuote(it) },
            onAuthorListener = {
                activity?.findNavController(R.id.nav_host_fragment)?.navigate(
                    R.id.action_feedFragment_to_feedFragmentByAuthor,
                    Bundle().apply {
                        putString("author", it.author)
                    }
                )
            },
            onDeleteListener = {
                viewModel.deleteById(it.id)
            }

        )

        binding.newsFeed.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { quotesList -> adapter.submitList(quotesList) })

        binding.fab.setOnClickListener {
            activity?.findNavController(R.id.nav_host_fragment)?.navigate(
                R.id.action_feedFragment_to_newQuoteFragment,
                Bundle().apply {
                    putLong("id", 0)
                    putString("author", "")
                    putString("content", "")
                }
            )
        }


        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    timeout = System.currentTimeMillis() - timeout
                    if (timeout > 3000) backPressedCount = 0
                    backPressedCount++
                    if (backPressedCount == 1) {
                        Toast.makeText(
                            activity, getString(R.string.backpress_more),
                            Toast.LENGTH_SHORT
                        ).show()
                        timeout = System.currentTimeMillis() //засекаем отсчет времени после первого
                        // нажатия
                    }
                    if (backPressedCount == 2) {
                        activity?.finish()
                    }
                }
            })


        return binding.root
    }


/*

    override fun onBackPressed() {
        timeout = System.currentTimeMillis() - timeout
        if (timeout > 3000) backPressedCount = 0
        backPressedCount++
        if (backPressedCount == 1) {
            Toast.makeText(
                this@NotesMainActivity, getString(R.string.backpress_more),
                Toast.LENGTH_SHORT
            ).show()
            timeout = System.currentTimeMillis() //засекаем отсчет времени после первого
            // нажатия
        }
        if (backPressedCount == 2) super.onBackPressed()
    }
*/

}