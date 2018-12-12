package com.shooter.game.sprites.util;

import com.shooter.game.sprites.Player.WeaponType;

public class Weapon {

    private WeaponType weaponType;

    private int leftX;
    private int rightX;
    private int y;
    private int width;
    private int height;

    public Weapon(WeaponType weaponType, int leftX, int rightX, int yPlacement, int width, int height) {
        this.weaponType = weaponType;
        this.leftX = leftX;
        this.rightX = rightX;
        this.y = yPlacement;
        this.width = width;
        this.height = height;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }
}
