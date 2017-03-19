package org.deepercreeper.engine.services;

import org.deepercreeper.engine.display.DefaultRenderer;
import org.deepercreeper.engine.display.TestDisplay;
import org.deepercreeper.engine.physics.TestEntity;
import org.deepercreeper.engine.util.Image;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class RenderServiceTest
{
    @Test
    public void testRendering()
    {
        EntityService entityService = new EntityService();
        TestDisplay display = new TestDisplay(100, 100);
        RenderService renderService = new RenderService(entityService, new UpdateService(), new DefaultRenderer(display));

        renderService.update(1);

        Assert.assertEquals(0, display.getRenderings());
        Assert.assertEquals(0, display.getClears());

        entityService.add(new TestEntity(0, 0, 1, 11));

        entityService.update(1);
        renderService.update(1);

        Assert.assertEquals(1, display.getRenderings());
        Assert.assertEquals(0, display.getClears());

        renderService.update(1);

        Assert.assertEquals(2, display.getRenderings());
        Assert.assertEquals(0, display.getClears());
    }

    @Test
    public void testClearing()
    {
        EntityService entityService = new EntityService();
        TestDisplay display = new TestDisplay(100, 100);
        RenderService renderService = new RenderService(entityService, new UpdateService(), new DefaultRenderer(display));
        TestEntity entity = new TestEntity().setXVelocity(1).setWidth(1).setHeight(1);

        entityService.add(entity);

        entityService.update(1);
        renderService.update(1);

        entity.move(1);

        renderService.update(1);

        Assert.assertEquals(2, display.getRenderings());
        Assert.assertEquals(1, display.getClears());
    }

    @Test
    public void testImageGeneration()
    {
        EntityService entityService = new EntityService();
        TestDisplay display = new TestDisplay(100, 100);
        RenderService renderService = new RenderService(entityService, new UpdateService(), new DefaultRenderer(display));

        AtomicInteger generationCounter = new AtomicInteger();
        TestEntity entity = new TestEntity(0, 0, 1, 1)
        {
            @Override
            public Image generateImage(double scale)
            {
                generationCounter.incrementAndGet();
                return super.generateImage(scale);
            }
        };
        entity.setXVelocity(1);

        entityService.add(entity);

        entityService.update(1);
        renderService.update(1);

        Assert.assertEquals(1, generationCounter.get());

        entity.move(1);
        renderService.update(1);

        Assert.assertEquals(2, generationCounter.get());
    }
}
