package org.deepercreeper.engine.motion.strategies;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.services.EntityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public abstract class AbstractMotionStrategy implements MotionStrategy {
    private EntityService entityService;

    private double delta;

    @Autowired
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public final Set<Entity<?>> getEntities() {
        return entityService.getSolidEntities();
    }

    public final double getDelta() {
        return delta;
    }

    @Override
    public final void update(double delta) {
        this.delta = delta;
        update();
    }

    protected abstract void update();
}
