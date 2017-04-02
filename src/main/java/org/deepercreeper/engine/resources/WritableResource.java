package org.deepercreeper.engine.resources;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.OutputStream;

public abstract class WritableResource<T> extends Resource<T> {
    public WritableResource(@NotNull Class<T> type, @NotNull String name) {
        super(type, name);
    }

    @Override
    protected void checkFile() {
        super.checkFile();
        if (!getFile().canWrite()) {
            throw new RuntimeException("File is not writable: " + getFile());
        }
    }

    public void save(T value) {
        OutputStream stream;
        try {
            stream = new FileOutputStream(getFile());
            save(value, stream);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not save file: " + getFile(), e);
        }
        IOUtils.closeQuietly(stream);
    }

    protected abstract void save(@NotNull T value, @NotNull OutputStream stream) throws Exception;
}
