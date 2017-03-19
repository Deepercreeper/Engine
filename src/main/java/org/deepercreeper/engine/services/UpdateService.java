package org.deepercreeper.engine.services;

import org.deepercreeper.common.util.Util;
import org.deepercreeper.engine.util.Updatable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class UpdateService implements Runnable
{
    private final CountDownLatch latch = new CountDownLatch(1);

    private final Object lock = new Object();

    private boolean running = true;

    private boolean updating;

    private double speed = 1;

    private long difference;

    private long lastExecution = -1;

    private int fps = 60;

    private Updatable updatable;

    public UpdateService()
    {
        new Thread(this, "Updater-thread").start();
    }

    public void update(Updatable updatable)
    {
        synchronized (lock)
        {
            this.updatable = updatable;
        }
        updating = updatable != null;
    }

    @Override
    public void run()
    {
        while (running)
        {
            synchronized (lock)
            {
                if (updating)
                {
                    update();
                }
                else
                {
                    lastExecution = -1;
                    Util.sleep(1);
                }
            }
        }
        latch.countDown();
    }

    private void update()
    {
        if (lastExecution == -1)
        {
            lastExecution = System.currentTimeMillis();
        }
        difference = System.currentTimeMillis() - lastExecution;
        Util.sleep(Math.max(0, 1000 / fps - difference));
        lastExecution = System.currentTimeMillis();
        updatable.update(speed / fps);
    }

    public long getDifference()
    {
        return difference;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    public void stop()
    {
        updating = false;
    }

    public void setFps(int fps)
    {
        this.fps = fps;
    }

    public void shutDown()
    {
        running = false;
        Util.await(latch);
    }
}
