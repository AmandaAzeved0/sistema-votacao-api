package org.sicredi.model.dto;


/**
 * DTO para resposta padronizada ao registrar o voto.
 */
public class VotoResponseDTO {
    private final String mensagem;

    public VotoResponseDTO(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
