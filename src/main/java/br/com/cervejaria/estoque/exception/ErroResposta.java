package br.com.cervejaria.estoque.exception;
import br.com.cervejaria.estoque.model.*;
import br.com.cervejaria.estoque.repository.*;
import br.com.cervejaria.estoque.service.*;
import br.com.cervejaria.estoque.controller.*;
import br.com.cervejaria.estoque.dto.*;
import br.com.cervejaria.estoque.exception.*;

import java.time.LocalDateTime;
import java.util.Map;

public record ErroResposta( 
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem,
    String caminho,
    Map<String, String> campos){}