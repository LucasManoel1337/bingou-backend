package project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.SaldoResponseDto;
import project.entity.SaldoEntity;
import project.repository.SaldoRepository;

import java.math.BigDecimal;

@Service
public class SaldoService {

    private final SaldoRepository saldoRepository;

    public SaldoService(SaldoRepository saldoRepository) {
        this.saldoRepository = saldoRepository;
    }

    @Transactional
    public void aplicarBonusParaTodosUsuarios(BigDecimal valorDoBonus) {
        if (valorDoBonus.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor do bônus não pode ser negativo.");
        }

        saldoRepository.adicionarBonusParaTodos(valorDoBonus);
    }

    public SaldoResponseDto consultarSaldo(String usuarioId) {
        SaldoEntity saldo = saldoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Saldo não encontrado para o usuário com ID: " + usuarioId));

        return new SaldoResponseDto(saldo.getId(), saldo.getValor(), saldo.getUsuario().getId());
    }

    @Transactional
    public SaldoEntity adicionarSaldo(String usuarioId, BigDecimal valorParaAdicionar) {
        SaldoEntity saldoParaAtualizar = saldoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Saldo não encontrado para o usuário com ID: " + usuarioId));

        BigDecimal saldoAtual = saldoParaAtualizar.getValor();
        BigDecimal novoSaldo = saldoAtual.add(valorParaAdicionar);
        saldoParaAtualizar.setValor(novoSaldo);

        return saldoRepository.save(saldoParaAtualizar);
    }

    @Transactional
    public SaldoEntity removerSaldo(String usuarioId, BigDecimal valorParaRemover) {
        SaldoEntity saldo = saldoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Saldo não encontrado para o usuário com ID: " + usuarioId));

        BigDecimal saldoAtual = saldo.getValor();

        if (saldoAtual.compareTo(valorParaRemover) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar a operação.");
        }

        BigDecimal novoSaldo = saldoAtual.subtract(valorParaRemover);
        saldo.setValor(novoSaldo);

        return saldoRepository.save(saldo);
    }
}