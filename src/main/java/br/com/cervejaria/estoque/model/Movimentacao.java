package br.com.cervejaria.estoque.model;
import br.com.cervejaria.estoque.model.*;
import br.com.cervejaria.estoque.repository.*;
import br.com.cervejaria.estoque.service.*;
import br.com.cervejaria.estoque.controller.*;
import br.com.cervejaria.estoque.dto.*;
import br.com.cervejaria.estoque.exception.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Insumo insumo;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    private BigDecimal quantidade;
    private LocalDateTime data;
    private String observacao; // ex: "Compra Mr. Beer", "Brassagem IPA #3"
}
