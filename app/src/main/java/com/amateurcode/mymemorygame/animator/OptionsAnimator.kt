package com.amateurcode.mymemorygame.animator

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.amateurcode.mymemorygame.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class OptionsAnimator (context: Context,
                       private val mainBtn: FloatingActionButton,
                       private val boardSizeBtn: FloatingActionButton,
                       private val refreshBtn: FloatingActionButton) {

    private var closed = false

    init{
        setMainOnClick()
    }

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_open
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_close
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_bottom
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_bottom
        )
    }
    private val fromTop: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_top
        )
    }
    private val toTop: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_top
        )
    }

    fun setMainOnClick(){
        mainBtn.setOnClickListener {
            toggleMenu()
        }
    }
    fun toggleMenu(){
        setVisibility(closed)
        setAnimation(closed)
        closed = !closed
    }

    fun setRefreshOnClick(onClick: OnClickListener){
        refreshBtn.setOnClickListener(onClick)
    }

    fun setBoardSizeOnClick(onClick: OnClickListener){
        boardSizeBtn.setOnClickListener(onClick)
    }

    // A Function used to set the Animation effect
    private fun setAnimation(closed: Boolean) {
        if (!closed) {
            boardSizeBtn.startAnimation(fromTop)
            refreshBtn.startAnimation(fromTop)
            mainBtn.startAnimation(rotateOpen)
        } else {
            boardSizeBtn.startAnimation(toTop)
            refreshBtn.startAnimation(toTop)
            mainBtn.startAnimation(rotateClose)
        }
    }

    // used to set visibility to VISIBLE / INVISIBLE
    private fun setVisibility(closed: Boolean) {
        if (!closed) {
            refreshBtn.visibility = View.VISIBLE
            boardSizeBtn.visibility = View.VISIBLE
        } else {
            refreshBtn.visibility = View.INVISIBLE
            refreshBtn.visibility = View.INVISIBLE
        }
    }
}