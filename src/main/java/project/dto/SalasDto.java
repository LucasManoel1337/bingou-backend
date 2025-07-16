package project.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalasDto {

    private String nome;
    private BigDecimal valorEntrada;
    private int jogadoresMaximos;

}
