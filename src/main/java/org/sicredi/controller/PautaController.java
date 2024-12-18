package org.sicredi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sicredi.model.dto.PautaRequestDTO;
import org.sicredi.model.dto.PautaResponseDTO;
import org.sicredi.service.PautaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Endpoints para gerenciamento de pautas na aplicação.")
@Validated
public class PautaController {

    private final PautaService pautaService;


    /**
     * Cria uma nova pauta no sistema.
     *
     * @param pautaRequest Dados da pauta a ser criada.
     * @return {@link ResponseEntity} contendo o {@link PautaResponseDTO} com os dados da pauta criada.
     */
    @Operation(summary = "Criar nova pauta", description = "Cria uma nova pauta no sistema com título e descrição fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<PautaResponseDTO> criarPauta(@Valid @RequestBody PautaRequestDTO pautaRequest) {
        PautaResponseDTO pautaResponse = pautaService.criarPauta(pautaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pautaResponse);
    }


}
