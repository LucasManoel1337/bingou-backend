package project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_salas_usuarios")
public class SalasUsuariosEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_sala")
    private String id_sala;

    @Column(name = "id_usuario")
    private String id_usuario;

    @Column(name = "nome_usuario")
    private String nomeUsuario;

}
