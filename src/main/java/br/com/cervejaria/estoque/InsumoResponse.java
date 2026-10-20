package br.com.cervejaria.estoque;

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