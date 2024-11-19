package com.example.project5

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import com.example.project5.GameView.Companion.DELTA_TIME
import java.util.Random

class Pong {
    private var deltaTime : Int = 0
    private var ballCenter : Point? = null
    private var paddleCenter : Point? = null
    private var left : Boolean = true
    private var bounce :Boolean = false
    private var width : Int = 0
    private var height : Int = 0
    private var loser : Boolean = false
    private var score : Int = 0
    private var best : Int = 0
    private var bonk : Boolean = false
    private var speed : Float = 0.0f
    private var size : Float = 0.0f
    private var paddleSize : Float = 0.0f


    constructor(height : Int, width : Int, context: Context, speed : Float, size : Float, paddleSize : Float) {

        val pref : SharedPreferences =
            context.getSharedPreferences( context.packageName + "_preferences", Context.MODE_PRIVATE)
        best = pref.getInt(PREFERENCE_BEST, 0)

        paddleCenter = Point(width/2, height - 10)
        ballCenter = Point(width/2, 30)

        this.width = width
        this.height = height
        this.size = size
        this.speed = speed
        this.paddleSize = paddleSize
    }

    fun getBonk() : Boolean {
        return bonk
    }

    fun unBonk() {
        bonk = false
    }

    fun getBallCenter(): Point {
        return ballCenter!!
    }

    fun getPaddleCenter(): Point {
        return paddleCenter!!
    }


    fun setDeltaTime(newDeltaTime: Int) {
        if (newDeltaTime > 0)
            deltaTime = newDeltaTime
    }

    fun moveBall() {
        if ( left ) {
            ballCenter!!.x -= (speed * deltaTime).toInt()
        } else {
            ballCenter!!.x += (speed * deltaTime).toInt()
        }
        if ( !bounce ) {
            ballCenter!!.y += (speed * deltaTime).toInt()
        } else {
            ballCenter!!.y -= (speed * deltaTime).toInt()
        }
        wallCollision()
        paddleCollision()
        endCollision()
    }

    fun wallCollision() {
        if ( 0 + (size + DELTA_TIME/10*speed) >= ballCenter!!.x - size )
            left = false

        if ( width - (size + DELTA_TIME/10*speed)<= ballCenter!!.x + size )
            left = true
    }

    fun paddleCollision() {
        if ( height - (size + DELTA_TIME/10*speed) <= ballCenter!!.y + size &&
            height - (size + DELTA_TIME/10*speed) >= ballCenter!!.y) {
            if (ballCenter!!.x >= paddleCenter!!.x - 100 && ballCenter!!.x <= paddleCenter!!.x + 100 ) {
                bounce = true
                bonk = true
                score += 1
            }
        }
    }

    fun endCollision() {
        if ( 0 + (size + DELTA_TIME/10*speed) >= ballCenter!!.y - size)
            bounce = false
        if (height - (size + DELTA_TIME/10*speed) < ballCenter!!.y) {
            loser = true
        }
    }

    fun getLoser() : Boolean {
        return loser
    }

    fun setPaddleCenter(point: Point?) {
        if (point != null)
            paddleCenter = point
    }

    fun getSize() : Float {
        return size
    }

    fun getPaddleSize() : Float {
        return paddleSize
    }

    fun getScore() : Int {
        return score
    }

    fun getBest() : Int {
        return best
    }

    fun setPreferences( context : Context) {
        val pref : SharedPreferences =
            context.getSharedPreferences( context.packageName + "_preferences", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()
        if (score > best) {
            best = score
        }

        editor.putInt(PREFERENCE_BEST, best)
        editor.commit()
    }

    companion object {
        private const val PREFERENCE_BEST : String = "best"
    }
}