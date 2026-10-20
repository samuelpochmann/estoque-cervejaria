package br.com.cervejaria.estoque;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InsumoService {
    private final InsumoRepository repo;
    private final MovimentacaoRepository movRepo;

    public InsumoService(InsumoRepository repo, MovimentacaoRepository movRepo) {
        this.repo = repo;
        this.movRepo = movRepo;
    }

    @Transactional(readOnly = true)
    public List<InsumoResponse> listar() {
        return repo.findAll().stream().map(InsumoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<InsumoResponse> listarAbaixoDoMinimo() {
        return repo.findAbaixoDoMinimo().stream().map(InsumoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public InsumoResponse buscar(Long id) {
        return InsumoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public InsumoResponse criar(InsumoRequest req) {
        Insumo insumo = new Insumo(); // quantidadeAtual já começa em ZERO
        aplicar(insumo, req);
        return InsumoResponse.from(repo.save(insumo));
    }

    @Transactional
    public InsumoResponse atualizar(Long id, InsumoRequest req) {
        Insumo insumo = buscarEntidade(id);
        aplicar(insumo, req); // o saldo NÃO é tocado aqui
        return InsumoResponse.from(insumo); // o dirty checking grava a alteração
    }

    @Transactional
    public void excluir(Long id) {
        Insumo insumo = buscarEntidade(id);
        if (movRepo.existsByInsumoId(id)) {
            throw new ConflitoException("Insumo possui movimentações e não pode ser excluído");
        }
        repo.delete(insumo);
    }

    private Insumo buscarEntidade(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Insumo não encontrado: " + id));
    }

    private void aplicar(Insumo insumo, InsumoRequest req) {
        insumo.setNome(req.nome().trim());
        insumo.setTipo(req.tipo());
        insumo.setUnidade(req.unidade().trim());
        insumo.setEstoqueMinimo(req.estoqueMinimo());
    }
}
