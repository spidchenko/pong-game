package d.spidchenko.ponggame

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundEngine(context: Context) {
    private val soundPool: SoundPool
    private val beepId: Int
    private val boopId: Int
    private val bopId: Int
    private val missId: Int

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        val assetsManager = context.assets
        var fileDescriptor = assetsManager.openFd("beep.ogg")
        beepId = soundPool.load(fileDescriptor, 0)
        fileDescriptor = assetsManager.openFd("boop.ogg")
        boopId = soundPool.load(fileDescriptor, 0)
        fileDescriptor = assetsManager.openFd("bop.ogg")
        bopId = soundPool.load(fileDescriptor, 0)
        fileDescriptor = assetsManager.openFd("miss.ogg")
        missId = soundPool.load(fileDescriptor, 0)
    }

    fun playBeep() = playSoundById(beepId)

    fun playBoop() = playSoundById(boopId)

    fun playBop() = playSoundById(bopId)

    fun playMiss() = playSoundById(missId)

    private fun playSoundById(soundId: Int) {
        val leftVolume = 1F
        val rightVolume = 1F
        val priority = 0
        val loopMode = 0
        val rate = 1F
        soundPool.play(soundId, leftVolume, rightVolume, priority, loopMode, rate)
    }

    companion object {
        private const val TAG = "SoundEngine.LOG_TAG"
    }

}
