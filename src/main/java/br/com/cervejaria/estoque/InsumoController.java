package br.com.cervejaria.estoque;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/insumos")
@Getter @Setter

public class InsumoController {

    private final InsumoRepository repo;
    private final MovimentacaoRepository movRepo;

    public InsumoController(InsumoRepository repo, MovimentacaoRepository movRepo) {
        this.repo = repo;
        this.movRepo = movRepo;
    }

    @GetMapping
    public List<Insumo> listar() { return repo.findAll(); }

    @GetMapping("/abaixo-do-minimo")
    public List<Insumo> abaixoDoMinimo() { return repo.findAbaixoDoMinimo(); }

    @GetMapping("/{id}")
    public Insumo buscar(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Insumo não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Insumo criar(@Valid @RequestBody Insumo dados) {
        dados.setId(null);
        dados.setQuantidadeAtual(BigDecimal.ZERO);
        return repo.save(dados);
    }

    @PutMapping("/{id}")
    public Insumo atualizar(@PathVariable Long id, @Valid @RequestBody Insumo dados) {
        Insumo i = buscar(id);
        i.setNome(dados.getNome());
        i.setTipo(dados.getTipo());
        i.setUnidade(dados.getUnidade());
        i.setEstoqueMinimo(dados.getEstoqueMinimo());
        return repo.save(i); // quantidadeAtual NÃO muda aqui
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        buscar(id);
        if (movRepo.existsByInsumoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Insumo possui movimentações e não pode ser excluído");
        }
        repo.deleteById(id);
    }
}
