package org.deepercreeper.engine.motion.components;


import org.deepercreeper.engine.motion.components.motion.ComponentMotion;

public interface MotionComponentFactory {
    MotionComponent create(ComponentMotion componentMotion, double delta);
}
