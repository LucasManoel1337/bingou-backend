package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.SalasUsuariosEntity;

import java.util.List;
import java.util.Optional;

public interface SalasUsuariosRepository extends JpaRepository<SalasUsuariosEntity, String> {

    List<SalasUsuariosEntity> findByIdSala(String idSala);

    Optional<SalasUsuariosEntity> findByIdUsuarioAndIdSala(String usuarioId, String salaId);
}
