package org.deepercreeper.engine.resources.types;

import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.Clip;

public class SoundResourceTest {
    private static final SoundResource RESOURCE = new SoundResource("org/deepercreeper/engine/resources/types/SoundResourceTest.wav");

    @Test
    public void testLoading() {
        Clip clip = RESOURCE.load();
        Assert.assertEquals(63882, clip.getFrameLength());
    }
}
