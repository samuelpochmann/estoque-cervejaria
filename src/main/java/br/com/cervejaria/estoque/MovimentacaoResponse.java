package br.com.cervejaria.estoque;

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
