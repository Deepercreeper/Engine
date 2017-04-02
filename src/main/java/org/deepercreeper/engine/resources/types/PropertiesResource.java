package org.deepercreeper.engine.resources.types;

import org.deepercreeper.engine.resources.WritableResource;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesResource extends WritableResource<Properties> {
    public PropertiesResource(@NotNull String name) {
        super(Properties.class, name);
    }

    @NotNull
    @Override
    protected Properties load(@NotNull InputStream stream) throws Exception {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
        return properties;
    }

    @Override
    protected void save(@NotNull Properties value, @NotNull OutputStream stream) throws Exception {
        value.store(new OutputStreamWriter(stream, StandardCharsets.UTF_8), null);
    }
}
