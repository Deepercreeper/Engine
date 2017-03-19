package org.deepercreeper.engine.display;

import org.deepercreeper.engine.geometry.position.Point;
import org.deepercreeper.engine.geometry.rectangle.Rectangle;
import org.deepercreeper.engine.util.Image;
import org.junit.Assert;
import org.junit.Test;

public class DefaultRendererTest {
    @Test
    public void testRender() {
        TestDisplay display = new TestDisplay(10, 10);
        DefaultRenderer renderer = new DefaultRenderer(display);

        renderer.render(new Image().setX(2).setWidth(6).setY(2).setHeight(6).setData(new int[36]));

        Assert.assertEquals(1, display.getRenderings());
    }

    @Test
    public void testInvisibleRender() {
        TestDisplay display = new TestDisplay(10, 10);
        DefaultRenderer renderer = new DefaultRenderer(display);

        renderer.render(new Image().setX(11).setWidth(4).setHeight(2).setData(new int[8]));

        Assert.assertEquals(0, display.getRenderings());
    }

    @Test
    public void testVisiblePositionRender() {
        TestDisplay display = new TestDisplay(10, 10);
        DefaultRenderer renderer = new DefaultRenderer(display);

        renderer.setPosition(new Point(-10, 0));

        renderer.render(new Image().setX(-8).setWidth(6).setY(2).setHeight(6).setData(new int[36]));

        Assert.assertEquals(1, display.getRenderings());
    }

    @Test
    public void testInvisiblePositionRender() {
        TestDisplay display = new TestDisplay(10, 10);
        DefaultRenderer renderer = new DefaultRenderer(display);

        renderer.setPosition(new Point(-10, 0));

        renderer.render(new Image().setX(2).setWidth(6).setY(2).setHeight(6).setData(new int[36]));

        Assert.assertEquals(0, display.getRenderings());
    }

    @Test
    public void testPartialRender() {
        TestDisplay display = new TestDisplay(10, 10);
        DefaultRenderer renderer = new DefaultRenderer(display);

        renderer.render(new Image().setX(5).setWidth(10).setY(5).setHeight(10).setData(new int[100]));

        Assert.assertEquals(1, display.getRenderings());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidImage() {
        TestDisplay display = new TestDisplay(10, 10);
        DefaultRenderer renderer = new DefaultRenderer(display);

        renderer.render(new Image().setX(-8).setWidth(6).setY(2).setHeight(6).setData(new int[30]));
    }

    @Test
    public void testVisibility() {
        TestDisplay display = new TestDisplay(10, 10);
        DefaultRenderer renderer = new DefaultRenderer(display);

        renderer.setPosition(new Point(-5, -5));

        Assert.assertTrue(renderer.isVisible(new Rectangle().setPosition(-9, -9).setSize(5, 5)));
        Assert.assertFalse(renderer.isVisible(new Rectangle().setPosition(-10, -10).setSize(5, 5)));
        Assert.assertTrue(renderer.isVisible(new Rectangle().setPosition(4, 4).setSize(5, 5)));
        Assert.assertFalse(renderer.isVisible(new Rectangle().setPosition(5, 5).setSize(5, 5)));
    }
}
