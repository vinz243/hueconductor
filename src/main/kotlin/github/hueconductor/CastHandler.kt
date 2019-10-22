package github.hueconductor

import kotlinx.coroutines.*
import org.apache.logging.log4j.kotlin.Logging
import su.litvak.chromecast.api.v2.*

class CastHandler(private val conductor: Conductor, private val chromeCast: ChromeCast) :
    ChromeCastSpontaneousEventListener,
    Logging {
    private var job: Job? = null
    private var playing = false

    override fun spontaneousEventReceived(event: ChromeCastSpontaneousEvent?) {
        logger.debug { "Event received [${event?.type}]" }
        if (event?.type == ChromeCastSpontaneousEvent.SpontaneousEventType.STATUS) {
            val status = event.getData(Status::class.java)

            val appName = status.runningApp?.name

            playbackLoop(appName)
        }
    }

    fun playbackLoop(appName: String?) {
        logger.info { "Detected running app [$appName]" }

        if (appName == "Netflix") {
            job = GlobalScope.launch {
                logger.info("Starting pooling")
                while (isActive) {
                    delay(1000)

                    val playerState: MediaStatus.PlayerState? = try {
                        chromeCast.mediaStatus?.playerState
                    } catch (e: ChromeCastException) {
                        e.printStackTrace()
                        null
                    }

                    if (playerState != null) {
                        logger.trace { "Received player state [$playerState]" }

                        val currentlyPlaying = isPlaying(playerState)
                        if (currentlyPlaying != playing) {
                            playing = currentlyPlaying

                            if (playing) {
                                conductor.playbackResumed()
                            } else {
                                conductor.playbackPaused()
                            }
                        }
                    }
                }
            }

            return
        }

        logger.info("Stopping pooling [$job]")

        job?.cancel()
    }

    private fun isPlaying(playerState: MediaStatus.PlayerState?) = playerState == MediaStatus.PlayerState.PLAYING
}