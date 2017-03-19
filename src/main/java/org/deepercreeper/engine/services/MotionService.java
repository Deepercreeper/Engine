package org.deepercreeper.engine.services;

import org.deepercreeper.engine.motion.strategies.MotionStrategy;
import org.deepercreeper.engine.util.Updatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotionService implements Updatable {
    private final EntityService entityService;

    private final MotionStrategy motionStrategy;

    private double delta;

    private boolean pause;

    @Autowired
    public MotionService(EntityService entityService, MotionStrategy motionStrategy) {
        this.entityService = entityService;
        this.motionStrategy = motionStrategy;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void togglePause() {
        pause = !pause;
    }

    @Override
    public void update(double delta) {
        if (!pause) {
            this.delta = delta;
            updateNonSolidMotion();
            updateSolidMotion();
        }
    }

    private void updateNonSolidMotion() {
        entityService.getNonSolidEntities().forEach(entity -> entity.updateAll(delta));
    }

    private void updateSolidMotion() {
        motionStrategy.update(delta);
    }
}
