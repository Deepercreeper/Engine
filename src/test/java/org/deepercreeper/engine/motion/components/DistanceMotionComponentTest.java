package org.deepercreeper.engine.motion.components;

import org.deepercreeper.engine.physics.TestEntity;
import org.deepercreeper.engine.geometry.position.Vector;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class DistanceMotionComponentTest {
    @Test
    public void testFiniteAddition() {
        DistanceMotionComponent motionComponent = new DistanceMotionComponent((entities, delta) -> {});

        motionComponent.add(new TestEntity().setXVelocity(2).setMass(.5));

        Assert.assertEquals(new Vector(1, 0), motionComponent.getMomentum());
        Assert.assertEquals(new Vector(0, 0), motionComponent.getVelocity());
    }

    @Test
    public void testInfiniteAddition() {
        DistanceMotionComponent motionComponent = new DistanceMotionComponent((entities, delta) -> {});

        motionComponent.add(new TestEntity().setXVelocity(1).setMass(Double.POSITIVE_INFINITY));

        Assert.assertEquals(new Vector(0, 0), motionComponent.getMomentum());
        Assert.assertEquals(new Vector(1, 0), motionComponent.getVelocity());
    }

    @Test
    public void testMaxVelocity() {
        DistanceMotionComponent motionComponent = new DistanceMotionComponent((entities, delta) -> {});

        TestEntity finiteEntity = new TestEntity().setXVelocity(2).setMass(.5);
        TestEntity infiniteEntity = new TestEntity().setXVelocity(1).setMass(Double.POSITIVE_INFINITY);

        motionComponent.add(finiteEntity);
        motionComponent.add(infiniteEntity);

        Assert.assertEquals(new Vector(5, 0), motionComponent.getMaxVelocity(finiteEntity));
        Assert.assertEquals(new Vector(1, 0), motionComponent.getMaxVelocity(infiniteEntity));
    }

    @Test
    public void testConsumption() {
        DistanceMotionComponent finiteComponent = new DistanceMotionComponent((entities, delta) -> {});
        DistanceMotionComponent infiniteComponent = new DistanceMotionComponent((entities, delta) -> {});

        finiteComponent.add(new TestEntity().setXVelocity(2).setMass(.5));
        infiniteComponent.add(new TestEntity().setXVelocity(1).setMass(Double.POSITIVE_INFINITY));

        finiteComponent.consume(infiniteComponent);

        Assert.assertEquals(new Vector(1, 0), finiteComponent.getMomentum());
        Assert.assertEquals(new Vector(1, 0), finiteComponent.getVelocity());
    }

    @Test
    public void testSingleMove() {
        DistanceMotionComponent motionComponent = new DistanceMotionComponent((entities, delta) -> {});

        TestEntity entity = new TestEntity().setXVelocity(1);

        CountDownLatch latch = new CountDownLatch(1);

        motionComponent.init(entity, 1);
        motionComponent.setLatch(latch);
        motionComponent.run();

        Assert.assertEquals(1, entity.getX(), 0);
        Assert.assertEquals(0, latch.getCount());
    }

    @Test
    public void testMultipleMove() {
        CountDownLatch latch = new CountDownLatch(2);
        DistanceMotionComponent motionComponent = new DistanceMotionComponent((entities, delta) -> latch.countDown());

        motionComponent.init(new TestEntity().setXVelocity(1).setWidth(1), 1);
        motionComponent.add(new TestEntity().setX(2).setWidth(1).setXVelocity(-1));

        motionComponent.setLatch(latch);
        motionComponent.run();

        Assert.assertEquals(0, latch.getCount());
    }
}
