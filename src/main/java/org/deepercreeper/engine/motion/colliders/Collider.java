package org.deepercreeper.engine.motion.colliders;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public interface Collider
{
    void collide(Set<Entity<?>> entities, double delta);

    boolean changedVelocities();
}
