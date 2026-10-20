package br.com.cervejaria.estoque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cervejaria.estoque.dto.InsumoRequest;
import br.com.cervejaria.estoque.dto.InsumoResponse;
import br.com.cervejaria.estoque.service.InsumoService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/insumos")
public class InsumoController {

    private final InsumoService service;

    public InsumoController(InsumoService service) { this.service = service; }

    @GetMapping
    public List<InsumoResponse> listar() { return service.listar(); }

    @GetMapping("/abaixo-do-minimo")
    public List<InsumoResponse> abaixoDoMinimo() { return service.listarAbaixoDoMinimo(); }

    @GetMapping("/{id}")
    public InsumoResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsumoResponse criar(@Valid @RequestBody InsumoRequest req) { return service.criar(req); }

    @PutMapping("/{id}")
    public InsumoResponse atualizar(@PathVariable Long id, @Valid @RequestBody InsumoRequest req) {
        return service.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
