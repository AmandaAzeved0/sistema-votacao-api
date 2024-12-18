package org.sicredi.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.sicredi.model.enums.StatusSessaoEnum;
import org.sicredi.model.enums.VotoEnum;
import org.sicredi.model.Associado;
import org.sicredi.model.SessaoVotacao;
import org.sicredi.model.Voto;
import org.sicredi.repository.VotoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotoService {
    private final VotoRepository votoRepository;
    private final AssociadoService associadoService;
    private final SessaoVotacaoService sessaoService;


    /**
     * Registra um voto para uma determinada sessao e associado.
     *
     * @param sessaoId     ID da sessao.
     * @param associadoId ID do associado.
     * @param voto        Valor do voto (SIM ou NAO).
     * @throws BadRequestException Se alguma validação falhar.
     */
    public void registrarVoto(Long sessaoId, Long associadoId, String voto) throws BadRequestException {
        VotoEnum votoEnum = converterVoto(voto); // Realiza a conversão do voto primeiro

        SessaoVotacao sessao = validarSessao(sessaoId);
        Associado associado = validarAssociado(associadoId);

        if (votoRepository.findByAssociadoIdAndSessaoVotacaoId(associadoId, sessaoId).isPresent()) {
            throw new BadRequestException("Associado já votou nesta sessao.");
        }

        Voto novoVoto = Voto.builder()
                .associado(associado)
                .sessao(sessao)
                .votoDoAssociado(votoEnum)
                .build();

        votoRepository.save(novoVoto);
    }



    private SessaoVotacao validarSessao(Long sessaoId) throws BadRequestException {

        SessaoVotacao sessao = sessaoService.getSessaoById(sessaoId);

        if (!sessao.getStatus().equals(StatusSessaoEnum.ABERTA)) {
            throw new BadRequestException("Sessão de votação está encerrada.");
        }

        return sessao;
    }

    private Associado validarAssociado(Long associadoId) throws BadRequestException {
        Associado associado = associadoService.buscarAssociadoPorId(associadoId);
        if(!associado.isAptoParaVotar()){
            throw new BadRequestException("Associado não está apto para votar.");
        }
        return associado;
    }



    private VotoEnum converterVoto(String voto) throws BadRequestException {
        try {
            return VotoEnum.valueOf(voto.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Voto inválido. Utilize 'SIM' ou 'NAO'.");
        }
    }
}
