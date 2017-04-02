package org.deepercreeper.engine.resources.types;

import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;

public class ImageResourceTest {
    private static final ImageResource RESOURCE = new ImageResource("org/deepercreeper/engine/resources/types/ImageResourceTest.png");

    @Test
    public void testLoading() {
        BufferedImage image = RESOURCE.load();
        Assert.assertEquals(0xff000000, image.getRGB(0, 0));
        Assert.assertEquals(0xffffffff, image.getRGB(1, 0));
        Assert.assertEquals(0xff000000, image.getRGB(2, 0));
    }
}
