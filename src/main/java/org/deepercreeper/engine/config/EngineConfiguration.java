package org.deepercreeper.engine.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MotionConfiguration.class)
@ComponentScan("org.deepercreeper.engine")
public class EngineConfiguration {}
