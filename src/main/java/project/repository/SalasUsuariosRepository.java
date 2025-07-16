package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.SalasUsuariosEntity;

public interface SalasUsuariosRepository extends JpaRepository<SalasUsuariosEntity, String> {
}
