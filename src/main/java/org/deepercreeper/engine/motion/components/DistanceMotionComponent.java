package org.deepercreeper.engine.motion.components;

import org.deepercreeper.engine.annotations.PrototypeComponent;
import org.deepercreeper.engine.motion.components.motion.ComponentMotion;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.geometry.position.Vector;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

@PrototypeComponent
public class DistanceMotionComponent implements MotionComponent {
    private final Set<Entity<?>> entities = new HashSet<>();

    private final ComponentMotion componentMotion;

    private final Vector momentum = new Vector();

    private final Vector velocity = new Vector();

    private CountDownLatch latch;

    private double delta;

    @Autowired
    public DistanceMotionComponent(ComponentMotion componentMotion) {
        this.componentMotion = componentMotion;
    }

    @Override
    public void init(Entity<?> entity, double delta) {
        this.delta = delta;
        add(entity);
    }

    @Override
    public void add(Entity<?> entity) {
        entities.add(entity);
        addMomentumAndVelocityOf(entity);
    }

    private void addMomentumAndVelocityOf(Entity<?> entity) {
        if (Double.isFinite(entity.getMass())) {
            this.momentum.add(entity.getVelocity(), entity.getMass());
        }
        else {
            velocity.add(entity.getVelocity().absolute());
        }
    }

    @Override
    public void consume(MotionComponent component) {
        DistanceMotionComponent distanceMotionComponent = (DistanceMotionComponent) component;
        entities.addAll(distanceMotionComponent.entities);
        addMomentumAndVelocityOf(distanceMotionComponent);
    }

    private void addMomentumAndVelocityOf(DistanceMotionComponent component) {
        momentum.add(component.momentum);
        velocity.add(component.velocity);
    }

    @Override
    public boolean isTouching(MotionComponent component) {
        DistanceMotionComponent motionComponent = (DistanceMotionComponent) component;
        for (Entity<?> entity : entities) {
            for (Entity<?> componentEntity : motionComponent.entities) {
                boolean canTouch = entity.canTouch(componentEntity) || componentEntity.canTouch(entity);
                boolean isTouching = entity.isDistanceTouching(getMaxVelocity(entity), componentEntity, motionComponent.getMaxVelocity(componentEntity), delta);
                if (canTouch && isTouching) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        if (entities.size() == 1) {
            moveSingle();
        }
        else {
            componentMotion.move(entities, delta);
        }
        latch.countDown();
        latch = null;
    }

    private void moveSingle() {
        Entity<?> entity = entities.iterator().next();
        entity.updateAll(delta);
    }

    Vector getMaxVelocity(Entity<?> entity) {
        if (Double.isFinite(entity.getMass())) {
            return momentum.times(2 / entity.getMass()).plus(velocity);
        }
        return entity.getVelocity();
    }

    Vector getVelocity() {
        return velocity;
    }

    Vector getMomentum() {
        return momentum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DistanceMotionComponent) {
            DistanceMotionComponent component = (DistanceMotionComponent) obj;
            return entities.equals(component.entities);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return entities.hashCode();
    }

    @Override
    public String toString() {
        return entities.toString();
    }
}
