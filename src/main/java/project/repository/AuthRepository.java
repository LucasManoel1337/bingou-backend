package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.CadastroEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<CadastroEntity, String> {

    Optional<CadastroEntity> findByEmail(String email);

    // MÉTODO NOVO PARA O TESTE
    @Query("SELECT c.id FROM CadastroEntity c WHERE c.email = :email")
    Optional<UUID> findIdByEmail(@Param("email") String email);
}
