package com.example.project5

import android.graphics.Point
import android.graphics.Rect
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Timer

class MainActivity : AppCompatActivity() {
    private lateinit var gameView : GameView
    private lateinit var game : Pong
    private var started : Boolean = false

    private lateinit var detector: GestureDetector

    private lateinit var pool : SoundPool
    private var hitSoundId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        var builder : SoundPool.Builder = SoundPool.Builder()
        pool = builder.build()
        hitSoundId = pool.load(this,R.raw.hit, 1)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        buildViewByCode( )
        animate()
    }

    fun animate () {
        var timer : Timer = Timer()
        var task : GameTimerTask = GameTimerTask(this)
        timer.schedule(task, 0L, GameView.DELTA_TIME.toLong())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if( event != null )
            detector.onTouchEvent( event )
        return super.onTouchEvent(event)
    }

    fun buildViewByCode( ) {
        var width : Int = resources.displayMetrics.widthPixels
        var height : Int = resources.displayMetrics.heightPixels

        val rectangle : Rect = Rect( 0, 0, 0, 0 )
        window.decorView.getWindowVisibleDisplayFrame( rectangle )
        Log.w( "MainActivity", "width = " + width )
        Log.w( "MainActivity", "height = " + height )
        Log.w( "MainActivity", "top = " + rectangle.top )

        gameView = GameView( this, width, height - rectangle.top )
        game = gameView.getGame()
        setContentView( gameView )

        var th : TouchHandler = TouchHandler()
        detector = GestureDetector(this, th)
    }

    fun updateModel() {
        if (started) {
            //Log.w( "MainActivity", "started = " + started )
            if (game.getLoser()) {
                //Log.w( "MainActivity", "loser = " + game.getLoser() )
                gameView.endScreen(this)
                game = gameView.getGame()
                started = false
            } else {
                game.moveBall()
                if (game.getBonk()) {
                    pool.play(hitSoundId,1.0f,1.0f,1,0,1.0f)
                    game.unBonk()
                }
            }
        }
    }

    fun updateView() {
        gameView.postInvalidate()
    }

    fun updatePaddle( distance : Float) {
        game.setPaddleCenter(Point(game.getPaddleCenter().x - distance.toInt(), gameView.height - 10))
    }

    inner class TouchHandler : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (!started) {
                started = true
                gameView.start()
            }
            return super.onSingleTapConfirmed(e)
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (started) {
                updatePaddle(distanceX)
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }
}