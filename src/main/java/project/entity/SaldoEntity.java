package project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "tb_saldos") // O nome da tabela no plural, como no Liquibase
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaldoEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "valor", nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @OneToOne(fetch = FetchType.LAZY) // Define um relacionamento de um para um
    @JoinColumn(name = "id_usuario", referencedColumnName = "id") // Especifica a chave estrangeira
    private CadastroEntity usuario;

    // Métodos equals() e hashCode() baseados apenas no ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaldoEntity that = (SaldoEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}