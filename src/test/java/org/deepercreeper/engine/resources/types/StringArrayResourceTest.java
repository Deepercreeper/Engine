package org.deepercreeper.engine.resources.types;

import org.junit.Assert;
import org.junit.Test;

public class StringArrayResourceTest {
    private static final StringArrayResource RESOURCE = new StringArrayResource("org/deepercreeper/engine/resources/types/StringArrayResourceTest.txt");

    @Test
    public void testLoading() {
        Assert.assertArrayEquals(new String[]{"First line", "Second line"}, RESOURCE.load());
    }
}
