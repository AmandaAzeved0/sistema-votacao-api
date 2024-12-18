package org.sicredi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sicredi.model.dto.PautaRequestDTO;
import org.sicredi.model.dto.PautaResponseDTO;
import org.sicredi.model.Pauta;
import org.sicredi.repository.PautaRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PautaService {
    private final PautaRepository pautaRepository;

    /**
     * Cria uma nova pauta no banco de dados.
     *
     * @param pautaRequest DTO com os dados da nova pauta.
     * @return Pauta criada.
     */
    public PautaResponseDTO criarPauta(PautaRequestDTO pautaRequest) {
        log.info("Iniciando o cadastro de uma nova pauta.");

        Pauta pauta = new Pauta();
        pauta.setTitulo(pautaRequest.getTitulo());
        pauta.setDescricao(pautaRequest.getDescricao());

        Pauta savedPauta = pautaRepository.save(pauta);
        return new PautaResponseDTO(savedPauta.getId(), savedPauta.getTitulo(), savedPauta.getDescricao());
    }
}
