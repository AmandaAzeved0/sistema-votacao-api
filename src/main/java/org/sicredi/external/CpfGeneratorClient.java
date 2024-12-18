package org.sicredi.external;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CpfGeneratorClient {

    private static final WebClient webClientCpf = WebClient.builder()
            .baseUrl("https://www.4devs.com.br")
            .build();

    public static String generateCpf() {
        return webClientCpf.post()
                .uri("/ferramentas_online.php")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("acao=gerar_cpf&pontuacao=false")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
