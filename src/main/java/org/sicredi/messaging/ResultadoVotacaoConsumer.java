package org.sicredi.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class ResultadoVotacaoConsumer {
    @RabbitListener(queues = "resultado.votacao.queue")
    public void processarResultado(String mensagem) {
        System.out.println("Resultado da votação recebido: " + mensagem);
    }
}
