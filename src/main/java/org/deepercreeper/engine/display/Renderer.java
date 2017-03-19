package org.deepercreeper.engine.display;

import org.deepercreeper.engine.geometry.position.Point;
import org.deepercreeper.engine.geometry.rectangle.AbstractRectangle;
import org.deepercreeper.engine.util.Image;

public interface Renderer {
    void render(Image image);

    void clear(AbstractRectangle<?> rectangle);

    void clear();

    void setPosition(Point position);

    boolean isVisible(AbstractRectangle<?> rectangle);
}
