package org.sicredi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public Queue resultadoVotacaoQueue() {
        return new Queue("resultado.votacao.queue", true);
    }

    // Fila onde a mensagem será processada após expiração (DLX)
    @Bean
    public Queue sessaoEncerramentoDlxQueue() {
        return new Queue("sessao.encerramento.dlx.queue", true);
    }

    // Exchange para DLX
    @Bean
    public DirectExchange sessaoDlxExchange() {
        return new DirectExchange("sessao.dlx.exchange");
    }

    // Fila principal com TTL e DLX configurado
    @Bean
    public Queue sessaoEncerramentoQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "sessao.dlx.exchange");
        args.put("x-dead-letter-routing-key", "sessao.encerrar.dlx");
        return new Queue("sessao.encerramento.queue", true, false, false, args);
    }

    // Exchange principal
    @Bean
    public DirectExchange sessaoExchange() {
        return new DirectExchange("sessao.exchange");
    }

    // Binding da fila principal para a exchange principal
    @Bean
    public Binding sessaoEncerramentoBinding(Queue sessaoEncerramentoQueue, DirectExchange sessaoExchange) {
        return BindingBuilder.bind(sessaoEncerramentoQueue).to(sessaoExchange).with("sessao.encerrar");
    }

    // Binding da fila DLX para a exchange DLX
    @Bean
    public Binding sessaoDlxBinding(Queue sessaoEncerramentoDlxQueue, DirectExchange sessaoDlxExchange) {
        return BindingBuilder.bind(sessaoEncerramentoDlxQueue).to(sessaoDlxExchange).with("sessao.encerrar.dlx");
    }
}
