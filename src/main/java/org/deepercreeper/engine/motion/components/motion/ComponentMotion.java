package org.deepercreeper.engine.motion.components.motion;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public interface ComponentMotion
{
    void move(Set<Entity<?>> entities, double delta);
}
