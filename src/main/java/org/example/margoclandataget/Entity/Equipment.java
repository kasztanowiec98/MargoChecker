package org.example.margoclandataget.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String charid;
    private String playername;
    private String typ;
    private String nazwaprzedmiotu;
    private String ulepszenie;
    private String rzadkosc;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime insertDate;
}
