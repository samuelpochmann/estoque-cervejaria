package br.com.cervejaria.estoque.dto;
import br.com.cervejaria.estoque.model.*;
import br.com.cervejaria.estoque.repository.*;
import br.com.cervejaria.estoque.service.*;
import br.com.cervejaria.estoque.controller.*;
import br.com.cervejaria.estoque.dto.*;
import br.com.cervejaria.estoque.exception.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        Long insumoId,
        String insumoNome,
        TipoMovimentacao tipo,
        BigDecimal quantidade,
        LocalDateTime data,
        String observacao) {

    public static MovimentacaoResponse from(Movimentacao m) {
        return new MovimentacaoResponse(
                m.getId(), 
                m.getInsumo().getId(), 
                m.getInsumo().getNome(),
                m.getTipo(), 
                m.getQuantidade(), 
                m.getData(), 
                m.getObservacao());
    }
}
