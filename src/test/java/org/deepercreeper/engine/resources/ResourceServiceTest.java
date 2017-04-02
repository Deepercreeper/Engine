package org.deepercreeper.engine.resources;

import org.apache.commons.io.FileUtils;
import org.deepercreeper.engine.resources.types.StringResource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResourceServiceTest {
    private static final StringResource RESOURCE = new StringResource("org/deepercreeper/engine/resources/ResourceServiceTest.txt");

    @Test
    public void testLoading() {
        ResourceService resourceService = new ResourceService();
        write("Test loading");
        Assert.assertEquals("Test loading", resourceService.load(RESOURCE));
    }

    @Test
    public void testSaving() {
        ResourceService resourceService = new ResourceService();
        long timeStamp = System.currentTimeMillis();
        resourceService.save("Test saving " + timeStamp, RESOURCE);
        Assert.assertEquals("Test saving " + timeStamp, read());
    }

    @Test
    public void testLoadCaching() throws IOException {
        write("Test load caching");
        ResourceService resourceService = new ResourceService();
        resourceService.load(RESOURCE);
        write("Test load caching " + System.currentTimeMillis());
        Assert.assertEquals("Test load caching", resourceService.load(RESOURCE));
    }

    @Test
    public void testSaveCaching() {
        ResourceService resourceService = new ResourceService();
        resourceService.save("Test save caching", RESOURCE);
        write("Test save caching " + System.currentTimeMillis());
        Assert.assertEquals("Test save caching", resourceService.load(RESOURCE));
    }

    private void write(String data) {
        try {
            FileUtils.write(RESOURCE.getFile(), data, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not write data to file: " + RESOURCE.getFile(), e);
        }
    }

    private String read() {
        try {
            return FileUtils.readFileToString(RESOURCE.getFile(), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read data from file: " + RESOURCE.getFile(), e);
        }
    }
}
