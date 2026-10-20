package br.com.cervejaria.estoque.controller;
import br.com.cervejaria.estoque.model.*;
import br.com.cervejaria.estoque.repository.*;
import br.com.cervejaria.estoque.service.*;
import br.com.cervejaria.estoque.controller.*;
import br.com.cervejaria.estoque.dto.*;
import br.com.cervejaria.estoque.exception.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service) { this.service = service; }

    @GetMapping
    public List<MovimentacaoResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public MovimentacaoResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse registrar(@Valid @RequestBody MovimentacaoRequest req) {
        return service.registrar(req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void estornar(@PathVariable Long id) { service.estornar(id); }
}