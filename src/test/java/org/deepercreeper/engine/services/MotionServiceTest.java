package org.deepercreeper.engine.services;

import org.deepercreeper.engine.motion.strategies.AbstractMotionStrategy;
import org.deepercreeper.engine.physics.TestEntity;
import org.junit.Assert;
import org.junit.Test;

public class MotionServiceTest {
    @Test
    public void testSolidUpdate() {
        boolean[] invocations = new boolean[1];

        EntityService entityService = new EntityService();
        MotionService motionService = new MotionService(entityService, delta -> invocations[0] = true);

        entityService.add(new TestEntity());
        entityService.add(new TestEntity().setSolid(false));
        entityService.update(1);

        motionService.update(1);

        Assert.assertTrue(invocations[0]);
    }

    @Test
    public void testNonSolidUpdate() {
        EntityService entityService = new EntityService();
        MotionService motionService = new MotionService(entityService, new AbstractMotionStrategy() {
            @Override
            protected void update() {}
        });

        TestEntity solidEntity = new TestEntity().setXVelocity(1);
        TestEntity nonSolidEntity = new TestEntity().setXVelocity(1).setSolid(false);

        entityService.add(solidEntity);
        entityService.add(nonSolidEntity);
        entityService.update(1);

        motionService.update(1);

        Assert.assertEquals(1, nonSolidEntity.getX(), 0);
        Assert.assertEquals(0, solidEntity.getX(), 0);
    }

    @Test
    public void testPause() {
        boolean[] invocation = new boolean[1];

        EntityService entityService = new EntityService();
        MotionService motionService = new MotionService(entityService, new AbstractMotionStrategy() {
            @Override
            protected void update() {
                invocation[0] = true;
            }
        });

        entityService.add(new TestEntity());
        entityService.add(new TestEntity().setSolid(false));
        entityService.update(1);

        motionService.setPause(true);
        motionService.update(1);

        Assert.assertFalse(invocation[0]);
    }
}
