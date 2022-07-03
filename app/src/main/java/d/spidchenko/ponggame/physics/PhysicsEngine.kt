package d.spidchenko.ponggame.physics

import android.graphics.RectF
import d.spidchenko.ponggame.GameState
import d.spidchenko.ponggame.SoundEngine
import d.spidchenko.ponggame.graphics.Ball
import d.spidchenko.ponggame.graphics.Bat
import d.spidchenko.ponggame.graphics.GameObject

class PhysicsEngine {

    fun update(fps: Long, soundEngine: SoundEngine, gameState: GameState) {
        val bat = gameState.gameObjects[0] as Bat
        val ball = gameState.gameObjects[1] as Ball
        bat.update(fps)
        ball.update(fps)
        detectCollisions(soundEngine, gameState)
    }

    private fun detectCollisions(soundEngine: SoundEngine, gameState: GameState) {

        val objects = gameState.gameObjects
        val bat = objects[0] as Bat
        for (i in 1 until objects.size){
            val ball = objects[i] as Ball
            when {
                RectF.intersects(bat.rect, ball.rect) -> {
                    ball.batBounce(bat.rect)
                    ball.increaseVelocity()
                    gameState.increaseScore()
                    soundEngine.playBeep()
                }

                ball.rect.bottom > gameState.screenSize.y -> {
                    ball.bounceOff(Ball.BOUNCE_UP)
                    gameState.decreaseTotalLives()
                    soundEngine.playMiss()
//                    if (totalLives == 0) {
//                        isPaused = true
//                        startNewGame()
//                    }
                }

                ball.rect.top < 0 -> {
                    ball.bounceOff(Ball.BOUNCE_DOWN)
                    soundEngine.playBoop()
                }

                ball.rect.left < 0 -> {
                    ball.bounceOff(Ball.BOUNCE_RIGHT)
                    soundEngine.playBop()
                }

                ball.rect.right >= gameState.screenSize.x -> {
                    ball.bounceOff(Ball.BOUNCE_LEFT)
                    soundEngine.playBop()
                }
            }
        }

    }
}