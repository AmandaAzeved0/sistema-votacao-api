package org.sicredi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sicredi.enums.StatusSessaoEnum;

import java.time.LocalDateTime;
@Table(name = "sessao_votacao")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SessaoVotacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "duracao_em_minutos")
    private Integer duracao;

    @Column(name = "status", nullable = false)
    @Enumerated()
    private StatusSessaoEnum status; // 'ABERTA' ou 'ENCERRADA'

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
