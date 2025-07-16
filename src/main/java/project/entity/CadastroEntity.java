package project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator; // Importe esta anotação

import java.util.Objects;

@Entity
@Table(name = "tb_cadastro")
@Getter
@Setter
public class CadastroEntity {

    @Id
    @GeneratedValue(generator = "uuid2") // Use o gerador de UUID do Hibernate
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "varchar(36)") // Mapeie para varchar
    private String id; // <--- TIPO ALTERADO PARA STRING

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @Column(name = "cpf")
    private String cpf;

    // Implementação segura de equals() e hashCode() para entidades JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CadastroEntity that = (CadastroEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}