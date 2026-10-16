package br.com.cervejaria.estoque;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    @Query("select i from Insumo i where i.quantidadeAtual < i.estoqueMinimo")
    List<Insumo> findAbaixoDoMinimo();
}
