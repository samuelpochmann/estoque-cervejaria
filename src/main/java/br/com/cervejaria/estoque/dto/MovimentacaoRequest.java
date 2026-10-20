package br.com.cervejaria.estoque.dto;
import br.com.cervejaria.estoque.model.*;
import br.com.cervejaria.estoque.repository.*;
import br.com.cervejaria.estoque.service.*;
import br.com.cervejaria.estoque.controller.*;
import br.com.cervejaria.estoque.dto.*;
import br.com.cervejaria.estoque.exception.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record MovimentacaoRequest(

    @NotNull(message = "é obrigatório") Long insumoId,
    @NotNull(message = "é obrigatório") TipoMovimentacao tipo,
    @NotNull(message = "é obrigatório")
    @Positive(message = "deve ser maior que zero") BigDecimal quantidade,
    String observacao) {}