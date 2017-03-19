package org.deepercreeper.engine.motion.strategies;

import org.deepercreeper.common.util.Util;
import org.deepercreeper.engine.motion.components.MotionComponent;
import org.deepercreeper.engine.physics.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ComponentStepsStrategy extends AbstractMotionStrategy {
    private final ExecutorService executorService = Executors.newFixedThreadPool(24);

    private final Set<MotionComponent> motionComponents = new HashSet<>();

    private final ApplicationContext applicationContext;

    @Autowired
    public ComponentStepsStrategy(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void update() {
        computeMotionComponents();
        moveMotionComponents();
    }

    private void computeMotionComponents() {
        motionComponents.clear();
        getEntities().forEach(this::findMotionComponent);
    }

    private void findMotionComponent(Entity<?> entity) {
        MotionComponent component = (MotionComponent) applicationContext.getBean("motionComponent");
        component.init(entity, getDelta());
        boolean changed;
        do {
            changed = addComponents(component);
        } while (changed);
        motionComponents.add(component);
    }

    private boolean addComponents(MotionComponent motionComponent) {
        boolean changed = false;
        Iterator<MotionComponent> iterator = motionComponents.iterator();
        while (iterator.hasNext()) {
            MotionComponent component = iterator.next();
            if (motionComponent.isTouching(component)) {
                motionComponent.consume(component);
                iterator.remove();
                changed = true;
            }
        }
        return changed;
    }

    private void moveMotionComponents() {
        CountDownLatch latch = new CountDownLatch(motionComponents.size());
        motionComponents.forEach(component -> component.setLatch(latch));
        motionComponents.forEach(executorService::execute);
        Util.await(latch);
    }
}
