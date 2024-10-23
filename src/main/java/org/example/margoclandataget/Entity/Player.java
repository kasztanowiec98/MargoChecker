package org.example.margoclandataget.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(unique = true)
    private String name;
    private String mainClanPlayerName;
    private String level;
    private String clan;
    private String klasa;
    private String url;
    private String playerid;
    private String ban;
    private String charid;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime insertDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateDate;
    public void setPlayerDetails(String name, String mainClanPlayerName, String level, String clan, String klasa,
                                 String url, String playerid, String ban, String charid) {
        this.setName(name);
        this.setMainClanPlayerName(mainClanPlayerName);
        this.setLevel(level);
        this.setClan(clan);
        this.setKlasa(klasa);
        this.setUrl(url);
        this.setPlayerid(playerid);
        this.setBan(ban);
        this.setCharid(charid);
    }
}
