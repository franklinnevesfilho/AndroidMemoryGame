package com.amateurcode.mymemorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amateurcode.mymemorygame.models.BoardSize
import com.amateurcode.mymemorygame.models.MemoryCard
import com.amateurcode.mymemorygame.models.MemoryGame
import com.amateurcode.mymemorygame.utils.DEFAULT_ICONS
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    // lateinit means it will initialize later
    private lateinit var rvBoard: RecyclerView
    private lateinit var clRoot: ConstraintLayout
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter
    private var boardSize: BoardSize = BoardSize.EASY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

        memoryGame = MemoryGame(boardSize)

        // This will become dynamic later on depending on the level
        adapter = MemoryBoardAdapter(this,boardSize, memoryGame.cards, object:MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })

        //The size of the RecyclerView (rvBoard) will always remain the same regardless of the adapter
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())

    }

    private fun updateGameWithFlip(position: Int) {
        if(memoryGame.haveWonGame()){
            Snackbar.make(clRoot, "You have already Won", Snackbar.LENGTH_LONG).show()
        }
        if(memoryGame.isCardFaceUp(position)){
            Snackbar.make(clRoot, "INVALID MOVE!!", Snackbar.LENGTH_LONG).show()
        }
        if(memoryGame.flipCard(position)){
            tvNumPairs.setText("Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}")
        }
        adapter.notifyDataSetChanged()
    }
}