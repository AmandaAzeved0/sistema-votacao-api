package org.sicredi.service;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.sicredi.model.enums.StatusSessaoEnum;
import org.sicredi.model.Associado;
import org.sicredi.model.SessaoVotacao;
import org.sicredi.model.Voto;
import org.sicredi.repository.VotoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private SessaoVotacaoService sessaoService;

    @InjectMocks
    private VotoService votoService;

    private Associado associadoMock;
    private SessaoVotacao sessaoMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        associadoMock = new Associado();
        associadoMock.setId(1L);
        associadoMock.setNome("Teste Associado");
        associadoMock.setAptoParaVotar(true);

        sessaoMock = new SessaoVotacao();
        sessaoMock.setId(1L);
        sessaoMock.setStatus(StatusSessaoEnum.ABERTA);
    }

    @Test
    void deveRegistrarVotoComSucesso() throws BadRequestException {
        // Arrange
        Long sessaoId = 1L;
        Long associadoId = 1L;
        String voto = "SIM";

        when(sessaoService.getSessaoById(sessaoId)).thenReturn(sessaoMock);
        when(associadoService.buscarAssociadoPorId(associadoId)).thenReturn(associadoMock);
        when(votoRepository.findByAssociadoIdAndSessaoVotacaoId(associadoId, sessaoId))
                .thenReturn(Optional.empty());

        // Act
        votoService.registrarVoto(sessaoId, associadoId, voto);

        // Assert
        verify(sessaoService, times(1)).getSessaoById(sessaoId);
        verify(associadoService, times(1)).buscarAssociadoPorId(associadoId);
        verify(votoRepository, times(1)).save(any(Voto.class));
    }

    @Test
    void deveLancarExcecaoQuandoSessaoNaoEstaAberta() {
        // Arrange
        Long sessaoId = 1L;
        sessaoMock.setStatus(StatusSessaoEnum.ENCERRADA);

        when(sessaoService.getSessaoById(sessaoId)).thenReturn(sessaoMock);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            votoService.registrarVoto(sessaoId, 1L, "SIM");
        });

        assertEquals("Sessão de votação está encerrada.", exception.getMessage());
        verify(sessaoService, times(1)).getSessaoById(sessaoId);
        verifyNoInteractions(associadoService, votoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoJaVotou() {
        // Arrange
        Long sessaoId = 1L;
        Long associadoId = 1L;

        when(sessaoService.getSessaoById(sessaoId)).thenReturn(sessaoMock);
        when(associadoService.buscarAssociadoPorId(associadoId)).thenReturn(associadoMock);
        when(votoRepository.findByAssociadoIdAndSessaoVotacaoId(associadoId, sessaoId))
                .thenReturn(Optional.of(new Voto()));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            votoService.registrarVoto(sessaoId, associadoId, "SIM");
        });

        assertEquals("Associado já votou nesta sessao.", exception.getMessage());
        verify(votoRepository, times(1)).findByAssociadoIdAndSessaoVotacaoId(associadoId, sessaoId);
        verifyNoMoreInteractions(votoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoNaoEstaAptoParaVotar() {
        // Arrange
        Long sessaoId = 1L;
        Long associadoId = 1L;
        associadoMock.setAptoParaVotar(false);

        when(sessaoService.getSessaoById(sessaoId)).thenReturn(sessaoMock);
        when(associadoService.buscarAssociadoPorId(associadoId)).thenReturn(associadoMock);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            votoService.registrarVoto(sessaoId, associadoId, "SIM");
        });

        assertEquals("Associado não está apto para votar.", exception.getMessage());
        verify(associadoService, times(1)).buscarAssociadoPorId(associadoId);
        verifyNoInteractions(votoRepository);
    }

    @Test
    void deveLancarExcecaoParaVotoInvalido() {
        // Arrange
        Long sessaoId = 1L;
        Long associadoId = 1L;

        when(sessaoService.getSessaoById(sessaoId)).thenReturn(sessaoMock);
        when(associadoService.buscarAssociadoPorId(associadoId)).thenReturn(associadoMock);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            votoService.registrarVoto(sessaoId, associadoId, "INVALIDO");
        });

        assertEquals("Voto inválido. Utilize 'SIM' ou 'NAO'.", exception.getMessage());
        verifyNoInteractions(votoRepository);
    }
}
