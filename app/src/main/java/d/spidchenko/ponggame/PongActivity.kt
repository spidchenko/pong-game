package d.spidchenko.ponggame

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout

class PongActivity : Activity() {

    companion object{
        private const val TAG = "PongActivity.LOG_TAG"
    }

    private lateinit var pongGame : PongGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        pongGame = PongGame(this)
        val gameLayout: LinearLayout = findViewById(R.id.gameLayout)
        gameLayout.addView(pongGame)
    }

    override fun onResume() {
        super.onResume()
        pongGame.resume()
    }

    override fun onPause() {
        super.onPause()
        pongGame.pause()
    }
}