package br.com.cervejaria.estoque;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MovimentacaoRequest(

    @NotNull
    Long insumoId,

    @NotNull
    TipoMovimentacao tipo,

    @NotNull
    @DecimalMin("0.01")
    BigDecimal quantidade,

    String observacao
){}