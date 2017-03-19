package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.geometry.position.Vector;

public interface Force
{
    Vector of(Entity<?> entity);
}
