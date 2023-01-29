package dev.alnat.tinylinkshortener.model;

/**
 * Marker for entity class
 *
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface Model<ID> {

    ID getId();
    void setId(ID id);

}
