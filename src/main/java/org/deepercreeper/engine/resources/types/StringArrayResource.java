package org.deepercreeper.engine.resources.types;

import org.apache.commons.io.IOUtils;
import org.deepercreeper.engine.resources.WritableResource;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class StringArrayResource extends WritableResource<String[]> {
    public StringArrayResource(@NotNull String name) {
        super(String[].class, name);
    }

    @NotNull
    @Override
    protected String @NotNull [] load(@NotNull InputStream stream) throws Exception {
        List<String> lines = IOUtils.readLines(stream, StandardCharsets.UTF_8);
        return lines.toArray(new String[lines.size()]);
    }

    @Override
    protected void save(@NotNull String @NotNull [] value, @NotNull OutputStream stream) throws Exception {
        for (String line : value) {
            IOUtils.write(line, stream, StandardCharsets.UTF_8);
        }
    }
}
