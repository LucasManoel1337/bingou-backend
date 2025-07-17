package project.service;

import org.springframework.stereotype.Service;
import project.dto.SalasDto;
import project.dto.SalasResponseDto;
import project.entity.SalasEntity;
import project.entity.SalasUsuariosEntity;
import project.repository.SalasChatRepository;
import project.repository.SalasRepository;
import project.repository.SalasUsuariosRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SalasService {

    private final SalasRepository repository;
    private final SalasChatRepository chatRepository;
    private final SaldoService saldoService;
    private final SalasUsuariosRepository usuariosRepository;

    public SalasService(SalasRepository repository, SalasChatRepository chatRepository, SaldoService saldoService, SalasUsuariosRepository usuariosRepository) {
        this.repository = repository;
        this.chatRepository = chatRepository;
        this.saldoService = saldoService;
        this.usuariosRepository = usuariosRepository;
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

            String mensagem = "Sala criada com sucesso!";
            SalasResponseDto response = new SalasResponseDto(mensagem, novaSala.getCodigo());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ocorreu um erro a criar uma sala!", e);
        }
    }

    public SalasResponseDto entrarSala(String usuarioId, String salaId) {
        try {
            Optional<SalasEntity> optionalSala = repository.findById(salaId);
            if (optionalSala.isEmpty()) {
                throw new IllegalArgumentException("Sala não encontrada.");
            }

            SalasEntity sala = optionalSala.get();
            BigDecimal saldoUsuario = saldoService.consultarSaldo(usuarioId).getValor();
            BigDecimal valorEntrada = sala.getValorEntrada();

            if (saldoUsuario.compareTo(valorEntrada) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente para entrar na sala.");
            }

            sala.setJogadoresAtuais(sala.getJogadoresAtuais() + 1);

            if (sala.getJogadoresAtuais() > sala.getJogadoresMaximo()) {
                throw new IllegalArgumentException("Sala cheia (verificação final).");
            }

            // Retirando o dinheiro da entrada do jogador
            saldoService.removerSaldo(usuarioId, sala.getValorEntrada());

            // Adicionando valor de entrada no valor do premio
            sala.setValorPremio(sala.getValorPremio().add(sala.getValorEntrada()));
            repository.save(sala);

            SalasUsuariosEntity usuariosEntity = new SalasUsuariosEntity();
            usuariosEntity.setId(UUID.randomUUID().toString());
            usuariosEntity.setIdSala(salaId);
            usuariosEntity.setIdUsuario(usuarioId);
            usuariosEntity.setNomeUsuario("joaozinho");
            usuariosRepository.save(usuariosEntity);

            String mensagem = "Entrou na sala com sucesso!";
            SalasResponseDto response = new SalasResponseDto(mensagem, sala.getCodigo());
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar entrar na sala: " + e.getMessage(), e);
        }
    }
}
