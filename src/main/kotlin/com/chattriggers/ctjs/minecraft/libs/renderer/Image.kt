package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

@External
class Image(var image: BufferedImage?) {
    private lateinit var texture: DynamicTexture
    private var textureWidth = image?.width ?: 0
    private var textureHeight = image?.height ?: 0

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @JvmOverloads
    constructor(name: String, url: String? = null) : this(getBufferedImage(name, url))

    fun getTextureWidth(): Int = this.textureWidth
    fun getTextureHeight(): Int = this.textureHeight
    fun getTexture(): DynamicTexture = this.texture

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent) {
        if (image != null) {
            texture = DynamicTexture(image)
            image = null

            MinecraftForge.EVENT_BUS.unregister(this)
        }
    }

    @JvmOverloads
    fun draw(x: Double, y: Double,
             width: Double = this.textureWidth.toDouble(),
             height: Double = this.textureHeight.toDouble()) = apply {
        if (image != null) return@apply

        Renderer.drawImage(this, x, y, width, height)
    }
}

private fun getBufferedImage(name: String, url: String? = null): BufferedImage? {
    if (url == null) {
        val file = File(name)
        return ImageIO.read(file)
    }

    val resourceFile = File(CTJS.assetsDir, name)

    if (resourceFile.exists()) {
        return ImageIO.read(resourceFile)
    }

    val image = ImageIO.read(URL(url))
    ImageIO.write(image, "png", resourceFile)
    return image
}