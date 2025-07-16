package project.service;

import org.springframework.stereotype.Service;
import project.dto.SalasDto;
import project.dto.SalasResponseDto;
import project.entity.SalasChatEntity;
import project.entity.SalasEntity;
import project.entity.SalasUsuariosEntity;
import project.repository.SalasChatRepository;
import project.repository.SalasRepository;
import project.repository.SalasUsuariosRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SalasService {

    private final SalasRepository repository;
    private final SalasChatRepository chatRepository;

    public SalasService(SalasRepository repository, SalasChatRepository chatRepository) {
        this.repository = repository;
        this.chatRepository = chatRepository;
    }

    public List<SalasEntity> listarTodasSalas() {
        return repository.findAll();
    }

    public String criarCodigoDeSala() {
        String codigo;
        final int tentativasMaximas = 100;

        for (int i = 0; i < tentativasMaximas; i++) {
            int numeroAleatorio = ThreadLocalRandom.current().nextInt(100000, 1000000);
            codigo = String.valueOf(numeroAleatorio);

            if (!repository.existsByCodigo(codigo)) {
                return codigo;
            }
        }

        throw new RuntimeException("Não foi possível gerar um código de sala único após " + tentativasMaximas + " tentativas.");
    }

    public SalasResponseDto criarSala(SalasDto dto) {
        try {
            //Criando a sala
            SalasEntity novaSala = new SalasEntity();
            novaSala.setNome(dto.getNome());
            novaSala.setCodigo(criarCodigoDeSala());
            novaSala.setValorEntrada(dto.getValorEntrada());
            novaSala.setValorPremio(BigDecimal.valueOf(0.0));
            novaSala.setJogadoresAtuais(0);
            novaSala.setJogadoresMaximo(dto.getJogadoresMaximos());
            repository.save(novaSala);

            //Criando a tabela de chat da sala
            SalasChatEntity novaSalaChat = new SalasChatEntity();
            novaSalaChat.setId(novaSala.getId());
            chatRepository.save(novaSalaChat);

            String mensagem = "Sala criada com sucesso!";
            SalasResponseDto response = new SalasResponseDto(mensagem, novaSala.getCodigo());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ocorreu um erro a criar uma sala!", e);
        }
    }
}
