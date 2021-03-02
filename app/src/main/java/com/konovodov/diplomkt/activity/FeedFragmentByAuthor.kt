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

class FeedFragmentByAuthor : Fragment() {

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
            onShareListener = { viewModel.shareById(it.id) }
        )

        binding.newsFeed.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { notesList -> adapter.submitList(notesList) })

        return binding.root
    }
}