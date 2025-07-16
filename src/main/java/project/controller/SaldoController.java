package project.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.SaldoDto;
import project.dto.SaldoResponseDto;
import project.entity.SaldoEntity;
import project.service.SaldoService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/saldo")
public class SaldoController {

    private final SaldoService service;

    public SaldoController(SaldoService service) {
        this.service = service;
    }

    @PostMapping("/bonus-all")
    public ResponseEntity<String> aplicarBonusParaTodos(@RequestParam("valor") BigDecimal valor) {
        try {
            service.aplicarBonusParaTodosUsuarios(valor);
            String mensagem = String.format("Bônus de R$ %.2f aplicado com sucesso para todos os usuários.", valor);
            return ResponseEntity.ok(mensagem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/consultar/{usuarioId}")
    public ResponseEntity<SaldoResponseDto> consultarSaldo(@PathVariable String usuarioId) {
        SaldoResponseDto saldoDto = service.consultarSaldo(usuarioId);
        return ResponseEntity.ok(saldoDto);
    }

    @PostMapping("/adicionar/{usuarioId}")
    public ResponseEntity<SaldoResponseDto> adicionarSaldo(@PathVariable String usuarioId, @Valid @RequestBody SaldoDto dto) {
        SaldoEntity saldoAtualizadoEntity = service.adicionarSaldo(usuarioId, dto.getValor());

        SaldoResponseDto respostaDto = new SaldoResponseDto(
                saldoAtualizadoEntity.getId(),
                saldoAtualizadoEntity.getValor(),
                saldoAtualizadoEntity.getUsuario().getId()
        );

        return ResponseEntity.ok(respostaDto);
    }

    @PostMapping("/remover/{usuarioId}")
    public ResponseEntity<?> removerSaldo(@PathVariable String usuarioId, @Valid @RequestBody SaldoDto dto) {
        try {
            SaldoEntity saldoAtualizadoEntity = service.removerSaldo(usuarioId, dto.getValor());
            SaldoResponseDto respostaDto = new SaldoResponseDto(
                    saldoAtualizadoEntity.getId(),
                    saldoAtualizadoEntity.getValor(),
                    saldoAtualizadoEntity.getUsuario().getId()
            );
            return ResponseEntity.ok(respostaDto);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Saldo insuficiente")) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado.");
        }
    }
}