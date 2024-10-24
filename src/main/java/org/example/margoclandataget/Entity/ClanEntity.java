package org.example.margoclandataget.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
public class ClanEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Clan name is required")
    private String clanName;

    @NotEmpty(message = "Clan URL is required")
    private String clanUrl;
}
