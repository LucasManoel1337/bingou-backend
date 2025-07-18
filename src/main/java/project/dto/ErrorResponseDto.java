package project.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {
    private String mensagem;

    public ErrorResponseDto(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}