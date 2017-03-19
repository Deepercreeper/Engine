package org.deepercreeper.engine.motion.components.motion;

import org.deepercreeper.engine.annotations.PrototypeComponent;
import org.deepercreeper.engine.motion.colliders.Collider;
import org.deepercreeper.engine.motion.splitters.Splitter;
import org.deepercreeper.engine.physics.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

@PrototypeComponent
public class StepMotion implements ComponentMotion {
    private final Collider collider;

    private final Splitter splitter;

    @Value("${motion.stepMotion.maxStepDistance:.1}")
    private double maxStepDistance;

    private Set<Entity<?>> entities;

    private double delta;

    private double stepDelta;

    @Autowired
    public StepMotion(Collider collider, Splitter splitter) {
        this.collider = collider;
        this.splitter = splitter;
    }

    @Override
    public void move(Set<Entity<?>> entities, double delta) {
        this.entities = entities;
        this.delta = delta;
        move();
    }

    private void move() {
        computeStepDelta();
        while (delta > 0) {
            moveStep();
        }
        updateAccelerations();
    }

    private void moveStep() {
        collider.collide(entities, stepDelta);
        splitter.split(entities);
        delta -= stepDelta;
        if (collider.changedVelocities()) {
            computeStepDelta();
        }
    }

    private void computeStepDelta() {
        if (delta <= 0) {
            return;
        }
        computeMinDistanceDelta();
    }

    private void computeMinDistanceDelta() {
        double minDistanceDelta = -1;
        for (Entity<?> entity : entities) {
            double maxDistanceDelta = entity.getMaxDistanceDelta(delta, maxStepDistance);
            if (minDistanceDelta == -1 || maxDistanceDelta < minDistanceDelta) {
                minDistanceDelta = maxDistanceDelta;
            }
        }
        stepDelta = minDistanceDelta;
    }

    private void updateAccelerations() {
        entities.forEach(Entity::updateProperties);
    }
}
