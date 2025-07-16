package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.SalasChatEntity;

public interface SalasChatRepository extends JpaRepository<SalasChatEntity, String> {
}
