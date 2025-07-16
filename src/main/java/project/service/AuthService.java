package project.service;

import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Service;
import project.dto.CadastroDto;
import project.dto.LoginDto;
import project.dto.LoginResponseDto;
import project.entity.CadastroEntity;
import project.entity.SaldoEntity;
import project.repository.AuthRepository;
import project.repository.SaldoRepository;
import project.service.security.CripDescripSenhaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final SaldoRepository saldoRepository;
    private final CripDescripSenhaService cripDescripService;

    public AuthService(AuthRepository authRepository, SaldoRepository saldoRepository, CripDescripSenhaService cripDescripService) {
        this.authRepository = authRepository;
        this.saldoRepository = saldoRepository;
        this.cripDescripService = cripDescripService;
    }

    @Transactional
    public String novoCadastro(CadastroDto dto) {
        try {
            // --- 1. Criar e salvar o usuário ---
            CadastroEntity novaEntidade = new CadastroEntity();
            novaEntidade.setEmail(cripDescripService.criptografar(dto.getEmail()));
            novaEntidade.setCpf(cripDescripService.criptografar(dto.getCpf()));
            novaEntidade.setSenha(cripDescripService.criptografar(dto.getSenha()));
            novaEntidade.setNomeCompleto(cripDescripService.criptografar(dto.getNomeCompleto()));
            novaEntidade.setDataNascimento(dto.getDataNascimento());

            CadastroEntity usuarioSalvo = authRepository.save(novaEntidade);

            // --- 2. Criar e salvar o saldo inicial para esse usuário ---
            SaldoEntity novoSaldo = new SaldoEntity();
            novoSaldo.setUsuario(usuarioSalvo);
            novoSaldo.setValor(BigDecimal.ZERO);
            saldoRepository.save(novoSaldo);

            return "Cadastro realizado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro!", e);
        }
    }

    public LoginResponseDto loginUsuario(LoginDto dto) {
        try {
            String emailCriptografadoParaBusca = cripDescripService.criptografar(dto.getEmail());
            var usuarioOptional = authRepository.findByEmail(emailCriptografadoParaBusca);

            if (usuarioOptional.isEmpty()) {
                return null;
            }

            CadastroEntity usuarioEncontrado = usuarioOptional.get();
            String senhaCriptografadaDoBanco = usuarioEncontrado.getSenha();
            String senhaDescriptografada = cripDescripService.descriptografar(senhaCriptografadaDoBanco);

            if (senhaDescriptografada.equals(dto.getSenha())) {
                return new LoginResponseDto("Login realizado com sucesso!", usuarioEncontrado.getId());
            } else {
                return null;
            }

        } catch (Exception e) {
            System.err.println("Erro durante o login: " + e.getMessage());
            return null;
        }
    }
}
