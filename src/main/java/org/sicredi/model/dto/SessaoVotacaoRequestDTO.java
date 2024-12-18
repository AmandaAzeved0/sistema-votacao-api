package org.sicredi.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * DTO de requisição para criar uma sessão de votação.
 */

@Data
public class SessaoVotacaoRequestDTO {

    @NotNull(message = "O ID da pauta é obrigatório.")
    private Long pautaId;

    private Integer duracaoMinutos;
}
