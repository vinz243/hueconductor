package github.hueconductor

import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.model.PHLight
import com.philips.lighting.model.PHLightState
import org.apache.logging.log4j.kotlin.Logging


class Conductor(var hueSdk: PHHueSDK, private val targetLightId: String): Logging {
    private var previousState: PHLightState? = null

    private fun light (): PHLight? {
        return hueSdk.selectedBridge.resourceCache.allLights.find { it.uniqueId == targetLightId }
    }

    fun playbackResumed () {
        logger.info { "Transitioning to cinema mode" }

        val state = PHLightState()
        state.brightness = 15
        state.hue = 0
        state.transitionTime = 16

        val light = light()

        previousState = light?.lastKnownLightState

        hueSdk.selectedBridge?.updateLightState(light, state)
    }


    fun playbackPaused () {
        logger.info { "Restoring previous light state" }
        previousState?.brightness = 128
        previousState?.transitionTime = 16
        hueSdk.selectedBridge.updateLightState(light(), previousState ?: whiteLight())
    }

    private fun whiteLight(): PHLightState {
        val state = PHLightState()
        state.brightness = 128
        state.hue = 0
        state.transitionTime = 1

        return state
    }
}