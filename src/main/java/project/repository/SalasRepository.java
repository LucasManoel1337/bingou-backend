package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.SalasEntity;
public interface SalasRepository extends JpaRepository<SalasEntity, String> {

    boolean existsByCodigo(String codigo);

}
