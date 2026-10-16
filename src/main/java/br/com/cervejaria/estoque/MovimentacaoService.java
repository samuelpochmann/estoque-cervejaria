package br.com.cervejaria.estoque;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter @Setter
public class MovimentacaoService {

    private final MovimentacaoRepository movRepo;
    private final InsumoRepository insumoRepo;

    public MovimentacaoService(MovimentacaoRepository movRepo, InsumoRepository insumoRepo) {
        this.movRepo = movRepo;
        this.insumoRepo = insumoRepo;
    }

    public List<Movimentacao> listar() {
        return movRepo.findAllByOrderByDataDesc();
    }

    public Movimentacao buscar(Long id) {
        return movRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimentação não encontrada"));
    }

    @Transactional
    public Movimentacao registrar(MovimentacaoRequest req) {
        Insumo insumo = insumoRepo.findById(req.insumoId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Insumo não encontrado"));

        BigDecimal novoSaldo = req.tipo() == TipoMovimentacao.ENTRADA
                ? insumo.getQuantidadeAtual().add(req.quantidade())
                : insumo.getQuantidadeAtual().subtract(req.quantidade());

        if (novoSaldo.signum() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque insuficiente");
        }

        insumo.setQuantidadeAtual(novoSaldo);

        Movimentacao m = new Movimentacao();
        m.setInsumo(insumo);
        m.setTipo(req.tipo());
        m.setQuantidade(req.quantidade());
        m.setObservacao(req.observacao());
        m.setData(LocalDateTime.now());
        return movRepo.save(m);
    }

    /** "Excluir" uma movimentação = estornar: desfaz o efeito no saldo. */
    @Transactional
    public void estornar(Long id) {
        Movimentacao m = buscar(id);
        Insumo insumo = m.getInsumo();

        BigDecimal novoSaldo = m.getTipo() == TipoMovimentacao.ENTRADA
                ? insumo.getQuantidadeAtual().subtract(m.getQuantidade())
                : insumo.getQuantidadeAtual().add(m.getQuantidade());

        if (novoSaldo.signum() < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível estornar: o saldo ficaria negativo");
        }

        insumo.setQuantidadeAtual(novoSaldo);
        movRepo.delete(m);
    }
}
