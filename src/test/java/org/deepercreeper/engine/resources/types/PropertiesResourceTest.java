package org.deepercreeper.engine.resources.types;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class PropertiesResourceTest {
    private static final PropertiesResource RESOURCE = new PropertiesResource("org/deepercreeper/engine/resources/types/PropertiesResourceTest.properties");

    @Test
    public void testLoading() {
        Properties properties = RESOURCE.load();
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("firstValue", properties.getProperty("firstKey"));
        Assert.assertEquals("secondValue", properties.getProperty("secondKey"));
    }
}
