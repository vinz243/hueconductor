package github.hueconductor

import com.philips.lighting.hue.sdk.PHAccessPoint
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.hue.sdk.PHSDKListener
import com.philips.lighting.model.PHBridge
import com.philips.lighting.model.PHHueParsingError

class PHHandler(private val phHueSDK: PHHueSDK) : PHSDKListener {
    override fun onBridgeConnected(phBridge: PHBridge?, p1: String?) {
        phHueSDK.selectedBridge = phBridge
        phHueSDK.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL.toLong())
    }

    override fun onParsingErrors(p0: MutableList<PHHueParsingError>?) {
        println("error $p0")
    }

    override fun onAccessPointsFound(p0: MutableList<PHAccessPoint>?) {}

    override fun onConnectionLost(p0: PHAccessPoint?) {}

    override fun onCacheUpdated(p0: MutableList<Int>?, phBridge: PHBridge?) {
    }

    override fun onAuthenticationRequired(p0: PHAccessPoint?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(p0: Int, p1: String?) {
        println("error = $p1")
    }

    override fun onConnectionResumed(phBridge: PHBridge?) {
    }

    companion object {
        fun connect(phHueSDK: PHHueSDK, ipAddress: String, userName: String): PHHandler {
            val hueSdk = PHHueSDK.create()

            val phHandler = PHHandler(phHueSDK)

            hueSdk.notificationManager.registerSDKListener(phHandler)

            val point = PHAccessPoint(ipAddress, userName, null)
            hueSdk.connect(point)

            return phHandler
        }
    }
}