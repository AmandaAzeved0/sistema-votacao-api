package org.sicredi.messaging;

import lombok.RequiredArgsConstructor;
import org.sicredi.model.enums.StatusSessaoEnum;
import org.sicredi.model.enums.VotoEnum;
import org.sicredi.model.SessaoVotacao;
import org.sicredi.repository.SessaoVotacaoRepository;
import org.sicredi.repository.VotoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessaoEncerramentoConsumer {
    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final VotoRepository votoRepository;
    private final ResultadoVotacaoPublisher resultadoVotacaoPublisher;

    @RabbitListener(queues = "sessao.encerramento.dlx.queue")
    public void encerrarSessao(Long sessaoId) {
        System.out.println("Recebida mensagem para encerrar sessão ID: " + sessaoId);

        SessaoVotacao sessao = sessaoVotacaoRepository.findById(sessaoId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada com o ID: " + sessaoId));

        if (sessao.getStatus() == StatusSessaoEnum.ABERTA) {
            sessao.setStatus(StatusSessaoEnum.ENCERRADA);
            sessaoVotacaoRepository.save(sessao);
            System.out.println("Sessão ID " + sessaoId + " foi encerrada automaticamente.");

            // Contabilizar os votos
            long votosSim = votoRepository.countBySessaoIdAndAndVotoDoAssociado(sessaoId, VotoEnum.SIM);
            long votosNao = votoRepository.countBySessaoIdAndAndVotoDoAssociado(sessaoId, VotoEnum.NAO);

            // Montar mensagem de resultado
            String resultado = "Sessão ID " + sessaoId + ": " + votosSim + " votos SIM, " + votosNao + " votos NÃO.";

            // Publicar o resultado na fila
            resultadoVotacaoPublisher.enviarResultado("resultado.votacao.queue", resultado);
            System.out.println("Resultado publicado na fila: " + resultado);
        }
    }
}
