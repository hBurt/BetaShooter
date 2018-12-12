package com.shooter.game.managers;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Created by: Harrison on 12 Dec 2018
 */
public class ActionManager extends Action {
    private int count;

    @Override
    public boolean act(float delta) {
        --count;
        return true;
    }

    public void setCount(int value) { count = value; }

    public boolean isComplete() { return count == 0; }
}
