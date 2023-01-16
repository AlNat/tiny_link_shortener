package dev.alnat.tinylinkshortener.model;

import dev.alnat.tinylinkshortener.model.converter.VisitHeadersConverter;
import dev.alnat.tinylinkshortener.model.converter.VisitStatusConverter;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLInetType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.springframework.http.HttpHeaders;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(typeClass = PostgreSQLInetType.class, defaultForType = Inet.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "visit")
public class Visit implements Serializable, Comparable<Visit> {

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
    @ToString.Include
    @Convert(converter = VisitStatusConverter.class)
    @Column(nullable = false, updatable = false)
    private VisitStatus status;

    @ToString.Include
    @Column(name = "ip_address", columnDefinition = "inet", updatable = false)
    private Inet ip;

    @Column(updatable = false)
    private String userAgent;

    @Column(name = "headers", columnDefinition = "text", updatable = false)
    @Convert(converter = VisitHeadersConverter.class)
    private HttpHeaders headers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Link link;

    @Override
    public int compareTo(Visit o) {
        return o.getId().compareTo(this.id);
    }
}
