package org.deepercreeper.engine.resources.types;

import org.deepercreeper.engine.resources.WritableResource;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class ImageResource extends WritableResource<BufferedImage> {
    private final String format;

    public ImageResource(@NotNull String name) {
        super(BufferedImage.class, name);
        format = getFile().getName().replaceAll(".*\\.", "").toLowerCase(Locale.ENGLISH);
    }

    @NotNull
    @Override
    protected BufferedImage load(@NotNull InputStream stream) throws Exception {
        return ImageIO.read(stream);
    }

    @Override
    protected void save(@NotNull BufferedImage value, @NotNull OutputStream stream) throws Exception {
        ImageIO.write(value, format, stream);
    }
}
