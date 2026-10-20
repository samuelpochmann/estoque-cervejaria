package br.com.cervejaria.estoque.dto;
import br.com.cervejaria.estoque.model.*;
import br.com.cervejaria.estoque.repository.*;
import br.com.cervejaria.estoque.service.*;
import br.com.cervejaria.estoque.controller.*;
import br.com.cervejaria.estoque.dto.*;
import br.com.cervejaria.estoque.exception.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record InsumoRequest(
    @NotBlank(message = "é obrigatório") String nome,
        @NotNull(message = "é obrigatório") TipoInsumo tipo,
        @NotBlank(message = "é obrigatório") String unidade,
        @NotNull(message = "é obrigatório")
        @PositiveOrZero(message = "não pode ser negativo") BigDecimal estoqueMinimo
) {}