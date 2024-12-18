package org.sicredi.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessaoEncerramentoPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void enviarEncerramentoSessao(Long sessaoId, long ttl) {
        rabbitTemplate.convertAndSend("sessao.exchange", "sessao.encerrar", sessaoId, message -> {
            message.getMessageProperties().setExpiration(String.valueOf(ttl));
            System.out.println("Enviando mensagem para encerrar sessão ID: " + sessaoId + " com TTL de " + ttl + " ms");
            return message;
        });
    }
}
