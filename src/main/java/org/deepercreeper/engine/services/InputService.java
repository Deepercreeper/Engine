package org.deepercreeper.engine.services;

import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.util.Updatable;
import org.deepercreeper.engine.geometry.position.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InputService implements Updatable
{
    private final MotionService motionService;

    private final RenderService renderService;

    private final Input input;

    @Autowired
    public InputService(MotionService motionService, RenderService renderService, Input input)
    {
        this.motionService = motionService;
        this.renderService = renderService;
        this.input = input;
    }

    @Override
    public void update(double delta)
    {
        if (input != null)
        {
            updateInput();
        }
    }

    private void updateInput()
    {
        if (input.checkHit(Key.PAUSE))
        {
            motionService.togglePause();
        }
        if (input.checkHit(Key.FRAME_RATE))
        {
            renderService.toggleFrameRate();
        }
        Vector position = renderService.getPosition();
        if (input.isActive(Key.CAMERA_RIGHT))
        {
            position.add(1, 0);
        }
        if (input.isActive(Key.CAMERA_LEFT))
        {
            position.add(-1, 0);
        }
        if (input.isActive(Key.CAMERA_UP))
        {
            position.add(0, -1);
        }
        if (input.isActive(Key.CAMERA_DOWN))
        {
            position.add(0, 1);
        }
        renderService.setPosition(position);
    }
}
