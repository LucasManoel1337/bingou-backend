package project.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.SalasDto;
import project.dto.SalasResponseDto;
import project.entity.SalasEntity;
import project.service.SalasService;

import java.util.List;

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

}
