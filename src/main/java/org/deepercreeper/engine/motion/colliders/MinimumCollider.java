package org.deepercreeper.engine.motion.colliders;

import org.deepercreeper.engine.annotations.PrototypeComponent;
import org.deepercreeper.engine.geometry.box.Box;
import org.deepercreeper.engine.motion.collisions.Collision;
import org.deepercreeper.engine.physics.Entity;

import java.util.*;

@PrototypeComponent
public class MinimumCollider implements Collider {
    private final Set<Collision> minUnknownCollisions = new HashSet<>();

    private final Set<Collision> unknownCollisions = new HashSet<>();

    private final Set<Collision> collisions = new HashSet<>();

    private final Set<Collision> knownCollisions = new HashSet<>();

    private Set<Entity<?>> entities;

    private double delta;

    private double minDelta;

    private boolean changedVelocities;

    @Override
    public boolean changedVelocities() {
        return changedVelocities;
    }

    @Override
    public void collide(Set<Entity<?>> entities, double delta) {
        this.entities = entities;
        this.delta = delta;
        changedVelocities = false;
        knownCollisions.clear();
        collide();
    }

    private void collide() {
        while (delta > 0) {
            computeCollisions();
            computeMinima();
            updateEntities();
            doCollisions();
            updateKnownCollisions();
            delta -= minDelta;
        }
    }

    private void computeCollisions() {
        collisions.clear();
        unknownCollisions.clear();
        List<Entity<?>> entities = new ArrayList<>(this.entities);
        Iterator<Entity<?>> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity<?> entity = iterator.next();
            iterator.remove();
            if (iterator.hasNext()) {
                computeCollisions(entity, entities);
            }
        }
    }

    private void computeCollisions(Entity<?> entity, List<Entity<?>> entities) {
        Box deltaBox = entity.getDeltaBox(delta);
        for (Entity<?> collisionEntity : entities) {
            boolean canTouch = entity.canTouch(collisionEntity) || collisionEntity.canTouch(entity);
            boolean isTouching = deltaBox.isTouching(collisionEntity.getDeltaBox(delta));
            if (canTouch && isTouching) {
                addCollision(entity, collisionEntity);
            }
        }
    }

    private void addCollision(Entity<?> firstEntity, Entity<?> secondEntity) {
        Collision collision = new Collision(firstEntity, secondEntity);
        if (!collision.isValid()) {
            return;
        }
        if (!isKnown(collision)) {
            unknownCollisions.add(collision);
        }
        collisions.add(collision);
        changedVelocities = true;
    }

    private void computeMinima() {
        minUnknownCollisions.clear();
        minDelta = delta;
        for (Collision collision : unknownCollisions) {
            if (collision.getDelta() < minDelta) {
                minUnknownCollisions.clear();
                minUnknownCollisions.add(collision);
                minDelta = collision.getDelta();
            }
            else if (collision.getDelta() == minDelta) {
                minUnknownCollisions.add(collision);
            }
        }
    }

    private void updateEntities() {
        for (Entity<?> entity : entities) {
            if (isColliding(entity)) {
                updateCollidingEntity(entity);
            }
            else {
                entity.updateAll(minDelta);
            }
        }
    }

    private void updateCollidingEntity(Entity<?> entity) {
        boolean accelerateHorizontal = isHorizontalUnknown(entity);
        boolean accelerateVertical = isVerticalUnknown(entity);

        if (accelerateHorizontal) {
            entity.moveXAccelerated(minDelta);
            entity.accelerateX(minDelta);
        }
        if (accelerateVertical) {
            entity.moveYAccelerated(minDelta);
            entity.accelerateY(minDelta);
        }
        entity.update(minDelta);
    }

    private boolean isHorizontalUnknown(Entity<?> entity) {
        boolean hasKnownCollision = false;
        for (Collision collision : knownCollisions) {
            if (collision.contains(entity) && collision.isHorizontal()) {
                hasKnownCollision = true;
                break;
            }
        }
        if (!hasKnownCollision) {
            return true;
        }
        for (Collision collision : unknownCollisions) {
            if (collision.contains(entity) && collision.isHorizontal()) {
                return true;
            }
        }
        return false;
    }

    private boolean isVerticalUnknown(Entity<?> entity) {
        boolean hasKnownCollision = false;
        for (Collision collision : knownCollisions) {
            if (collision.contains(entity) && !collision.isHorizontal()) {
                hasKnownCollision = true;
                break;
            }
        }
        if (!hasKnownCollision) {
            return true;
        }
        for (Collision collision : unknownCollisions) {
            if (collision.contains(entity) && !collision.isHorizontal()) {
                return true;
            }
        }
        return false;
    }

    private boolean isColliding(Entity<?> entity) {
        for (Collision collision : collisions) {
            if (collision.contains(entity)) {
                return true;
            }
        }
        return false;
    }

    private void doCollisions() {
        for (Collision collision : minUnknownCollisions) {
            collision.collide();
            if (collision.isWeak()) {
                knownCollisions.add(collision);
            }
        }
    }

    private boolean isKnown(Collision collision) {
        return collision.isWeak() && getKnownCollision(collision) != null;
    }

    private Collision getKnownCollision(Collision collision) {
        for (Collision knownCollision : knownCollisions) {
            if (knownCollision.equals(collision) && knownCollision.isHorizontal() == collision.isHorizontal()) {
                return knownCollision;
            }
        }
        return null;
    }

    private void updateKnownCollisions() {
        Set<Collision> knownCollisions = new HashSet<>();
        for (Collision collision : collisions) {
            Collision knownCollision = getKnownCollision(collision);
            if (knownCollision != null) {
                knownCollisions.add(knownCollision);
            }
        }
        this.knownCollisions.retainAll(knownCollisions);
    }
}
