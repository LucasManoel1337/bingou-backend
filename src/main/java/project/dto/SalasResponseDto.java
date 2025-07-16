package project.dto;

import lombok.Data;

@Data
public class SalasResponseDto {
    private String message;
    private String codigoDaSala;

    public SalasResponseDto(String message, String codigoDaSala) {
        this.message = message;
        this.codigoDaSala = codigoDaSala;
    }
}
