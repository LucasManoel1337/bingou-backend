package project.repository;

import org.springframework.stereotype.Repository;
import project.entity.BingoCartelaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface BingoCartelaRepository extends JpaRepository<BingoCartelaEntity, String> {

    List<BingoCartelaEntity> findByUsuarioIdAndSalaId(String usuarioId, String salaId);

    void deleteByUsuarioIdAndSalaId(String usuarioId, String salaId); // opcional
}
