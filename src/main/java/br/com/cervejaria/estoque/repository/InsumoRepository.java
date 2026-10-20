package br.com.cervejaria.estoque.repository;
import br.com.cervejaria.estoque.model.*;
import br.com.cervejaria.estoque.repository.*;
import br.com.cervejaria.estoque.service.*;
import br.com.cervejaria.estoque.controller.*;
import br.com.cervejaria.estoque.dto.*;
import br.com.cervejaria.estoque.exception.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    @Query("select i from Insumo i where i.quantidadeAtual < i.estoqueMinimo")
    List<Insumo> findAbaixoDoMinimo();
}
