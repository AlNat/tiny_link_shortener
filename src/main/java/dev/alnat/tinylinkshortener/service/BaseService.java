package dev.alnat.tinylinkshortener.service;

import dev.alnat.tinylinkshortener.exceptions.NotFoundException;
import dev.alnat.tinylinkshortener.model.Model;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Basic CRUD operations for Entity
 * <p>
 * Created by @author AlNat on 14.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface BaseService<E extends Model<ID>, ID> {

    E create(E entity);

    E update(E entity, ID id) throws NotFoundException;

    default E update(E entity) throws NotFoundException {
        if (entity.getId() != null) {
            update(entity, entity.getId());
        }
        throw new IllegalArgumentException("Cant update entity with null ID!");
    }

    E getByID(ID id) throws NotFoundException;

    Optional<E> findByID(ID id);

    List<E> findPaginated(Pageable pageable);

    void delete(ID id) throws NotFoundException;

}
