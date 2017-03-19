package org.deepercreeper.engine.annotations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public @interface PrototypeComponent {}
