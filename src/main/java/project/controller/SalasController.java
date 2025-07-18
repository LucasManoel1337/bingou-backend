package project.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.ErrorResponseDto;
import project.dto.JogadorSalaDto;
import project.dto.SalasDto;
import project.dto.SalasResponseDto;
import project.entity.SalasEntity;
import project.service.SalasService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/salas")
public class SalasController {

    private SalasService service;

    public SalasController(SalasService service) {
        this.service = service;
    }

    @GetMapping("/listar")
    public List<SalasEntity> listarSalas() {
        return service.listarTodasSalas();
    }

    @PostMapping("/criar")
    public ResponseEntity<SalasResponseDto> criarSala(@RequestBody @Valid SalasDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarSala(dto));
    }

    @PostMapping("/entrar")
    public ResponseEntity<SalasResponseDto> entrarSala(
            @RequestParam String usuarioId,
            @RequestParam String salaId) {
        try {
            SalasResponseDto response = service.entrarSala(usuarioId, salaId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new SalasResponseDto(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SalasResponseDto("Erro interno: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{salaId}/jogadores")
    public ResponseEntity<List<JogadorSalaDto>> listarJogadores(@PathVariable String salaId) {
        try {
            List<JogadorSalaDto> jogadores = service.buscarJogadoresComPontosPorSala(salaId);
            return ResponseEntity.ok(jogadores);
        } catch (Exception e) {
            System.err.println("Erro ao listar jogadores da sala " + salaId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{salaId}")
    public ResponseEntity<?> buscarSala(@PathVariable String salaId) {
        try {
            SalasEntity sala = service.buscarSalaPorId(salaId);
            return ResponseEntity.ok(sala);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno: " + e.getMessage()));
        }
    }

    @PostMapping("/sair")
    public ResponseEntity<?> sairSala(@RequestParam String usuarioId, @RequestParam String salaId) {
        try {
            SalasResponseDto response = service.sairSala(usuarioId, salaId);
            return ResponseEntity.ok(response); // Retorna 200 OK com a mensagem de sucesso
        } catch (RuntimeException e) {
            // Captura as RuntimeException lançadas pelo serviço (incluindo IllegalArgumentException encapsuladas)
            // e retorna um HttpStatus.BAD_REQUEST (400) com a mensagem de erro.
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        } catch (Exception e) {
            // Captura quaisquer outras exceções inesperadas e retorna um HttpStatus.INTERNAL_SERVER_ERROR (500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto("Erro interno do servidor ao tentar sair da sala."));
        }
    }

}
