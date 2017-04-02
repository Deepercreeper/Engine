package org.deepercreeper.engine.resources.types;

import org.apache.commons.io.IOUtils;
import org.deepercreeper.engine.resources.WritableResource;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StringResource extends WritableResource<String> {
    public StringResource(@NotNull String name) {
        super(String.class, name);
    }

    @NotNull
    @Override
    protected String load(@NotNull InputStream stream) throws Exception {
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }

    @Override
    protected void save(@NotNull String value, @NotNull OutputStream stream) throws Exception {
        stream.write(value.getBytes(StandardCharsets.UTF_8));
    }
}
