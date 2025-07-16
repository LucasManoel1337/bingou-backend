package project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaldoDto { // Nome da classe corrigido para SaldoDto

    @NotNull(message = "O campo 'valor' é obrigatório.")
    @Positive(message = "O campo 'valor' deve ser um número positivo.")
    private BigDecimal valor;
}