package project.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "tb_salas")
public class SalasEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "valor_entrada")
    private BigDecimal valorEntrada;

    @Column(name = "valor_premio")
    private BigDecimal valorPremio;

    @Column(name = "jogadores_atuais")
    private int jogadoresAtuais;

    @Column(name = "maximo_jogadores")
    private int jogadoresMaximo;

}
