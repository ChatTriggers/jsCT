package com.chattriggers.ctjs.minecraft.libs.renderer;

import com.chattriggers.ctjs.CTJS;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Used in {@link Renderer#image(String)}
 */
@Accessors(chain = true)
public class Image {
    /**
     * -- GETTER --
     * Gets the image x position
     *
     * @return The image x position
     * <p>
     * -- SETTER --
     * Sets the image x position
     * @param x The new image x position
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private float x;

    /**
     * -- GETTER --
     * Gets the image y position
     *
     * @return The image y position
     * <p>
     * -- SETTER --
     * Sets the image y position
     * @param y The new image y position
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private float y;

    /**
     * -- GETTER --
     * Gets the texture width
     *
     * @return The texture width
     * <p>
     * -- SETTER --
     * Sets the texture width
     * @param textureWidth The new texture width
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private int textureWidth;

    /**
     * -- GETTER --
     * Gets the texture height
     *
     * @return The texture height
     * <p>
     * -- SETTER --
     * Sets the texture height
     * @param textureHeight The new texture height
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private int textureHeight;

    /**
     * -- GETTER --
     * Gets the texture x location
     *
     * @return The texture x location
     * <p>
     * -- SETTER --
     * Sets the texture x location
     * @param textureX The new texture x location
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private int textureX;

    /**
     * -- GETTER --
     * Gets the texture y location
     *
     * @return The texture y location
     * <p>
     * -- SETTER --
     * Sets the texture y location
     * @param textureY The new texture y location
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private int textureY;

    /**
     * -- GETTER --
     * Gets the image x scale
     *
     * @return The image x scale
     * <p>
     * -- SETTER --
     * Sets the image scale
     * @param scaleX The new image x scale
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private float scaleX;

    /**
     * -- GETTER --
     * Gets the image y scale
     *
     * @return The image y scale
     * <p>
     * -- SETTER --
     * Sets the image scale
     * @param scaleY The new image y scale
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private float scaleY;

    /**
     * -- GETTER --
     * Gets the image resource name
     *
     * @return The image resource name
     * <p>
     * -- SETTER --
     * Sets the image resource name
     * @param resourceName The new image resource name
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private String resourceName;

    /**
     * -- GETTER --
     * Gets the image resource domain
     *
     * @return The image resource domain
     * <p>
     * -- SETTER --
     * Sets the image resource domain
     * @param resourceDomain The new image resource domain
     * @return The Image object to allow for method chaining
     */
    @Getter
    @Setter
    private String resourceDomain;

    Image(String resourceName) {
        this.resourceName = resourceName;

        this.x = 0;
        this.y = 0;
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.textureX = 0;
        this.textureY = 0;
        this.scaleX = 1;
        this.scaleY = 1;
        this.resourceDomain = "ctjs.images";
    }

    /**
     * Sets the image scale
     *
     * @return The Image object to allow for method chaining
     */
    public Image setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;

        return this;
    }

    /**
     * Sets the texture location on the bound image.
     *
     * @param x The texture x position
     * @param y The texture y position
     * @param width The texture width
     * @param height The texture height
     * @return The Image object to allow for method chaining
     */
    public Image setTextureLocation(int x, int y, int width, int height) {
        this.textureX = x;
        this.textureY = y;
        this.textureWidth = width;
        this.textureHeight = height;

        return this;
    }

    /**
     * Downloads an image to store at the resource name location.
     *
     * @param url          The url to download the image from
     * @param shouldResize If the image should be resized to 256x256
     * @return The Image object to allow for method chaining
     */
    public Image download(String url, boolean shouldResize) {
        try {
            BufferedImage image = ImageIO.read(new URL(url));

            File resourceFile = new File(CTJS.getInstance().getAssetsDir(), this.resourceName);
            resourceFile.createNewFile();

            if (shouldResize) {
                BufferedImage resized = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = resized.createGraphics();
                g.drawImage(image, 0, 0, 256, 256, null);
                g.dispose();
                image = resized;
            }

            ImageIO.write(image, url.endsWith("jpeg") || url.endsWith("jpg") ? "jpg" : "png", resourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Draws the image on screen
     *
     * @return The Image object to allow for method chaining
     */
    public Image draw() {
        Renderer.drawImage(this.resourceDomain, this.resourceName, this.x, this.y, this.scaleX, this.scaleY, this.textureX, this.textureY, this.textureWidth, this.textureHeight);

        return this;
    }
}
