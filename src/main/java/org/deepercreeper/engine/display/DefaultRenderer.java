package org.deepercreeper.engine.display;

import org.deepercreeper.engine.geometry.position.Point;
import org.deepercreeper.engine.geometry.rectangle.AbstractRectangle;
import org.deepercreeper.engine.geometry.rectangle.Rectangle;
import org.deepercreeper.engine.util.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultRenderer implements Renderer {
    private final Rectangle rectangle = new Rectangle();

    private final Point position = new Point();

    private final Display display;

    @Autowired
    public DefaultRenderer(Display display) {
        this.display = display;
    }

    @Override
    public void render(Image image) {
        if (display == null) {
            return;
        }
        image.validate();
        Rectangle cut = getRectangle().getCut(image);
        if (cut.isEmpty()) {
            return;
        }
        Image visibleImage = new Image().set(cut).setData(Display.createFilledRectangle(cut.getWidth(), cut.getHeight(), 0));
        image.drawOver(visibleImage);
        visibleImage.getPosition().subtract(position);
        display.render(visibleImage.getX(), visibleImage.getY(), visibleImage.getWidth(), visibleImage.getHeight(), visibleImage.getData());
    }

    @Override
    public void clear(AbstractRectangle<?> rectangle) {
        if (display == null) {
            return;
        }
        Rectangle visibleRectangle = getRectangle().getCut(rectangle);
        if (visibleRectangle.isEmpty()) {
            return;
        }
        visibleRectangle.getPosition().subtract(position);
        display.clear(visibleRectangle.getX(), visibleRectangle.getY(), visibleRectangle.getWidth(), visibleRectangle.getHeight());
    }

    @Override
    public void clear() {
        display.clear(0, 0, display.getWidth(), display.getHeight());
    }

    public void setPosition(Point position) {
        this.position.set(position);
    }

    private Rectangle getRectangle() {
        rectangle.setPosition(position);
        rectangle.setSize(display.getWidth(), display.getHeight());
        return rectangle;
    }

    @Override
    public boolean isVisible(AbstractRectangle<?> rectangle) {
        return !getRectangle().getCut(rectangle).isEmpty();
    }
}