package project.dto;

import lombok.Data;

import java.util.List;

@Data
public class BingoCartelaDto {

    private String id;
    private String usuarioId;
    private String salaId;
    private List<String> numerosB;
    private List<String> numerosI;
    private List<String> numerosN;
    private List<String> numerosG;
    private List<String> numerosO;
}
