package org.deepercreeper.engine.geometry.rectangle;

import org.deepercreeper.engine.geometry.position.Point;
import org.deepercreeper.engine.util.Generic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractRectangle<T extends AbstractRectangle<T>> extends Generic<T> {
    private final Point position = new Point(this::updateCenterAndHashCode);

    private final Point size = new Point(this::updateCenterAndHashCode);

    private final Point center = new Point();

    private int hashCode;

    public final T setX(int x) {
        getPosition().setX(x);
        return getThis();
    }

    public final T setX(double x) {
        getPosition().setX(x);
        return getThis();
    }

    public final T setY(int y) {
        getPosition().setY(y);
        return getThis();
    }

    public final T setY(double y) {
        getPosition().setY(y);
        return getThis();
    }

    public final T setPosition(int x, int y) {
        getPosition().set(x, y);
        return getThis();
    }

    public final T setPosition(double x, double y) {
        getPosition().set(x, y);
        return getThis();
    }

    public final T setPosition(Point position) {
        getPosition().set(position);
        return getThis();
    }

    public final T setWidth(int width) {
        getSize().setX(width);
        return getThis();
    }

    public final T setWidth(double width) {
        getSize().setX(width);
        return getThis();
    }

    public final T setHeight(int height) {
        getSize().setY(height);
        return getThis();
    }

    public final T setHeight(double height) {
        getSize().setY(height);
        return getThis();
    }

    public final T setSize(int width, int height) {
        getSize().set(width, height);
        return getThis();
    }

    public final T setSize(double width, double height) {
        getSize().set(width, height);
        return getThis();
    }

    public final T setSize(Point size) {
        getSize().set(size);
        return getThis();
    }

    public final T set(AbstractRectangle<?> rectangle) {
        setPosition(rectangle.getPosition());
        setSize(rectangle.getSize());
        return getThis();
    }

    public final T setCenter(double x, double y) {
        getPosition().set(x - getWidth() * .5, y - getHeight() * .5);
        return getThis();
    }

    public final T setCenter(int x, int y) {
        getPosition().set(x - getWidth() * .5, y - getHeight() * .5);
        return getThis();
    }

    public final T setCenter(Point center) {
        getPosition().set(center.getX() - getWidth() * .5, center.getY() - getHeight() * .5);
        return getThis();
    }

    public final T setMaxX(double x) {
        setX(x - getWidth());
        return getThis();
    }

    public final T setMaxX(int x) {
        setX(x - getWidth());
        return getThis();
    }

    public final T setMaxY(double y) {
        setY(y - getHeight());
        return getThis();
    }

    public final T setMaxY(int y) {
        setY(y - getHeight());
        return getThis();
    }

    public final T setCenterX(double x) {
        setX(x - getWidth() * .5);
        return getThis();
    }

    public final T setCenterX(int x) {
        setX(x - getWidth() * .5);
        return getThis();
    }

    public final T setCenterY(double y) {
        setY(y - getHeight() * .5);
        return getThis();
    }

    public final T setCenterY(int y) {
        setY(y - getHeight() * .5);
        return getThis();
    }

    public final T moveBy(double x, double y) {
        getPosition().add(x, y);
        return getThis();
    }

    public final T moveBy(int x, int y) {
        getPosition().add(x, y);
        return getThis();
    }

    public final T moveBy(Point point) {
        getPosition().add(point);
        return getThis();
    }

    public final Point getPosition() {
        return position;
    }

    public final Point getCenter() {
        return center;
    }

    public final Point getSize() {
        return size;
    }

    public final Rectangle shift(Point point) {
        return new Rectangle().setPosition(getX() + point.getX(), getY() + point.getY()).setSize(getSize());
    }

    public final int getX() {
        return getPosition().getX();
    }

    public final int getY() {
        return getPosition().getY();
    }

    public final int getWidth() {
        return getSize().getX();
    }

    public final int getHeight() {
        return getSize().getY();
    }

    public final int getMaxX() {
        return getX() + getWidth() - 1;
    }

    public final int getMaxY() {
        return getY() + getHeight() - 1;
    }

    public final int getCenterX() {
        return getCenter().getX();
    }

    public final int getCenterY() {
        return getCenter().getY();
    }

    public final boolean isEmpty() {
        return getWidth() == 0 || getHeight() == 0;
    }

    public final boolean isTouching(AbstractRectangle<?> rectangle) {
        return rectangle.getX() <= getMaxX() && getX() <= rectangle.getMaxX() && rectangle.getY() <= getMaxY() && getY() <= rectangle.getMaxY();
    }

    public final Rectangle getCut(AbstractRectangle<?> rectangle) {
        if (!isTouching(rectangle)) {
            return new Rectangle();
        }
        Rectangle cut = new Rectangle();
        cut.setPosition(Math.max(getX(), rectangle.getX()), Math.max(getY(), rectangle.getY()));
        cut.setSize(Math.min(getMaxX(), rectangle.getMaxX()) - cut.getX() + 1, Math.min(getMaxY(), rectangle.getMaxY()) - cut.getY() + 1);
        return cut;
    }

    public final Rectangle getContainment(AbstractRectangle<?> rectangle) {
        Rectangle containment = new Rectangle();
        containment.setPosition(Math.min(getX(), rectangle.getX()), Math.min(getY(), rectangle.getY()));
        containment.setSize(Math.max(getMaxX(), rectangle.getMaxX()) - containment.getX() + 1, Math.max(getMaxY(), rectangle.getMaxY()) - containment.getY() + 1);
        return containment;
    }

    public final Set<Rectangle> getSubtraction(AbstractRectangle<?> rectangle) {
        if (!isTouching(rectangle)) {
            return Collections.singleton(new Rectangle().set(this));
        }
        if (rectangle.contains(this)) {
            return Collections.emptySet();
        }
        Set<Rectangle> subtraction = new HashSet<>();
        Rectangle component;
        component = new Rectangle().set(this).setHeight(Math.max(0, rectangle.getY() - getY()));
        if (!component.isEmpty()) {
            subtraction.add(component);
        }
        component = new Rectangle().set(this).setY(rectangle.getMaxY() + 1).setHeight(Math.max(0, getMaxY() - rectangle.getMaxY()));
        if (!component.isEmpty()) {
            subtraction.add(component);
        }
        component = new Rectangle().set(this).setWidth(Math.max(0, rectangle.getX() - getX()));
        if (!component.isEmpty()) {
            subtraction.add(component);
        }
        component = new Rectangle().set(this).setX(rectangle.getMaxX() + 1).setWidth(Math.max(0, getMaxX() - rectangle.getMaxX()));
        if (!component.isEmpty()) {
            subtraction.add(component);
        }
        return subtraction;
    }

    public final boolean contains(AbstractRectangle<?> rectangle) {
        return getX() <= rectangle.getX() && getY() <= rectangle.getY() && getMaxX() >= rectangle.getMaxX() && getMaxY() >= rectangle.getMaxY();
    }

    private void updateCenterAndHashCode() {
        getCenter().set(getX() + getWidth() * .5, getY() + getHeight() * .5);
        updateHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractRectangle<?>) {
            AbstractRectangle<?> rectangle = (AbstractRectangle<?>) obj;
            return getPosition().equals(rectangle.getPosition()) && getSize().equals(rectangle.getSize());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

    protected final void updateHashCode() {
        hashCode = computeHashCode();
    }

    protected int computeHashCode() {
        int hashCode = getX();
        hashCode = hashCode * 13 + getY();
        hashCode = hashCode * 13 + getWidth();
        hashCode = hashCode * 13 + getHeight();
        return hashCode;
    }

    @Override
    public String toString() {
        return "(" + getPosition() + ", " + getSize() + ")";
    }
}
