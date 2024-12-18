package org.sicredi.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;


/**
 * DTO para a criação de uma nova Pauta.
 */
@Data
public class PautaRequestDTO {

        /**
         * Título da pauta.
         * Deve ser preenchido e conter entre 3 e 100 caracteres.
         */
        @NotBlank(message = "O título é obrigatório.")
        @Size(min = 3, max = 100, message = "O título deve conter entre 3 e 100 caracteres.")
        private String titulo;

        /**
         * Descrição da pauta.
         * Deve ser preenchida e conter entre 10 e 500 caracteres.
         */
        @NotBlank(message = "A descrição é obrigatória.")
        @Size(min = 10, max = 500, message = "A descrição deve conter entre 10 e 500 caracteres.")
        private String descricao;
}
