package org.deepercreeper.engine.physics;

import org.deepercreeper.common.util.Util;
import org.deepercreeper.engine.services.*;
import org.deepercreeper.engine.util.Updatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Engine implements Updatable
{
    private final CountDownLatch latch = new CountDownLatch(1);

    private final RenderService renderService;

    private final PhysicsService physicsService;

    private final UpdateService updateService;

    private final EntityService entityService;

    private final MotionService motionService;

    private final InputService inputService;

    @Autowired
    public Engine(RenderService renderService, PhysicsService physicsService, UpdateService updateService, EntityService entityService, MotionService motionService, InputService inputService)
    {
        this.renderService = renderService;
        this.physicsService = physicsService;
        this.updateService = updateService;
        this.entityService = entityService;
        this.motionService = motionService;
        this.inputService = inputService;
    }

    @Override
    public void update(double delta)
    {
        inputService.update(delta);
        physicsService.update(delta);
        entityService.update(delta);
        motionService.update(delta);
        renderService.update(delta);
    }

    public void await()
    {
        Util.await(latch);
    }

    public void add(Force force)
    {
        physicsService.add(force);
    }

    public void add(Entity<?> entity)
    {
        entityService.add(entity);
    }

    public void setFps(int fps)
    {
        updateService.setFps(fps);
    }

    public void setScale(double scale)
    {
        renderService.setScale(scale);
    }

    public void start()
    {
        updateService.update(this);
    }

    public void stop()
    {
        updateService.stop();
    }

    public void shutDown()
    {
        updateService.shutDown();
        latch.countDown();
    }
}
