package org.deepercreeper.engine.services;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.TestEntity;
import org.junit.Assert;
import org.junit.Test;

public class EntityServiceTest {
    @Test
    public void testSolidAddition() {
        EntityService engine = new EntityService();

        engine.add(new TestEntity());

        Assert.assertTrue(engine.getEntities().isEmpty());
        Assert.assertTrue(engine.getSolidEntities().isEmpty());

        engine.update(1);

        Assert.assertFalse(engine.getEntities().isEmpty());
        Assert.assertFalse(engine.getSolidEntities().isEmpty());
        Assert.assertTrue(engine.getNonSolidEntities().isEmpty());
    }

    @Test
    public void testNonSolidAddition() {
        EntityService engine = new EntityService();

        engine.add(new TestEntity().setSolid(false));

        Assert.assertTrue(engine.getEntities().isEmpty());
        Assert.assertTrue(engine.getNonSolidEntities().isEmpty());

        engine.update(1);

        Assert.assertFalse(engine.getEntities().isEmpty());
        Assert.assertFalse(engine.getNonSolidEntities().isEmpty());
        Assert.assertTrue(engine.getSolidEntities().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddition() {
        EntityService engine = new EntityService();
        engine.getEntities().add(new TestEntity());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalRemoval() {
        EntityService engine = new EntityService();
        Entity entity = new TestEntity();
        engine.add(entity);
        engine.update(1);

        engine.getEntities().remove(entity);
    }

    @Test
    public void testRemoval() {
        EntityService engine = new EntityService();
        Entity entity = new TestEntity();
        engine.add(entity);
        engine.update(1);

        entity.remove();

        Assert.assertFalse(engine.getEntities().isEmpty());

        engine.update(1);

        Assert.assertTrue(engine.getEntities().isEmpty());
    }
}
