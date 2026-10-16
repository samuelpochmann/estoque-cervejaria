package br.com.cervejaria.estoque;

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
    public List<Movimentacao> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Movimentacao buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movimentacao registrar(@Valid @RequestBody MovimentacaoRequest req) {
        return service.registrar(req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void estornar(@PathVariable Long id) { service.estornar(id); }
}