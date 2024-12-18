package org.sicredi.service;

import lombok.RequiredArgsConstructor;
import org.sicredi.model.dto.SessaoVotacaoRequestDTO;
import org.sicredi.model.dto.SessaoVotacaoResponseDTO;
import org.sicredi.model.enums.StatusSessaoEnum;
import org.sicredi.messaging.SessaoEncerramentoPublisher;
import org.sicredi.model.Pauta;
import org.sicredi.model.SessaoVotacao;
import org.sicredi.repository.PautaRepository;
import org.sicredi.repository.SessaoVotacaoRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessaoVotacaoService {

    private static final int DURACAO_PADRAO_MINUTOS = 1;

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final PautaRepository pautaRepository;
    private final SessaoEncerramentoPublisher encerramentoPublisher;

    /**
     * Cria uma nova sessão de votação associada a uma pauta.
     *
     * @param requestDTO Dados da requisição para criar uma sessão de votação.
     * @return SessaoVotacao Sessão criada.
     */
    public SessaoVotacaoResponseDTO criarSessao(SessaoVotacaoRequestDTO requestDTO) {
        Pauta pauta = buscarPautaExistente(requestDTO.getPautaId());
        validarSePautaPossuiSessaoAberta(requestDTO.getPautaId());

        SessaoVotacao novaSessao = construirSessao(pauta, requestDTO.getDuracaoMinutos());
        SessaoVotacao sessaoSalva = sessaoVotacaoRepository.save(novaSessao);

        long ttl = calcularTTL(novaSessao);
        enviarMensagemDeEncerramento(sessaoSalva.getId(), ttl);

        return mapToResponseDTO(sessaoSalva);
    }



    public SessaoVotacao getSessaoById(Long sessaoId) {
        return sessaoVotacaoRepository.findById(sessaoId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão de votação não encontrada com o ID: " + sessaoId));
    }

    private Pauta buscarPautaExistente(Long pautaId) {
        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada com o ID: " + pautaId));
    }

    private void validarSePautaPossuiSessaoAberta(Long pautaId) {
        boolean existeSessaoAberta = sessaoVotacaoRepository
                .existsByPautaIdAndStatus(pautaId, StatusSessaoEnum.ABERTA);

        if (existeSessaoAberta) {
            throw new IllegalArgumentException("A pauta já está associada a uma sessão aberta.");
        }
    }


    private SessaoVotacao construirSessao(Pauta pauta, Integer duracaoMinutos) {
        LocalDateTime dataHoraInicio = LocalDateTime.now();
        int duracao = (duracaoMinutos != null) ? duracaoMinutos : DURACAO_PADRAO_MINUTOS;

        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setPauta(pauta);
        sessao.setDataInicio(dataHoraInicio);
        sessao.setDuracao(duracao);
        sessao.setStatus(StatusSessaoEnum.ABERTA);

        return sessao;
    }


    private long calcularTTL(SessaoVotacao sessao) {
        LocalDateTime dataHoraFim = sessao.getDataInicio().plusMinutes(sessao.getDuracao());
        return Duration.between(sessao.getDataInicio(), dataHoraFim).toMillis();
    }

    private void enviarMensagemDeEncerramento(Long sessaoId, long ttl) {
        encerramentoPublisher.enviarEncerramentoSessao(sessaoId, ttl);
    }

    private SessaoVotacaoResponseDTO mapToResponseDTO(SessaoVotacao sessao) {
        return new SessaoVotacaoResponseDTO(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getDataInicio(),
                sessao.getStatus().toString()
        );
    }

}
