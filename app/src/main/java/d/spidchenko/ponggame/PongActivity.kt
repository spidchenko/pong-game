package d.spidchenko.ponggame

import android.app.Activity
import android.os.Bundle
import android.view.Window

class PongActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
    }
}