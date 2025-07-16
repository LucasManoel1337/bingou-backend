package project.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaldoResponseDto {
    private String saldoId;
    private BigDecimal valor;
    private String usuarioId;

    // Você pode criar um construtor para facilitar
    public SaldoResponseDto(String saldoId, BigDecimal valor, String usuarioId) {
        this.saldoId = saldoId;
        this.valor = valor;
        this.usuarioId = usuarioId;
    }
}