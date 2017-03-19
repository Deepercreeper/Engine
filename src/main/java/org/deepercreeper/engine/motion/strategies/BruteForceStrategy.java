package org.deepercreeper.engine.motion.strategies;


import org.deepercreeper.engine.motion.components.motion.ComponentMotion;

public class BruteForceStrategy extends AbstractMotionStrategy {
    private final ComponentMotion motion;

    public BruteForceStrategy(ComponentMotion motion) {
        this.motion = motion;
    }

    @Override
    protected void update() {
        motion.move(getEntities(), getDelta());
    }
}
