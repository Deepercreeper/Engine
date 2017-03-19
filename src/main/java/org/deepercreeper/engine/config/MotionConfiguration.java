package org.deepercreeper.engine.config;

import org.deepercreeper.common.util.Util;
import org.deepercreeper.engine.motion.colliders.Collider;
import org.deepercreeper.engine.motion.components.MotionComponent;
import org.deepercreeper.engine.motion.components.motion.ComponentMotion;
import org.deepercreeper.engine.motion.splitters.Splitter;
import org.deepercreeper.engine.motion.strategies.MotionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan("org.deepercreeper.engine.motion")
public class MotionConfiguration {
    @Bean
    public MotionStrategy motionStrategy(@Value("${motion.strategyClass}") String motionStrategyClassName, ApplicationContext applicationContext) {
        return applicationContext.getBean(Util.getClass(motionStrategyClassName, MotionStrategy.class));
    }

    @Bean
    @Scope("prototype")
    public MotionComponent motionComponent(@Value("${motion.componentClass}") String motionComponentClassName, ApplicationContext applicationContext) {
        return applicationContext.getBean(Util.getClass(motionComponentClassName, MotionComponent.class));
    }

    @Bean
    @Scope("prototype")
    public ComponentMotion componentMotion(@Value("${motion.motionClass}") String componentMotionClassName, ApplicationContext applicationContext) {
        return applicationContext.getBean(Util.getClass(componentMotionClassName, ComponentMotion.class));
    }

    @Bean
    @Scope("prototype")
    public Collider collider(@Value("${motion.stepMotion.colliderClass}") String colliderClassName, ApplicationContext applicationContext) {
        return applicationContext.getBean(Util.getClass(colliderClassName, Collider.class));
    }

    @Bean
    @Scope("prototype")
    public Splitter splitter(@Value("${motion.stepMotion.splitterClass}") String splitterClassName, ApplicationContext applicationContext) {
        return applicationContext.getBean(Util.getClass(splitterClassName, Splitter.class));
    }
}
