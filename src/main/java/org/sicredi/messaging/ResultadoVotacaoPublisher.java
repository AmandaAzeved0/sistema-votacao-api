package org.sicredi.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultadoVotacaoPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ResultadoVotacaoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarResultado(String queueName, Object mensagem) {
        rabbitTemplate.convertAndSend(queueName, mensagem);
    }
}
