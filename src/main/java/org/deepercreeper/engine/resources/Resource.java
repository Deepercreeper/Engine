package org.deepercreeper.engine.resources;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public abstract class Resource<T> {
    private final Class<T> type;

    private final File file;

    private final int hashCode;

    public Resource(@NotNull Class<T> type, @NotNull String name) {
        this.type = type;
        this.file = findFile(name);
        checkFile();
        hashCode = hash();
    }

    private File findFile(String name) {
        URL url = getClass().getResource(name);
        if (url != null) {
            return FileUtils.toFile(url);
        }
        url = getClass().getClassLoader().getResource(name);
        if (url != null) {
            return FileUtils.toFile(url);
        }
        return new File(name);
    }

    protected void checkFile() {
        if (!getFile().isFile() || !getFile().canRead()) {
            throw new RuntimeException("File does not exist, is not a file or is not readable: " + getFile());
        }
    }

    public Class<T> getType() {
        return type;
    }

    public File getFile() {
        return file;
    }

    public T cast(Object value) {
        return getType().cast(value);
    }

    public @NotNull T load() {
        FileInputStream stream;
        T value;
        try {
            stream = new FileInputStream(getFile());
            value = load(stream);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not load file: " + getFile(), e);
        }
        IOUtils.closeQuietly(stream);
        return value;
    }

    @NotNull
    protected abstract T load(@NotNull InputStream stream) throws Exception;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Resource<?>) {
            Resource<?> resource = (Resource<?>) obj;
            return getFile().equals(resource.getFile()) && getType().equals(resource.getType());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    protected int hash() {
        return Objects.hash(getType(), getFile());
    }

    @Override
    public String toString() {
        return "<" + getType().getSimpleName() + ":" + getFile() + ">";
    }
}
