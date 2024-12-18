package org.sicredi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfValidationServiceTest {

    private CpfValidationService cpfValidationService;

    @BeforeEach
    void setUp() {
        cpfValidationService = new CpfValidationService();
    }

    @Test
    void deveRetornarTrueQuandoCpfForValidado() {
        // Arrange
        String cpf = "12345678901";

        // Mockando ThreadLocalRandom para retornar "true"
        try (MockedStatic<ThreadLocalRandom> mockedRandom = Mockito.mockStatic(ThreadLocalRandom.class)) {
            mockedRandom.when(ThreadLocalRandom::current)
                    .thenReturn(Mockito.mock(ThreadLocalRandom.class));

            Mockito.when(ThreadLocalRandom.current().nextBoolean()).thenReturn(true);

            // Act
            boolean resultado = cpfValidationService.validarCpf(cpf);

            // Assert
            assertTrue(resultado, "O resultado da validação deveria ser true");
        }
    }

    @Test
    void deveRetornarFalseQuandoCpfForInvalido() {
        // Arrange
        String cpf = "12345678901";

        // Mockando ThreadLocalRandom para retornar "false"
        try (MockedStatic<ThreadLocalRandom> mockedRandom = Mockito.mockStatic(ThreadLocalRandom.class)) {
            mockedRandom.when(ThreadLocalRandom::current)
                    .thenReturn(Mockito.mock(ThreadLocalRandom.class));

            Mockito.when(ThreadLocalRandom.current().nextBoolean()).thenReturn(false);

            // Act
            boolean resultado = cpfValidationService.validarCpf(cpf);

            // Assert
            assertFalse(resultado, "O resultado da validação deveria ser false");
        }
    }
}
