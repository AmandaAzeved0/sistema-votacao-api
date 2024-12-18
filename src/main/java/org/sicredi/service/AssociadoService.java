package org.sicredi.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sicredi.model.dto.AssociadoResponseDTO;
import org.sicredi.exception.ResourceNotFoundException;
import org.sicredi.external.CpfGeneratorClient;
import org.sicredi.external.RandomUserClient;
import org.sicredi.model.Associado;
import org.sicredi.repository.AssociadoRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AssociadoService {

    private final AssociadoRepository associadoRepository;
    private final RandomUserClient randomUserClient;
    private final CpfGeneratorClient cpfGeneratorClient;
    private final CpfValidationService cpfValidationService;

    /**
     * Gera um novo associado utilizando dados fictícios de APIs externas.
     *
     * @return AssociadoResponseDTO com os dados do associado salvo.
     */
    public AssociadoResponseDTO gerarNovoAssociado() {
        try {
            log.info("Iniciando geração de novo associado.");

            var randomUser = randomUserClient.fetchRandomUser();
            String nome = randomUser.getFullName();
            String email = randomUser.getEmail();
            String telefone = randomUser.getPhone();
            String endereco = randomUser.getFullAddress();

            String cpf = cpfGeneratorClient.generateCpf();
            log.info("CPF gerado: {}", cpf);

            // Cria o objeto associado
            Associado associado = new Associado();
            associado.setNome(nome);
            associado.setCpf(cpf);
            associado.setEmail(email);
            associado.setTelefone(telefone);
            associado.setEndereco(endereco);
            associado.setAptoParaVotar(cpfValidationService.validarCpf(cpf));

            // Salva no banco
            Associado saved = associadoRepository.save(associado);
            log.info("Novo associado salvo com sucesso: ID {}", saved.getId());

            return new AssociadoResponseDTO(
                    saved.getId(),
                    saved.getNome(),
                    saved.getCpf(),
                    saved.getEmail(),
                    saved.getTelefone(),
                    saved.getEndereco()
            );

        } catch (Exception e) {
            log.error("Erro ao gerar novo associado", e);
            throw new RuntimeException("Erro ao gerar novo associado. Tente novamente mais tarde.", e);
        }
    }

    /**
     * Busca um associado pelo seu identificador único (ID).
     *
     * @param associadoId Identificador único do associado.
     * @return {@link Associado} encontrado no banco de dados.
     * @throws ResourceNotFoundException Caso o associado não seja encontrado.
     */
    public Associado buscarAssociadoPorId(Long associadoId) {
        return associadoRepository.findById(associadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Associado não encontrado com o ID: " + associadoId));
    }

}
