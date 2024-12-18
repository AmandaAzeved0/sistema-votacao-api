package org.sicredi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.sicredi.model.enums.StatusSessaoEnum;
import org.sicredi.messaging.SessaoEncerramentoPublisher;
import org.sicredi.model.Pauta;
import org.sicredi.model.SessaoVotacao;
import org.sicredi.model.dto.SessaoVotacaoRequestDTO;
import org.sicredi.model.dto.SessaoVotacaoResponseDTO;
import org.sicredi.repository.PautaRepository;
import org.sicredi.repository.SessaoVotacaoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

class SessaoVotacaoServiceTest {

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoEncerramentoPublisher encerramentoPublisher;

    @InjectMocks
    private SessaoVotacaoService sessaoVotacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarSessaoComSucesso() {
        // Arrange
        Long pautaId = 1L;
        Integer duracaoMinutos = 10;

        SessaoVotacaoRequestDTO requestDTO = new SessaoVotacaoRequestDTO();
        requestDTO.setPautaId(pautaId);
        requestDTO.setDuracaoMinutos(duracaoMinutos);

        Pauta pautaMock = new Pauta();
        pautaMock.setId(pautaId);
        pautaMock.setTitulo("Pauta Teste");

        SessaoVotacao sessaoMock = new SessaoVotacao();
        sessaoMock.setId(1L);
        sessaoMock.setPauta(pautaMock);
        sessaoMock.setDataInicio(LocalDateTime.now());
        sessaoMock.setDuracao(duracaoMinutos);
        sessaoMock.setStatus(StatusSessaoEnum.ABERTA);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pautaMock));
        when(sessaoVotacaoRepository.existsByPautaIdAndStatus(pautaId, StatusSessaoEnum.ABERTA)).thenReturn(false);
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoMock);

        // Act
        SessaoVotacaoResponseDTO responseDTO = sessaoVotacaoService.criarSessao(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals(pautaId, responseDTO.getPautaId());
        assertEquals(StatusSessaoEnum.ABERTA.toString(), responseDTO.getStatus());

        // Verificações
        verify(pautaRepository, times(1)).findById(pautaId);
        verify(sessaoVotacaoRepository, times(1)).existsByPautaIdAndStatus(pautaId, StatusSessaoEnum.ABERTA);
        verify(sessaoVotacaoRepository, times(1)).save(any(SessaoVotacao.class));
        verify(encerramentoPublisher, times(1)).enviarEncerramentoSessao(eq(sessaoMock.getId()), anyLong());
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoExistir() {
        // Arrange
        Long pautaId = 1L;
        SessaoVotacaoRequestDTO requestDTO = new SessaoVotacaoRequestDTO();
        requestDTO.setPautaId(pautaId);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sessaoVotacaoService.criarSessao(requestDTO);
        });

        assertEquals("Pauta não encontrada com o ID: " + pautaId, exception.getMessage());
        verify(pautaRepository, times(1)).findById(pautaId);
        verifyNoInteractions(sessaoVotacaoRepository, encerramentoPublisher);
    }

    @Test
    void deveLancarExcecaoQuandoSessaoJaEstiverAberta() {
        // Arrange
        Long pautaId = 1L;
        SessaoVotacaoRequestDTO requestDTO = new SessaoVotacaoRequestDTO();
        requestDTO.setPautaId(pautaId);

        Pauta pautaMock = new Pauta();
        pautaMock.setId(pautaId);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pautaMock));
        when(sessaoVotacaoRepository.existsByPautaIdAndStatus(pautaId, StatusSessaoEnum.ABERTA)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sessaoVotacaoService.criarSessao(requestDTO);
        });

        assertEquals("A pauta já está associada a uma sessão aberta.", exception.getMessage());
        verify(pautaRepository, times(1)).findById(pautaId);
        verify(sessaoVotacaoRepository, times(1)).existsByPautaIdAndStatus(pautaId, StatusSessaoEnum.ABERTA);
        verifyNoMoreInteractions(sessaoVotacaoRepository, encerramentoPublisher);
    }
}
