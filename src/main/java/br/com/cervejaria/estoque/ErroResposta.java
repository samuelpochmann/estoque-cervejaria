package br.com.cervejaria.estoque;

import java.time.LocalDateTime;
import java.util.Map;

public record ErroResposta( 
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem,
    String caminho,
    Map<String, String> campos){}