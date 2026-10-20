package br.com.cervejaria.estoque.dto;
import br.com.cervejaria.estoque.model.*;
import br.com.cervejaria.estoque.repository.*;
import br.com.cervejaria.estoque.service.*;
import br.com.cervejaria.estoque.controller.*;
import br.com.cervejaria.estoque.dto.*;
import br.com.cervejaria.estoque.exception.*;

import java.math.BigDecimal;

public record InsumoResponse(
    Long id,
    String nome,
    TipoInsumo tipo,
    String unidade,
    BigDecimal estoqueMinimo,
    BigDecimal quantidadeAtual,
    boolean abaixoDoMinimo
) {
    public static InsumoResponse from(Insumo i) {
        return new InsumoResponse(
                i.getId(), 
                i.getNome(), 
                i.getTipo(), 
                i.getUnidade(),
                i.getEstoqueMinimo(), 
                i.getQuantidadeAtual(),
                i.getQuantidadeAtual().compareTo(i.getEstoqueMinimo()) < 0);
    }
}