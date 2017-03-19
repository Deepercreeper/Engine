package org.deepercreeper.engine.services;

import org.deepercreeper.engine.physics.Force;
import org.deepercreeper.engine.geometry.position.Vector;
import org.junit.Assert;
import org.junit.Test;

public class PhysicsServiceTest
{
    @Test
    public void testForceAddition()
    {
        PhysicsService physicsService = new PhysicsService();
        physicsService.add(entity -> new Vector(0, 9.81 / entity.getMass()));

        Assert.assertTrue(physicsService.getForces().isEmpty());

        physicsService.update(1);

        Assert.assertFalse(physicsService.getForces().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddition()
    {
        PhysicsService physicsService = new PhysicsService();

        physicsService.getForces().add(entity -> new Vector(0, 9.81 / entity.getMass()));
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalRemoval()
    {
        PhysicsService physicsService = new PhysicsService();
        Force force = entity -> new Vector(0, 9.81 / entity.getMass());
        physicsService.add(force);

        physicsService.update(1);

        physicsService.getForces().remove(force);
    }
}
