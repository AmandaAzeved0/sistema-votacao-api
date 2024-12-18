package org.sicredi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sicredi.model.Pauta;
import org.sicredi.model.dto.PautaRequestDTO;
import org.sicredi.model.dto.PautaResponseDTO;
import org.sicredi.repository.PautaRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarPautaComSucesso() {
        // Arrange
        PautaRequestDTO pautaRequest = new PautaRequestDTO();
        pautaRequest.setTitulo("Título da Pauta");
        pautaRequest.setDescricao("Descrição detalhada da pauta");

        Pauta pautaMock = new Pauta();
        pautaMock.setId(1L);
        pautaMock.setTitulo(pautaRequest.getTitulo());
        pautaMock.setDescricao(pautaRequest.getDescricao());

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaMock);

        // Act
        PautaResponseDTO response = pautaService.criarPauta(pautaRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Título da Pauta", response.getTitulo());
        assertEquals("Descrição detalhada da pauta", response.getDescricao());

        // Verifica se o método save foi chamado uma vez
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    void deveLancarExcecaoQuandoSalvarPautaFalhar() {
        // Arrange
        PautaRequestDTO pautaRequest = new PautaRequestDTO();
        pautaRequest.setTitulo("Título da Pauta");
        pautaRequest.setDescricao("Descrição detalhada da pauta");

        when(pautaRepository.save(any(Pauta.class))).thenThrow(new RuntimeException("Falha ao salvar pauta"));

        // Act & Assert
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            pautaService.criarPauta(pautaRequest);
        });

        assertEquals("Falha ao salvar pauta", exception.getMessage());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }
}
