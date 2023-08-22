package com.amateurcode.mymemorygame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.amateurcode.mymemorygame.models.BoardSize
import com.amateurcode.mymemorygame.models.MemoryCard
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClick: CardClickListener
):
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    // similar as static
    companion object {
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"
    }

    interface CardClickListener{
        fun onCardClicked(position: Int)
    }


    //Responsible for creating 1 view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardWidth = parent.width /boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight = parent.height / boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLength = min(cardWidth, cardHeight)

        val view: View = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)

        val loParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        loParams.height = cardSideLength
        loParams.width = cardSideLength
        loParams.setMargins(MARGIN_SIZE)

        return ViewHolder(view)
    }

    // how many elements
    override fun getItemCount() = boardSize.numCards

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(position: Int){

            val card = cards[position]
            imageButton.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.ic_launcher_background)

            imageButton.alpha = if (card.isMatched) .1f else 1.0f
            val colorStateList = if(card.isMatched) ContextCompat.getColorStateList(context, R.color.faded) else null
            ViewCompat.setBackgroundTintList(imageButton, colorStateList)

            imageButton.setOnClickListener{
                cardClick.onCardClicked(position)
            }
        }
    }
}