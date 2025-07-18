package project.dto;

import lombok.Data;

@Data
public class JogadorSalaDto {
    private String nomeUsuario;
    private int pontos;

    public JogadorSalaDto(int pontos, String nomeUsuario) {
        this.pontos = pontos;
        this.nomeUsuario = nomeUsuario;
    }
}