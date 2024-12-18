package org.sicredi.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.sicredi.model.dto.AssociadoResponseDTO;
import org.sicredi.service.AssociadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/associados")
@RequiredArgsConstructor
@Tag(name = "Associados", description = "Endpoints para gerenciamento de associados na aplicação.")
public class AssociadosController {
    private final AssociadoService associadoService;

    /**
     * Gera um novo associado com dados fictícios e salva no banco de dados.
     *
     * @return {@link ResponseEntity} contendo o {@link AssociadoResponseDTO} com os dados do associado criado.
     */
    @Operation(summary = "Cadastrar novo associado", description = "Gera dados fictícios para o associado e o salva no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associado cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar o associado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<AssociadoResponseDTO> gerarAssociado() {
        AssociadoResponseDTO associado = associadoService.gerarNovoAssociado();
        return ResponseEntity.ok(associado);
    }
}
