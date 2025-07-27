package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.entity.BingoCartelaEntity;
import project.service.BingoService;

import java.util.List;

@RestController
@RequestMapping("/bingo")
public class BingoController {

    private BingoService service;

    public BingoController(BingoService service) {
        this.service = service;
    }

    @PostMapping("/criar-cartela")
    public ResponseEntity<BingoCartelaEntity> criarCartela(
            @RequestParam String usuarioId,
            @RequestParam String salaId) {
        return ResponseEntity.ok(service.criarCartela(usuarioId, salaId));
    }

    @GetMapping("/listar-cartela")
    public ResponseEntity<List<BingoCartelaEntity>> listarCartelas(
            @RequestParam String usuarioId,
            @RequestParam String salaId) {
        return ResponseEntity.ok(service.listarCartelasPorUsuarioESala(usuarioId, salaId));
    }
}
