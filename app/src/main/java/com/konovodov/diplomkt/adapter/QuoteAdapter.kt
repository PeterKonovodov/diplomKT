package com.konovodov.diplomkt.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konovodov.diplomkt.R
import com.konovodov.diplomkt.databinding.QuoteLayoutBinding
import com.konovodov.diplomkt.db.QuoteEntity
import com.konovodov.diplomkt.dto.Quote
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.abs


typealias OnLikeListener = (quote: Quote) -> Unit
typealias OnDislikeListener = (quote: Quote) -> Unit
typealias OnShareListener = (quote: Quote) -> Unit
typealias OnAuthorListener = (quote: Quote) -> Unit
typealias OnDeleteListener = (quote: Quote) -> Unit


class QuoteAdapter(
    private val onLikeListener: (quote: Quote) -> Unit,
    private val onDislikeListener: (quote: Quote) -> Unit,
    private val onShareListener: (quote: Quote) -> Unit,
    private val onAuthorListener: (quote: Quote) -> Unit,
    private val onDeleteListener: (quote: Quote) -> Unit
) : PagedListAdapter<QuoteEntity, QuoteViewHolder>(QUOTE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding =
            QuoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(
            parent,
            binding,
            onLikeListener,
            onDislikeListener,
            onShareListener,
            onAuthorListener,
            onDeleteListener
        )
    }



    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        quote?.let{holder.bind(it)}
    }

    companion object {
        private val QUOTE_COMPARATOR = object : DiffUtil.ItemCallback<QuoteEntity>() {
            override fun areItemsTheSame(oldItem: QuoteEntity, newItem: QuoteEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: QuoteEntity, newItem: QuoteEntity): Boolean {
                return oldItem.likes == newItem.likes
            }
        }
    }


}



class QuoteViewHolder(
    private val parent: ViewGroup,
    private val binding: QuoteLayoutBinding,
    private val onLikeListener: OnLikeListener,
    private val onDislikeListener: OnDislikeListener,
    private val onShareListener: OnShareListener,
    private val onAuthorListener: OnAuthorListener,
    private val onDeleteListener: OnDeleteListener,

    ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(quote: QuoteEntity) {
        binding.apply {

            authorNameText.text = quote.author

            if (quote.fromAuthor.isNotEmpty())
                authorNameText.text = (quote.author + " (сплагиатил у " + quote.fromAuthor + ")")
            else authorNameText.text = quote.author

            publishedText.text = LocalDateTime.ofEpochSecond(quote.published, 0, ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))

            contentText.text = quote.content

            likesText.text = statNumberToString(quote.likes)
            if (quote.link.isNotEmpty()) {
                linkText.text = quote.link
                linkText.visibility = View.VISIBLE
            } else linkText.visibility = View.GONE


/*
            quote.imageDrawable?.let {
                imageContent.visibility = View.VISIBLE
                imageContent.setImageDrawable(it)
            } ?: run { imageContent.visibility = View.GONE }
*/


            if (quote.author == parent.resources.getString(R.string.user_name)) {
                toolbar.menu.findItem(R.id.action_delete_quote).isVisible = true
                toolbar.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_delete_quote -> onDeleteListener(quote.toDto())
                    }
                    true
                }
            } else {
                toolbar.menu.findItem(R.id.action_delete_quote).isVisible = false
                toolbar.setOnMenuItemClickListener {
                    true
                }
            }

            likeButton.setOnClickListener {
                onLikeListener(quote.toDto())
            }
            dislikeButton.setOnClickListener {
                onDislikeListener(quote.toDto())
            }
            shareButton.setOnClickListener {
                onShareListener(quote.toDto())
            }

            authorNameText.setOnClickListener {
                onAuthorListener(quote.toDto())
            }
            linkText.setOnClickListener {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(linkText.toString())
                }
            }

        }
    }

    private fun statNumberToString(number: Int): String {
        return when (abs(number)) {
            in 0..999 -> number.toString()
            in 1000..9999 -> String.format("%1.1fK", number / 100 / 10f)
            in 10000..999999 -> String.format("%dK", number / 1000)
            else -> String.format("%.1fM", number / 1000000f)
        }

    }
}

