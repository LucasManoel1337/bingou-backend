package project.service;

import org.springframework.stereotype.Service;
import project.dto.JogadorSalaDto;
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
import java.util.stream.Collectors;

@Service
public class SalasService {

    private final SalasRepository repository;
    private final SalasChatRepository chatRepository;
    private final SaldoService saldoService;
    private final SalasUsuariosRepository usuariosRepository;
    private final AuthService authService;

    public SalasService(SalasRepository repository, SalasChatRepository chatRepository, SaldoService saldoService, SalasUsuariosRepository usuariosRepository, AuthService authService) {
        this.repository = repository;
        this.chatRepository = chatRepository;
        this.saldoService = saldoService;
        this.usuariosRepository = usuariosRepository;
        this.authService = authService;
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
            Optional<SalasUsuariosEntity> usuarioJaNaSala = usuariosRepository.findByIdUsuarioAndIdSala(usuarioId, salaId);

            if (usuarioJaNaSala.isPresent()) {
                String mensagem = "Você já está nesta sala.";
                SalasResponseDto response = new SalasResponseDto(mensagem, sala.getCodigo());
                return response;
            }

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
            usuariosEntity.setNomeUsuario(authService.buscarNome(usuarioId));
            usuariosEntity.setPontos(0);
            usuariosRepository.save(usuariosEntity);

            String mensagem = "Entrou na sala com sucesso!";
            SalasResponseDto response = new SalasResponseDto(mensagem, sala.getCodigo());
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar entrar na sala: " + e.getMessage(), e);
        }
    }

    public List<JogadorSalaDto> buscarJogadoresComPontosPorSala(String salaId) {
        List<SalasUsuariosEntity> usuarios = usuariosRepository.findByIdSala(salaId);

        return usuarios.stream()
                .map(usuarioEntity -> new JogadorSalaDto(
                        usuarioEntity.getPontos(),
                        usuarioEntity.getNomeUsuario()
                                ))
                .collect(Collectors.toList());
    }

    public SalasEntity buscarSalaPorId(String salaId) {
        return repository.findById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com id: " + salaId));
    }

    public SalasResponseDto sairSala(String usuarioId, String salaId) {
        try {
            Optional<SalasEntity> optionalSala = repository.findById(salaId);
            if (optionalSala.isEmpty()) {
                throw new IllegalArgumentException("Sala não encontrada.");
            }
            SalasEntity sala = optionalSala.get();

            Optional<SalasUsuariosEntity> optionalSalasUsuarios = usuariosRepository.findByIdUsuarioAndIdSala(usuarioId, salaId);
            if (optionalSalasUsuarios.isEmpty()) {
                throw new IllegalArgumentException("Usuário não está nesta sala.");
            }
            usuariosRepository.delete(optionalSalasUsuarios.get());

            int jogadoresAtuais = sala.getJogadoresAtuais();
            if (jogadoresAtuais > 0) {
                sala.setJogadoresAtuais(jogadoresAtuais - 1);
            } else {
                System.out.println("Atenção: Contador de jogadores na sala já era 0 ao tentar sair.");
            }

            sala.setValorPremio(sala.getValorPremio().subtract(sala.getValorEntrada()));
            repository.save(sala);

            saldoService.adicionarSaldo(usuarioId, sala.getValorEntrada());

            String mensagem = "Saiu da sala com sucesso!";
            SalasResponseDto response = new SalasResponseDto(mensagem, sala.getCodigo());
            return response;

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao tentar sair da sala: " + e.getMessage(), e);
        }
    }
}
