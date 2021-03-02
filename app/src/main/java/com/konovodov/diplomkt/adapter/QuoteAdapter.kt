package com.konovodov.diplomkt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konovodov.diplomkt.databinding.QuoteInlistLayoutBinding
import com.konovodov.diplomkt.dto.Quote
import java.time.LocalDate
import java.time.format.DateTimeFormatter


typealias OnLikeListener = (quote: Quote) -> Unit
typealias OnDislikeListener = (quote: Quote) -> Unit
typealias OnShareListener = (quote: Quote) -> Unit


class QuoteAdapter(
    private val onLikeListener: (quote: Quote) -> Unit,
    private val onDislikeListener: (quote: Quote) -> Unit,
    private val onShareListener: (quote: Quote) -> Unit

) : ListAdapter<Quote, QuoteViewHolder>(QuoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding =
            QuoteInlistLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(
            parent,
            binding,
            onLikeListener,
            onDislikeListener,
            onShareListener
        )
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        holder.bind(quote)
    }
}


class QuoteViewHolder(
    private val parent: ViewGroup,
    private val binding: QuoteInlistLayoutBinding,
    private val onLikeListener: OnLikeListener,
    private val onDislikeListener: OnDislikeListener,
    private val onShareListener: OnShareListener
    ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(quote: Quote) {
        binding.apply {


            authorNameText.text = quote.author
            publishedText.text = LocalDate.ofEpochDay(quote.published)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            contentText.text = quote.content

            dislikeButton.text = statNumberToString(quote.likes)
            linkText.text = quote.link


            if (quote.imagePath.isNotEmpty()) {
                contentImage.visibility = View.VISIBLE
            } else contentImage.visibility = View.GONE


//            likesButton.isChecked = note.likedByMe


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
        }
    }

    private fun statNumberToString(number: Int): String {
        return when (number) {
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


