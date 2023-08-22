package com.amateurcode.mymemorygame

import android.animation.ArgbEvaluator
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amateurcode.mymemorygame.animator.OptionsAnimator
import com.amateurcode.mymemorygame.models.BoardSize
import com.amateurcode.mymemorygame.models.MemoryGame
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    // lateinit means it will initialize later
    private lateinit var rvBoard: RecyclerView
    private lateinit var clRoot: ConstraintLayout
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView
    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var btnBoardSize: FloatingActionButton
    private lateinit var btnRefresh: FloatingActionButton
    private lateinit var btnOptions: FloatingActionButton
    private lateinit var optionsAnimator: OptionsAnimator

    private var boardSize: BoardSize = BoardSize.EASY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

        btnOptions = findViewById(R.id.btnOptions)
        btnBoardSize = findViewById(R.id.btnBoardSize)
        btnRefresh = findViewById(R.id.btnRefresh)

        setUpOptionsMenu()
        setUpBoard()
    }

    private fun setUpOptionsMenu(){
        btnBoardSize.visibility = View.INVISIBLE
        btnRefresh.visibility = View.INVISIBLE

        optionsAnimator = OptionsAnimator(this, btnOptions, btnBoardSize, btnRefresh)

        optionsAnimator.setRefreshOnClick{
            optionsAnimator.toggleMenu()
            if(memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                showAlertDialogue("Quit your current Game",null){
                    setUpBoard()
                }
            }else{
                showDialogue("Game has reset",null)
                setUpBoard()
            }
        }
        optionsAnimator.setBoardSizeOnClick{
            showNewSizeDialog()
            optionsAnimator.toggleMenu()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        rvBoard.adapter = adapter
        super.onConfigurationChanged(newConfig)
    }
    private fun showNewSizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        showAlertDialogue("Choose new size", boardSizeView){
            boardSize = when(radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                R.id.rbHard -> BoardSize.HARD
                else -> boardSize
            }

            setUpBoard()
        }
    }

    private fun showDialogue(title: String, view: View?){
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setPositiveButton("OK",null)
            .show()
    }

    private fun showAlertDialogue(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("CANCEL", null)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun setUpBoard(){
        when(boardSize){
            BoardSize.EASY -> {
                tvNumMoves.text = "Easy: 4 x 2"
                tvNumPairs.text = "Pairs: 0 / 4"
            }
            BoardSize.MEDIUM -> {
                tvNumMoves.text = "Easy: 6 x 3"
                tvNumPairs.text = "Pairs: 0 / 9"

            }
            BoardSize.HARD -> {
                tvNumMoves.text = "Easy: 6 x 4"
                tvNumPairs.text = "Pairs: 0 / 12"

            }
        }

        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.colorError))
        memoryGame = MemoryGame(boardSize)

        // This will become dynamic later on depending on the level
        adapter = MemoryBoardAdapter(this,boardSize, memoryGame.cards, object:MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })

        //The size of the RecyclerView (rvBoard) will always remain the same regardless of the adapter
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(false)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    private fun updateGameWithFlip(position: Int) {
        if(memoryGame.haveWonGame()){
            showAlertDialogue("Congratulations you've won!!!!! Restart?",null){
                setUpBoard()
            }
        }
        if(memoryGame.flipCard(position)){
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                    ContextCompat.getColor(this, R.color.colorError),
                    ContextCompat.getColor(this, R.color.colorProgressFull)
                ) as Int

            tvNumPairs.setTextColor(color)
            tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if(memoryGame.haveWonGame()){
                showAlertDialogue("You have won the game... Restart?",null){
                    setUpBoard()
                }
            }
        }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }
}
