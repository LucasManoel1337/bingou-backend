package project.service;

import org.springframework.stereotype.Service;
import project.dto.CadastroDto;
import project.dto.LoginDto;
import project.dto.LoginResponseDto;
import project.entity.CadastroEntity;
import project.repository.AuthRepository;
import project.repository.SaldoRepository;
import project.service.security.CripDescripSenhaService;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final SaldoRepository saldoRepository; // 1. Adicione o novo repositório
    private final CripDescripSenhaService cripDescripService;

    // 2. Receba-o no construtor
    public AuthService(AuthRepository authRepository, SaldoRepository saldoRepository, CripDescripSenhaService cripDescripService) {
        this.authRepository = authRepository;
        this.saldoRepository = saldoRepository;
        this.cripDescripService = cripDescripService;
    }

    public String novoCadastro(CadastroDto dto){
        try{
            CadastroEntity entity = new CadastroEntity();
            CripDescripSenhaService CripDescrip = new CripDescripSenhaService();

            entity.setEmail(CripDescripSenhaService.criptografar(dto.getEmail()));
            entity.setSenha(CripDescripSenhaService.criptografar(dto.getSenha()));
            entity.setCpf(CripDescripSenhaService.criptografar(dto.getCpf()));

            repository.save(entity);

            return "Cadastro feito com sucesso!";
        } catch (Exception e) {
            System.err.println(e);
            return "Ocorreu um erro ao realizar o cadastro!";
        }
    }

    // Altere o tipo de retorno para a nova DTO
    public LoginResponseDto loginUsuario(LoginDto dto) {
        try {
            String emailCriptografadoParaBusca = cripDescripService.criptografar(dto.getEmail());
            var usuarioOptional = repository.findByEmail(emailCriptografadoParaBusca);

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
