package org.deepercreeper.engine.physics;

public class TestEntity extends Entity<TestEntity> {
    public TestEntity() {
        super(null);
    }

    public TestEntity(double x, double y) {
        this();
        setPosition(x, y);
    }

    public TestEntity(double x, double y, double width, double height) {
        this(x, y);
        setSize(width, height);
    }

    @Override
    protected TestEntity getThis() {
        return this;
    }
}
