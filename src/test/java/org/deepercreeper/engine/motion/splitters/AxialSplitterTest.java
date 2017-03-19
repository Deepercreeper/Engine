package org.deepercreeper.engine.motion.splitters;

import org.deepercreeper.engine.physics.TestEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class AxialSplitterTest
{
    private final double epsilon = .125;

    @Test
    public void testHorizontalSplit()
    {
        Splitter splitter = new AxialSplitter(epsilon);
        TestEntity firstEntity = new TestEntity().setWidth(1).setHeight(1);
        TestEntity secondEntity = new TestEntity().setX(.5).setWidth(1).setHeight(1);

        splitter.split(new HashSet<>(Arrays.asList(firstEntity, secondEntity)));

        Assert.assertFalse(firstEntity.isTouching(secondEntity));
        Assert.assertEquals(epsilon, firstEntity.getXDistanceTo(secondEntity), 0);
        Assert.assertEquals(0, firstEntity.getYDistanceTo(secondEntity), 0);
    }

    @Test
    public void testVerticalSplit()
    {
        Splitter splitter = new AxialSplitter(epsilon);
        TestEntity firstEntity = new TestEntity().setWidth(1).setHeight(1);
        TestEntity secondEntity = new TestEntity().setY(.5).setWidth(1).setHeight(1);

        splitter.split(new HashSet<>(Arrays.asList(firstEntity, secondEntity)));

        Assert.assertFalse(firstEntity.isTouching(secondEntity));
        Assert.assertEquals(epsilon, firstEntity.getYDistanceTo(secondEntity), 0);
        Assert.assertEquals(0, firstEntity.getXDistanceTo(secondEntity), 0);
    }

    @Test
    public void testInfiniteMassSplit()
    {
        Splitter splitter = new AxialSplitter(epsilon);
        TestEntity firstEntity = new TestEntity().setX(-9).setWidth(10).setHeight(10);
        TestEntity secondEntity = new TestEntity().setWidth(10).setHeight(10);

        firstEntity.setMass(Double.POSITIVE_INFINITY);
        secondEntity.setMass(1);

        splitter.split(new HashSet<>(Arrays.asList(firstEntity, secondEntity)));

        Assert.assertEquals(1, firstEntity.getMaxX(), 0);
        Assert.assertEquals(1, secondEntity.getX(), epsilon);

        secondEntity.setX(0);
        secondEntity.setMass(Double.POSITIVE_INFINITY);

        splitter.split(new HashSet<>(Arrays.asList(firstEntity, secondEntity)));

        Assert.assertEquals(.5, firstEntity.getMaxX(), epsilon);
        Assert.assertEquals(.5, secondEntity.getX(), epsilon);
    }

    @Test
    public void testFiniteMassSplit()
    {
        Splitter splitter = new AxialSplitter(epsilon);
        TestEntity firstEntity = new TestEntity().setX(-9).setWidth(10).setHeight(10);
        TestEntity secondEntity = new TestEntity().setWidth(10).setHeight(10);

        firstEntity.setMass(100);
        secondEntity.setMass(1);

        splitter.split(new HashSet<>(Arrays.asList(firstEntity, secondEntity)));

        Assert.assertTrue(firstEntity.getMaxX() > .5);
        Assert.assertTrue(secondEntity.getX() > .5);

        firstEntity.setMaxX(1);
        secondEntity.setX(0);
        secondEntity.setMass(100);

        splitter.split(new HashSet<>(Arrays.asList(firstEntity, secondEntity)));

        Assert.assertEquals(.5, firstEntity.getMaxX(), epsilon);
        Assert.assertEquals(.5, secondEntity.getX(), epsilon);
    }
}
