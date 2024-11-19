package com.example.project5

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.TextView

class GameView : View {
    private var paint : Paint
    private var width : Int = 0
    private var height : Int = 0
    private var ended : Boolean = false
    private var scoreDisplay : Int = 0
    private var bestDisplay : Int = 0


    private lateinit var game : Pong

    constructor(context : Context, width : Int, height : Int ) : super( context ) {

        this.width = width
        this.height = height
        paint = Paint( )
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.strokeWidth = 20.0f
        paint.textSize = 100.0f


        game = Pong(height, width, context, .3f, 25.0f, 100.0f)
        game.setDeltaTime( DELTA_TIME )
    }

    fun getGame( ) : Pong {
        return game
    }

    fun start() {
        ended = false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Log.w( "MainActivity", "Inside Draw" )

        //ball stuff
        canvas.drawCircle( game.getBallCenter().x.toFloat(), game.getBallCenter().y.toFloat(),
            game.getSize(), paint )

        //paddle stuff
        canvas.drawLine(game.getPaddleCenter().x - game.getPaddleSize(), height - 10.0f,
            game.getPaddleCenter().x + game.getPaddleSize(), height - 10.0f, paint )

        if (ended) {
            canvas.drawText("Score: " + scoreDisplay, (width / 2.0f) - 200.0f, (height/ 2.0f) - 50.0f, paint)
            canvas.drawText("Best Score: " + bestDisplay, (width / 2.0f) - 200.0f, (height/ 2.0f) + 50.0f, paint)
        }
    }

    fun endScreen( context : Context) {
        game.setPreferences(context)

        scoreDisplay = game.getScore()

        bestDisplay = game.getBest()

        game = Pong(height, width, context, .05f, 25.0f, 100.0f)
        game.setDeltaTime( DELTA_TIME )
        ended = true
    }

    companion object {
        val DELTA_TIME : Int = 100
    }
}