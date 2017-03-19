package org.deepercreeper.engine.util;

public abstract class Generic<T extends Generic<T>> {
    protected abstract T getThis();
}
