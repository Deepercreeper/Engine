package org.deepercreeper.engine.services;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.display.Renderer;
import org.deepercreeper.engine.geometry.box.AbstractBox;
import org.deepercreeper.engine.geometry.position.Vector;
import org.deepercreeper.engine.geometry.rectangle.AbstractRectangle;
import org.deepercreeper.engine.geometry.rectangle.Rectangle;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Image;
import org.deepercreeper.engine.util.Updatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class RenderService implements Updatable {
    private final Map<Entity<?>, AbstractRectangle<?>> clearBoxes = new HashMap<>();

    private final Map<Entity<?>, Image> images = new HashMap<>();

    private final Vector position = new Vector();

    private final EntityService entityService;

    private final UpdateService updateService;

    private final Renderer renderer;

    private boolean frameRate = false;

    private double scale = 48;

    private int counter = 0;

    @Autowired
    public RenderService(EntityService entityService, UpdateService updateService, Renderer renderer) {
        this.entityService = entityService;
        this.updateService = updateService;
        this.renderer = renderer;
    }

    public void setPosition(Vector position) {
        if (this.position.equals(position)) {
            return;
        }
        this.position.set(position);
        renderer.setPosition(position.asPoint());
        renderer.clear();
    }

    public Vector getPosition() {
        return new Vector(position);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setFrameRate(boolean frameRate) {
        this.frameRate = frameRate;
    }

    public void toggleFrameRate() {
        frameRate = !frameRate;
    }

    public boolean isVisible(AbstractBox<?> box) {
        return renderer.isVisible(box.asScaledRectangle(scale));
    }

    public double getScale() {
        return scale;
    }

    @Override
    public void update(double delta) {
        computeImages();
        clearEntities();
        renderEntities();
        renderFrameRate();
    }

    private void computeImages() {
        images.clear();
        for (Entity<?> entity : entityService.getEntities()) {
            images.put(entity, entity.generateImage(scale));
        }
    }

    private void clearEntities() {
        Iterator<Map.Entry<Entity<?>, AbstractRectangle<?>>> iterator = clearBoxes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Entity<?>, AbstractRectangle<?>> entry = iterator.next();
            Entity<?> entity = entry.getKey();
            AbstractRectangle<?> renderBox = images.get(entity);
            if (renderBox != null) {
                for (Rectangle subtraction : entry.getValue().getSubtraction(renderBox)) {
                    renderer.clear(subtraction);
                }
            }
            else {
                renderer.clear(entry.getValue());
            }
            iterator.remove();
        }
        clearBoxes.putAll(images);
    }

    private void renderEntities() {
        for (Image image : images.values()) {
            renderer.render(image);
        }
    }

    private void renderFrameRate() {
        if (!frameRate) {
            return;
        }
        int difference = (int) updateService.getDifference();
        if (difference > 0) {
            Image image = new Image().setPosition(counter, 100 - difference).setSize(1, difference).setData(Display.createRectangle(1, difference, 0xffffffff));
            image.moveBy(position.asPoint());
            renderer.clear(new Rectangle().set(image).setY(position.asPoint().getY()).setHeight(100));
            renderer.render(image);
        }
        counter = (counter + 1) % 100;
    }
}
