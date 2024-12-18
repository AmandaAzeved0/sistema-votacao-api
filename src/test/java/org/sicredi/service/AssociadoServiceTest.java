package org.sicredi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sicredi.exception.ResourceNotFoundException;
import org.sicredi.external.CpfGeneratorClient;
import org.sicredi.external.RandomUserClient;
import org.sicredi.model.Associado;
import org.sicredi.model.dto.AssociadoResponseDTO;
import org.sicredi.model.dto.RandomUserResponse;
import org.sicredi.repository.AssociadoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssociadoServiceTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private RandomUserClient randomUserClient;

    @Mock
    private CpfGeneratorClient cpfGeneratorClient;

    @Mock
    private CpfValidationService cpfValidationService;

    @InjectMocks
    private AssociadoService associadoService;

    private RandomUserResponse mockRandomUserResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuração do Mock de RandomUserResponse
        RandomUserResponse.Result.Name name = new RandomUserResponse.Result.Name();
        name.setFirst("João");
        name.setLast("Silva");

        RandomUserResponse.Result.Location.Street street = new RandomUserResponse.Result.Location.Street();
        street.setNumber(123);
        street.setName("Rua Principal");

        RandomUserResponse.Result.Location location = new RandomUserResponse.Result.Location();
        location.setStreet(street);
        location.setCity("Porto Alegre");
        location.setState("RS");
        location.setCountry("Brasil");

        RandomUserResponse.Result result = new RandomUserResponse.Result();
        result.setName(name);
        result.setLocation(location);
        result.setEmail("joao.silva@example.com");
        result.setPhone("51999999999");

        mockRandomUserResponse = new RandomUserResponse();
        mockRandomUserResponse.setResults(java.util.Collections.singletonList(result));
    }


    @Test
    void deveLancarExcecaoAoGerarNovoAssociadoSeAlgoFalhar() {
        // Arrange
        when(randomUserClient.fetchRandomUser()).thenThrow(new RuntimeException("API indisponível"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> associadoService.gerarNovoAssociado());
        assertEquals("Erro ao gerar novo associado. Tente novamente mais tarde.", exception.getMessage());

        verify(randomUserClient, times(1)).fetchRandomUser();
        verifyNoInteractions(cpfGeneratorClient, cpfValidationService, associadoRepository);
    }

    @Test
    void deveBuscarAssociadoPorIdComSucesso() {
        // Arrange
        Associado associadoMock = new Associado();
        associadoMock.setId(1L);
        associadoMock.setNome("João Silva");
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associadoMock));

        // Act
        Associado result = associadoService.buscarAssociadoPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("João Silva", result.getNome());
        verify(associadoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoNaoForEncontrado() {
        // Arrange
        when(associadoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> associadoService.buscarAssociadoPorId(1L));
        assertEquals("Associado não encontrado com o ID: 1", exception.getMessage());
        verify(associadoRepository, times(1)).findById(1L);
    }
}
