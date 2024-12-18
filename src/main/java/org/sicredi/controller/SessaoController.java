package org.sicredi.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sicredi.model.dto.SessaoVotacaoRequestDTO;
import org.sicredi.model.dto.SessaoVotacaoResponseDTO;
import org.sicredi.service.SessaoVotacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sessoes")
@Tag(name = "Sessões de Votação", description = "Endpoints para gerenciamento das sessões de votação.")
@Validated
public class SessaoController {

    private final SessaoVotacaoService sessaoService;

    /**
     * Cria uma nova sessão de votação.
     *
     * @param requestDTO Dados da sessão de votação.
     * @return {@link ResponseEntity} contendo os dados da sessão criada.
     */
    @Operation(summary = "Criar nova sessão de votação", description = "Cria uma nova sessão de votação para uma pauta existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<SessaoVotacaoResponseDTO> criarSessao(@Valid @RequestBody SessaoVotacaoRequestDTO requestDTO) {
        SessaoVotacaoResponseDTO sessaoResponse = sessaoService.criarSessao(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoResponse);
    }
}
