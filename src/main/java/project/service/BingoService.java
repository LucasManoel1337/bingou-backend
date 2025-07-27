package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.BingoCartelaEntity;
import project.repository.BingoCartelaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class BingoService {

    @Autowired
    private BingoCartelaRepository repository;

    @Autowired
    public BingoService(BingoCartelaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BingoCartelaEntity criarCartela(String usuarioId, String salaId) {
        BingoCartelaEntity cartela = BingoCartelaEntity.builder()
                .id(UUID.randomUUID().toString())
                .usuarioId(usuarioId)
                .salaId(salaId)
                .numerosB(String.join(",", gerarNumeros(1, 15, 5)))
                .numerosI(String.join(",", gerarNumeros(16, 30, 5)))
                .numerosN(String.join(",", gerarNumeros(31, 45, 4)))
                .numerosG(String.join(",", gerarNumeros(46, 60, 5)))
                .numerosO(String.join(",", gerarNumeros(61, 75, 5)))
                .build();

        System.out.println(cartela);

        return repository.save(cartela);
    }

    public List<BingoCartelaEntity> listarCartelasPorUsuarioESala(String usuarioId, String salaId) {
        return repository.findByUsuarioIdAndSalaId(usuarioId, salaId);
    }

    private List<String> gerarNumeros(int inicio, int fim, int quantidade) {
        List<Integer> numeros = new ArrayList<>();
        for (int i = inicio; i <= fim; i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros);
        return numeros.subList(0, quantidade).stream().map(String::valueOf).toList();
    }

    @Transactional
    public void deletarCartelasPorUsuarioESala(String usuarioId, String salaId) {
        List<BingoCartelaEntity> cartelas = repository.findByUsuarioIdAndSalaId(usuarioId, salaId);
        repository.deleteAll(cartelas);
    }

    public boolean existeCartelaParaUsuarioESala(String usuarioId, String salaId) {
        List<BingoCartelaEntity> cartelas = repository.findByUsuarioIdAndSalaId(usuarioId, salaId);
        return !cartelas.isEmpty();
    }
}
