package org.sicredi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.sicredi.model.dto.VotoRequestDTO;
import org.sicredi.model.dto.VotoResponseDTO;
import org.sicredi.service.VotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/votacao")
@RequiredArgsConstructor
@Tag(name = "Votação", description = "Endpoints para registro de votos em uma pauta de votação.")
@Validated
public class VotacaoController {

    private final VotoService votoService;

    /**
     * Registra o voto de um associado em uma sessao específica.
     *
     * @param sessaoId     ID da sessao a ser votada.
     * @param associadoId ID do associado que está votando.
     * @param voto        Valor do voto (SIM ou NAO).
     * @return {@link ResponseEntity} com uma mensagem de sucesso.
     * @throws BadRequestException Caso a validação do voto ou outros critérios falhem.
     */
    @Operation(summary = "Registrar voto",
            description = "Registra o voto de um associado em uma sessao aberta para votação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos ou regra de negócio violada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping("/sessao/{sessaoId}/votar")
    public ResponseEntity<VotoResponseDTO> votar(
            @Parameter(description = "ID da sessao para votação", required = true)
            @PathVariable @NotNull Long sessaoId,

            @Parameter(description = "ID do associado que está votando", required = true)
            @RequestParam @NotNull Long associadoId,

            @Parameter(description = "Voto do associado (SIM ou NAO)", required = true)
            @RequestParam @NotBlank String voto
    ) throws BadRequestException {
        votoService.registrarVoto(sessaoId, associadoId, voto);
        return ResponseEntity.ok(new VotoResponseDTO("Voto registrado com sucesso!"));
    }


    @PostMapping("/votar-em-lote")
    public ResponseEntity<String> registrarVotosEmLote(@RequestBody @Valid List<VotoRequestDTO> votos) {
        votos.forEach(voto ->
                {
                    try {
                        votoService.registrarVoto(voto.getSessaoId(), voto.getAssociadoId(), voto.getVoto());
                    } catch (BadRequestException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return ResponseEntity.ok("Votos registrados com sucesso!");
    }
}
