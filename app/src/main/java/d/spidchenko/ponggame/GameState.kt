package d.spidchenko.ponggame

import android.content.Context
import android.graphics.Point
import d.spidchenko.ponggame.graphics.Ball
import d.spidchenko.ponggame.graphics.Bat
import d.spidchenko.ponggame.graphics.GameObject

class GameState(
    context: Context
) {

    val screenSize = Point()
    var score: Int = 0
    private set
    var totalLives: Int = 30
    private set
    val gameObjects = arrayListOf<GameObject>()

    fun newGame(screenX: Int, screenY: Int){
        this.screenSize.set(screenX, screenY)
        gameObjects.clear()
        gameObjects.add(Bat(screenX, screenY))
        val ball = Ball(screenX)
        ball.reset(screenX, screenY)
        gameObjects.add(ball)
    }

    fun increaseScore() = score++

    fun decreaseTotalLives() = totalLives--

    //fun initLevel(screenX: Int, screenY: Int) = gameStarter.initLevel(screenX, screenY)
}