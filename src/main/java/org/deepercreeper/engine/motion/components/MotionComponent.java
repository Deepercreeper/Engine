package org.deepercreeper.engine.motion.components;

import org.deepercreeper.engine.physics.Entity;

import java.util.concurrent.CountDownLatch;

public interface MotionComponent extends Runnable
{
    void init(Entity<?> entity, double delta);

    void add(Entity<?> entity);

    void consume(MotionComponent component);

    boolean isTouching(MotionComponent component);

    void setLatch(CountDownLatch latch);
}