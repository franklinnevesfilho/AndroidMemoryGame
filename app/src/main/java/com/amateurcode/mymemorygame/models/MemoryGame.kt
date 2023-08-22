package com.amateurcode.mymemorygame.models

import com.amateurcode.mymemorygame.utils.DEFAULT_ICONS

class MemoryGame (private val boardSize: BoardSize){

    val cards: List<MemoryCard>
    var numPairsFound = 0;
    private var numMoves = 0;

    private var indexOfPreviousCard: Int? = null;

    init{

        val chosenImgs = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImgs = (chosenImgs + chosenImgs).shuffled()
        cards= randomizedImgs.map { MemoryCard(it) }

    }

    fun flipCard(position: Int):Boolean {
        numMoves++
        val card = cards[position]
        var result = false
        if(indexOfPreviousCard == null){
            restoreCards()
            indexOfPreviousCard = position
        }else if(position == indexOfPreviousCard){
            restoreCards()
            numMoves--
        }else if(!card.isMatched){
            result = checkForMatch(indexOfPreviousCard!!,position)
            indexOfPreviousCard = null;
        }

        if(!card.isFaceUp) card.isFaceUp = true

        return result;
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        var result = false
        if(cards[position1].identifier == cards[position2].identifier){
            result = true
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            numPairsFound++
        }
        return result
    }

    private fun restoreCards() {
        for(card in cards){
            if(card.isFaceUp && !card.isMatched){
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numMoves / 2
    }
}
