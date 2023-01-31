package dev.alnat.tinylinkshortener.model;

/**
 * Marker for Entity that's cant be deleted and must be updated with active = false
 *
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface Activating {

    void setActive(boolean value);

    boolean isActive();

}
