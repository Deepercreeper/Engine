package org.deepercreeper.engine.resources.types;

import org.apache.commons.io.IOUtils;
import org.deepercreeper.engine.resources.Resource;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SoundResource extends Resource<Clip> {
    public SoundResource(@NotNull String name) {
        super(Clip.class, name);
    }

    @NotNull
    @Override
    protected Clip load(@NotNull InputStream stream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(stream, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(bytes)));
        return clip;
    }
}
