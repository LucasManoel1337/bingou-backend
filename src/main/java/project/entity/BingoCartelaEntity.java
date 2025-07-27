package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_sala_bingo_cartela")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BingoCartelaEntity {

    @Id
    private String id;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(name = "sala_id", nullable = false)
    private String salaId;

    @Lob
    @Column(name = "numeros_b")
    private String numerosB;

    @Lob
    @Column(name = "numeros_i")
    private String numerosI;

    @Lob
    @Column(name = "numeros_n")
    private String numerosN;

    @Lob
    @Column(name = "numeros_g")
    private String numerosG;

    @Lob
    @Column(name = "numeros_o")
    private String numerosO;
}