package org.sicredi.repository;

import org.sicredi.model.enums.StatusSessaoEnum;
import org.sicredi.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {
    boolean existsByPautaIdAndStatus(Long pautaId, StatusSessaoEnum status);
}
