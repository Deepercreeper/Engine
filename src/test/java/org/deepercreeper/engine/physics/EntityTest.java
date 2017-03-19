package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.geometry.box.Box;
import org.junit.Assert;
import org.junit.Test;

public class EntityTest {
    @Test
    public void testLinearMaxDistanceDelta() {
        TestEntity entity = new TestEntity();
        double delta;

        delta = entity.getMaxDistanceDelta(1, 1);
        Assert.assertEquals(0, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(1, delta, 0);

        entity.setXVelocity(1);

        delta = entity.getMaxDistanceDelta(1, 1);
        Assert.assertEquals(1, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(1, delta, 0);

        delta = entity.getMaxDistanceDelta(2, 2);
        Assert.assertEquals(2, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(2, delta, 0);

        entity.setSpeed(2);

        delta = entity.getMaxDistanceDelta(1, 2);
        Assert.assertEquals(2, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(1, delta, 0);

        delta = entity.getMaxDistanceDelta(2, 4);
        Assert.assertEquals(4, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(2, delta, 0);

        entity.setSpeed(1);
        entity.setXVelocity(.5);

        delta = entity.getMaxDistanceDelta(1, 1);
        Assert.assertEquals(.5, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(1, delta, 0);

        delta = entity.getMaxDistanceDelta(2, 1);
        Assert.assertEquals(1, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(2, delta, 0);

        entity.setSpeed(2);

        delta = entity.getMaxDistanceDelta(1, 1);
        Assert.assertEquals(1, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(1, delta, 0);

        delta = entity.getMaxDistanceDelta(2, 2);
        Assert.assertEquals(2, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(2, delta, 0);

        entity.setSpeed(1);
        entity.setVelocity(1, 1);

        delta = entity.getMaxDistanceDelta(1, 2);
        Assert.assertEquals(Math.sqrt(2), entity.getDistanceTo(delta), 0);
        Assert.assertEquals(1, delta, 0);

        delta = entity.getMaxDistanceDelta(2, 4);
        Assert.assertEquals(2 * Math.sqrt(2), entity.getDistanceTo(delta), 0);
        Assert.assertEquals(2, delta, 0);

        entity.setSpeed(2);

        delta = entity.getMaxDistanceDelta(1, 4);
        Assert.assertEquals(2 * Math.sqrt(2), entity.getDistanceTo(delta), 0);
        Assert.assertEquals(1, delta, 0);

        delta = entity.getMaxDistanceDelta(2, 8);
        Assert.assertEquals(4 * Math.sqrt(2), entity.getDistanceTo(delta), 0);
        Assert.assertEquals(2, delta, 0);
    }

    @Test
    public void testQuadraticMaxDistance() {
        TestEntity entity = new TestEntity().setXVelocity(1).setXAcceleration(1);
        double delta;

        delta = entity.getMaxDistanceDelta(1, 2);
        Assert.assertEquals(1.5, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(1, delta, 0);

        entity.setSpeed(2);

        delta = entity.getMaxDistanceDelta(1, 4);
        Assert.assertEquals(4, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(1, delta, 0);

        entity.setSpeed(1);
        entity.setXVelocity(1);
        entity.setXAcceleration(-2);

        delta = entity.getMaxDistanceDelta(1, 2);
        Assert.assertEquals(.25, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(.5, delta, 0);
        delta = entity.getMaxDistanceDelta(2, 2);
        Assert.assertEquals(.25, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(.5, delta, 0);

        entity.setSpeed(2);

        delta = entity.getMaxDistanceDelta(.5, 1);
        Assert.assertEquals(.25, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(.25, delta, 0);
        delta = entity.getMaxDistanceDelta(1, 2);
        Assert.assertEquals(.25, entity.getDistanceTo(delta), 0);
        Assert.assertEquals(.25, delta, 0);
    }

    @Test
    public void testInfiniteMassScale() {
        TestEntity firstEntity = new TestEntity();
        TestEntity secondEntity = new TestEntity();

        firstEntity.setMass(Double.POSITIVE_INFINITY);
        secondEntity.setMass(1);

        Assert.assertEquals(1, firstEntity.getMassScaleTo(secondEntity), 0);
        Assert.assertEquals(0, secondEntity.getMassScaleTo(firstEntity), 0);

        secondEntity.setMass(Double.POSITIVE_INFINITY);

        Assert.assertEquals(.5, firstEntity.getMassScaleTo(secondEntity), 0);
    }

    @Test
    public void testFiniteMassScale() {
        TestEntity firstEntity = new TestEntity();
        TestEntity secondEntity = new TestEntity();

        firstEntity.setMass(100);
        secondEntity.setMass(1);

        Assert.assertTrue(firstEntity.getMassScaleTo(secondEntity) > .5);
        Assert.assertTrue(secondEntity.getMassScaleTo(firstEntity) < .5);

        secondEntity.setMass(100);

        Assert.assertEquals(.5, firstEntity.getMassScaleTo(secondEntity), 0);
    }

    @Test
    public void testDeltaBox() {
        TestEntity entity = new TestEntity().setXVelocity(Math.random()).setX(Math.random()).setXAcceleration(Math.random());
        double delta = Math.random();
        Box deltaBox = entity.getDeltaBox(delta);

        entity.move(delta);

        Assert.assertEquals(entity.getX(), deltaBox.getX(), 0);
    }

    @Test
    public void testDeltaVelocity() {
        TestEntity entity = new TestEntity().setXVelocity(Math.random()).setX(Math.random()).setXAcceleration(Math.random());
        double delta = Math.random();
        double deltaVelocity = entity.getDeltaVelocity(delta).getX();

        entity.accelerateX(delta);

        Assert.assertEquals(entity.getXVelocity(), deltaVelocity, 0);
    }
}
