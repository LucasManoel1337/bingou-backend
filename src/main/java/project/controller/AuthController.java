package project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.CadastroDto;
import project.dto.LoginDto;
import project.dto.LoginResponseDto;
import project.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> novoCadastro(@RequestBody CadastroDto dto) {
        String respostaDoServico = service.novoCadastro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respostaDoServico);
    }

    // Novo endpoint para o login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        LoginResponseDto resposta = service.loginUsuario(dto);

        if (resposta != null) {
            return ResponseEntity.ok(resposta);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha no login: Usuário ou senha inválidos.");
        }
    }

    @GetMapping("/{userId}/nome")
    public ResponseEntity<String> buscarNome(@PathVariable String userId) {
        try {
            String nome = service.buscarNome(userId);
            return ResponseEntity.ok(nome);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar nome: " + e.getMessage());
        }
    }
}