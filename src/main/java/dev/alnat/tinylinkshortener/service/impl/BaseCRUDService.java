package dev.alnat.tinylinkshortener.service.impl;

import dev.alnat.tinylinkshortener.exceptions.NotFoundException;
import dev.alnat.tinylinkshortener.model.Activating;
import dev.alnat.tinylinkshortener.model.Model;
import dev.alnat.tinylinkshortener.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Base service to avoid boilerplate code for basic operation to entity
 * ProtoFramework, if its possible :)
 * <p>
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@RequiredArgsConstructor
@Transactional
public abstract class BaseCRUDService<E extends Model<ID>, ID> implements BaseService<E, ID> {

    protected final JpaRepository<E, ID> repository;


    @Override
    public E create(E entity) {
        return repository.save(entity);
    }

    @Override
    public E update(E entity, ID id) throws NotFoundException {
        getByID(id); // Check for existing

        if (entity.getId() == null || entity.getId() != id) {
            entity.setId(id);
        }
        return repository.save(entity);
    }


    @Override
    @Transactional(readOnly = true)
    public E getByID(ID id) throws NotFoundException {
        var entityOpt = findByID(id);

        if (entityOpt.isEmpty()) {
            throw new NotFoundException("Not found entity with id " + id);
        }

        return entityOpt.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<E> findByID(ID id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> findPaginated(Pageable pageable) {
        return repository.findAll();
    }

    @Override
    public void delete(ID id) throws NotFoundException {
        var entityOpt = findByID(id);

        if (entityOpt.isEmpty()) {
            throw new NotFoundException("Not found entity with id " + id);
        }

        var entity = entityOpt.get();

        if (entity instanceof Activating) {
            ((Activating) entity).setActive(Boolean.FALSE);
            update(entity, id);
            return;
        }

        repository.deleteById(id);
    }

}

