package br.com.cervejaria.estoque;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
