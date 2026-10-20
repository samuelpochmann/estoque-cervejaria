package br.com.cervejaria.estoque;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movRepo;
    private final InsumoRepository insumoRepo;

    public MovimentacaoService(MovimentacaoRepository movRepo, InsumoRepository insumoRepo) {
        this.movRepo = movRepo;
        this.insumoRepo = insumoRepo;
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> listar() {
        return movRepo.findAllByOrderByDataDesc().stream().map(MovimentacaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public MovimentacaoResponse buscar(Long id) {
        return MovimentacaoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public MovimentacaoResponse registrar(MovimentacaoRequest req) {
        Insumo insumo = insumoRepo.findById(req.insumoId()).orElseThrow(() ->
                new RecursoNaoEncontradoException("Insumo não encontrado: " + req.insumoId()));

        BigDecimal novoSaldo = req.tipo() == TipoMovimentacao.ENTRADA
                ? insumo.getQuantidadeAtual().add(req.quantidade())
                : insumo.getQuantidadeAtual().subtract(req.quantidade());

        if (novoSaldo.signum() < 0) {
            throw new RegraNegocioException("Estoque insuficiente de " + insumo.getNome()
                    + ": saldo atual " + insumo.getQuantidadeAtual() + " " + insumo.getUnidade());
        }

        insumo.setQuantidadeAtual(novoSaldo);

        Movimentacao m = new Movimentacao();
        m.setInsumo(insumo);
        m.setTipo(req.tipo());
        m.setQuantidade(req.quantidade());
        m.setObservacao(req.observacao());
        m.setData(LocalDateTime.now());
        return MovimentacaoResponse.from(movRepo.save(m));
    }

    /** "Excluir" uma movimentação = estornar: desfaz o efeito no saldo. */
    @Transactional
    public void estornar(Long id) {
        Movimentacao m = buscarEntidade(id);
        Insumo insumo = m.getInsumo();

        BigDecimal novoSaldo = m.getTipo() == TipoMovimentacao.ENTRADA
                ? insumo.getQuantidadeAtual().subtract(m.getQuantidade())
                : insumo.getQuantidadeAtual().add(m.getQuantidade());

        if (novoSaldo.signum() < 0) {
            throw new ConflitoException("Não é possível estornar: o saldo de "
                    + insumo.getNome() + " ficaria negativo");
        }

        insumo.setQuantidadeAtual(novoSaldo);
        movRepo.delete(m);
    }

    private Movimentacao buscarEntidade(Long id) {
        return movRepo.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Movimentação não encontrada: " + id));
    }
}