package com.konovodov.diplomkt.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.konovodov.diplomkt.R
import com.konovodov.diplomkt.adapter.QuoteAdapter
import com.konovodov.diplomkt.databinding.FeedFragmentBinding
import com.konovodov.diplomkt.viewmodel.QuoteViewModel

class FeedFragment : Fragment() {

    val viewModel: QuoteViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
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

        return binding.root
    }
}