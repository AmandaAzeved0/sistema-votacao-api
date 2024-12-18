package org.sicredi.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VotoRequestDTO {

    @NotNull(message = "O ID da sessão é obrigatório.")
    private Long sessaoId;

    @NotNull(message = "O ID do associado é obrigatório.")
    private Long associadoId;

    @NotNull(message = "O voto é obrigatório.")
    @Pattern(regexp = "^(SIM|NAO)$", message = "O voto deve ser SIM ou NAO.")
    private String voto;
}
