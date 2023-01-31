package dev.alnat.tinylinkshortener.model;

import dev.alnat.tinylinkshortener.model.converter.LinkStatusConverter;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Entity
@Data
@DynamicUpdate // For status update
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "link")
public class Link implements Model<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @CreationTimestamp
    @ToString.Include
    @Column(updatable = false, nullable = false)
    private LocalDateTime created;

    @NotNull
    @Convert(converter = LinkStatusConverter.class)
    @Column(nullable = false)
    private LinkStatus status;

    @NotNull
    @ToString.Include
    @Column(nullable = false, updatable = false, name = "original_link")
    private String originalLink;

    @NotNull
    @ToString.Include
    @Column(nullable = false, updatable = false, name = "short_link")
    private String shortLink;

    private LocalDateTime availableFrom;

    private LocalDateTime availableTo;

    @Column(name = "max_visit_count")
    private Integer maxVisitCount;

    @Formula("( SELECT count(*) FROM visit v WHERE v.link_id = id)")
    private Integer currentVisitCount;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "link", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<Visit> visitList;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private User user;


    public Integer getCurrentVisitCount() {
        if (currentVisitCount == null) {
            return 0;
        }
        return currentVisitCount;
    }

    @Transient
    public boolean isExpired() {
        if (availableTo == null) {
            return false;
        }

        return availableTo.isBefore(LocalDateTime.now());
    }

    @Transient
    public boolean isNotAvailable() {
        if (availableFrom == null) {
            return false;
        }

        return availableFrom.isAfter(LocalDateTime.now());
    }

    @Transient
    public boolean isTooMuchRequest() {
        if (maxVisitCount == null || maxVisitCount < 1) {
            return false;
        }
        return currentVisitCount >= maxVisitCount;
    }

}
