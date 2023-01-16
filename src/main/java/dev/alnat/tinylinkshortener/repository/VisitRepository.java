package dev.alnat.tinylinkshortener.repository;

import dev.alnat.tinylinkshortener.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long>, VisitSearchRepository {

}
