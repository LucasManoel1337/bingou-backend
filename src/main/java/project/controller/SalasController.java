package project.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.SalasDto;
import project.dto.SalasResponseDto;
import project.service.SalasService;

@RestController
@RequestMapping("/salas")
public class SalasController {

    private SalasService service;

    public SalasController(SalasService service) {
        this.service = service;
    }

    @PostMapping("/criar")
    public ResponseEntity<SalasResponseDto> criarSala(@RequestBody @Valid SalasDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarSala(dto));
    }

}
