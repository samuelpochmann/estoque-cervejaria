package br.com.cervejaria.estoque;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findAllByOrderByDataDesc();
    boolean existsByInsumoId(Long insumoId);
}
