package org.deepercreeper.engine.util;

import org.deepercreeper.engine.geometry.rectangle.AbstractRectangle;
import org.deepercreeper.engine.geometry.rectangle.Rectangle;

public class Image extends AbstractRectangle<Image> {
    private int[] data;

    public final Image setData(int[] data) {
        this.data = data;
        return this;
    }

    public final int[] getData() {
        return data;
    }

    @Override
    protected Image getThis() {
        return this;
    }

    public final void validate() {
        if (data == null || data.length != getWidth() * getHeight()) {
            throw new IllegalStateException("Data length of image has to be width times height");
        }
    }

    public final void drawOver(Image image) {
        validate();
        image.validate();
        Rectangle cut = getCut(image);
        if (cut.isEmpty()) {
            return;
        }
        int[] imageData = image.getData();
        for (int y = 0; y < cut.getHeight(); y++) {
            int dataIndex = (y + cut.getY() - getY()) * getWidth() + cut.getX() - getX();
            int imageDataIndex = (y + cut.getY() - image.getY()) * image.getWidth() + cut.getX() - image.getX();
            System.arraycopy(data, dataIndex, imageData, imageDataIndex, cut.getWidth());
        }
    }
}
