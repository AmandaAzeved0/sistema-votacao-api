package org.sicredi.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.sicredi.enums.VotoEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "voto", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sessao_id", "associado_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessaoVotacao sessao;

    @ManyToOne
    @JoinColumn(name = "associado_id", nullable = false)
    private Associado associado;

    @Column(name="voto", nullable = false)
    @Enumerated()
    private VotoEnum votoDoAssociado;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
