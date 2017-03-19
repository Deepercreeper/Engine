package org.deepercreeper.engine.services;

import org.deepercreeper.common.util.Util;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class UpdateServiceTest
{
    @Test
    public void testUpdate()
    {
        AtomicInteger updates = new AtomicInteger();
        UpdateService updateService = new UpdateService();

        updateService.setFps(10);
        updateService.update(delta -> updates.incrementAndGet());

        Util.sleep(1050);

        updateService.stop();

        Assert.assertEquals(10, updates.get());
    }

    @Test
    public void testSum()
    {
        double[] deltaSum = new double[1];
        UpdateService updateService = new UpdateService();

        updateService.setFps(10);
        updateService.update(delta -> deltaSum[0] += delta);

        Util.sleep(1050);

        updateService.stop();

        Assert.assertEquals(1, deltaSum[0], 10E-10);
    }

    @Test
    public void testSpeed()
    {
        double[] deltaSum = new double[1];
        UpdateService updateService = new UpdateService();

        updateService.setFps(10);
        updateService.setSpeed(2);
        updateService.update(delta -> deltaSum[0] += delta);

        Util.sleep(1050);

        updateService.stop();

        Assert.assertEquals(2, deltaSum[0], 10E-10);
    }
}
