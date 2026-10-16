package br.com.cervejaria.estoque;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity //Informa ao JPA/Hibernate: "crie e mapeie uma tabela chamada insumo no banco de dados ára representar essa classe"
@Getter @Setter //Ja define os getter e setter sem precisar escrever o código manualmente, por exemplo getId() e setId()

public class Insumo {
    @Id //define que esse campo é a chave primária da tabela insumo
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank 
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoInsumo tipo;

    @NotBlank
    private String unidade; //kg, g, pacote...

    @NotNull @PositiveOrZero 
    private BigDecimal estoqueMinimo;

    //controlado apenas pelas movimentações
    private BigDecimal estoqueAtual = BigDecimal.ZERO;

    private BigDecimal quantidadeAtual = BigDecimal.ZERO;
}
