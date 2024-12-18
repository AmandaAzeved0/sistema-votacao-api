package org.sicredi.repository;

import org.sicredi.model.enums.VotoEnum;
import org.sicredi.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    @Query("select v from Voto v where v.associado.id = :associadoId and v.sessao.id = :sessaoVotacaoId")
    Optional<Voto> findByAssociadoIdAndSessaoVotacaoId(Long associadoId, Long sessaoVotacaoId);

    long countBySessaoIdAndAndVotoDoAssociado(Long sessaoId, VotoEnum votoEnum);
}
