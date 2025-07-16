package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.SaldoEntity;

import java.math.BigDecimal;
import java.util.Optional;

public interface SaldoRepository extends JpaRepository<SaldoEntity, String> {

    Optional<SaldoEntity> findByUsuarioId(String usuarioId);

    @Modifying
    @Query(
            value = "UPDATE tb_saldos SET valor = valor + :valorBonus",
            nativeQuery = true
    )
    void adicionarBonusParaTodos(@Param("valorBonus") BigDecimal valorBonus);
}