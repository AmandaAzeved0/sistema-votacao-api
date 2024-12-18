package org.sicredi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PautaResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
}
