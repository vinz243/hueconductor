package github.hueconductor

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.philips.lighting.hue.sdk.PHHueSDK
import su.litvak.chromecast.api.v2.ChromeCast

fun main (args: Array<String>) = Main().main(args)

class Main : CliktCommand() {
    private val bridgeIp: String by option(help = "IP Address for the Hue Bridge").required()
    private val bridgeKey: String by option(help = "Bridge username/key").required()
    private val lightId: String by option(help = "Hue light uniqueId").required()
    private val castIp: String by option(help = "Chromecast IP Address").required()

    override fun run() {
        val phHueSDK = PHHueSDK.create()

        PHHandler.connect(
            phHueSDK = phHueSDK,
            ipAddress = bridgeIp,
            userName = bridgeKey
        )

        val conductor = Conductor(hueSdk = phHueSDK, targetLightId = lightId)

        val chromeCast = ChromeCast(castIp)
        val castHandler = CastHandler(conductor, chromeCast)
        chromeCast.registerListener(castHandler)
        castHandler.playbackLoop(chromeCast.runningApp?.name)
    }

}
