package com.konovodov.diplomkt.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konovodov.diplomkt.databinding.QuoteLayoutBinding
import com.konovodov.diplomkt.dto.Quote
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.abs


typealias OnLikeListener = (quote: Quote) -> Unit
typealias OnDislikeListener = (quote: Quote) -> Unit
typealias OnShareListener = (quote: Quote) -> Unit
typealias OnAuthorListener = (quote: Quote) -> Unit


class QuoteAdapter(
    private val onLikeListener: (quote: Quote) -> Unit,
    private val onDislikeListener: (quote: Quote) -> Unit,
    private val onShareListener: (quote: Quote) -> Unit,
    private val onAuthorListener: (quote: Quote) -> Unit
) : ListAdapter<Quote, QuoteViewHolder>(QuoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding =
            QuoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(
            parent,
            binding,
            onLikeListener,
            onDislikeListener,
            onShareListener,
            onAuthorListener
        )
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        holder.bind(quote)
    }
}


class QuoteViewHolder(
    private val parent: ViewGroup,
    private val binding: QuoteLayoutBinding,
    private val onLikeListener: OnLikeListener,
    private val onDislikeListener: OnDislikeListener,
    private val onShareListener: OnShareListener,
    private val onAuthorListener: OnAuthorListener,

    ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(quote: Quote) {
        binding.apply {


            authorNameText.text = quote.author

            if (quote.fromAuthor.isNotEmpty())
                authorNameText.text = (quote.author + " (сплагиатил у " + quote.fromAuthor + ")")
            else authorNameText.text = quote.author

            publishedText.text = LocalDateTime.ofEpochSecond(quote.published, 0, ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

            contentText.text = quote.content

            likesText.text = statNumberToString(quote.likes)
            linkText.text = quote.link


            quote.imageDrawable?.let {
                contentImage.visibility = View.VISIBLE
                imageContent.setImageDrawable(it)
            } ?: run { contentImage.visibility = View.GONE }


            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
//                    R.id.action_delete_note -> onDeleteListener(quote)
//                    R.id.action_edit_note -> onEditListener(quote)
                }
                true
            }

            likeButton.setOnClickListener {
                onLikeListener(quote)
            }
            dislikeButton.setOnClickListener {
                onDislikeListener(quote)
            }
            shareButton.setOnClickListener {
                onShareListener(quote)
            }

            authorNameText.setOnClickListener {
                onAuthorListener(quote)
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

class QuoteDiffCallback : DiffUtil.ItemCallback<Quote>() {
    override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem == newItem
    }
}


