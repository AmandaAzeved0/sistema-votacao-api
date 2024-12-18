package org.sicredi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class CpfValidationService {

    /**
     * Valida o CPF e retorna o status de aptidão.
     *
     * @param cpf CPF a ser validado.
     * @return true ou false randomicamente.
     */
    public boolean validarCpf(String cpf) {
        log.info("Iniciando validação do CPF: {}", cpf);

        // Gera um resultado aleatório (simulação de validação)
        boolean aptoParaVotar = ThreadLocalRandom.current().nextBoolean();

        log.info("Validação concluída para CPF {}: {}", cpf, aptoParaVotar);
        return aptoParaVotar;
    }
}
