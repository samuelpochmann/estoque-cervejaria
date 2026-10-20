package br.com.cervejaria.estoque.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice //faz a classe interceptar exceções de todos os controllers.

public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> naoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage(), req, Map.of());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> regraNegocio(RegraNegocioException ex, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, ex.getMessage(), req, Map.of());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResposta> conflito(ConflitoException ex, HttpServletRequest req) {
        return montar(HttpStatus.CONFLICT, ex.getMessage(), req, Map.of());
    }

    /** Falha nas anotações @NotBlank, @NotNull, @Positive etc. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> validacao(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos", req, campos);
    }

    /** JSON malformado ou valor de enum inexistente (ex.: "tipo":"ROUBO"). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> jsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST,
                "JSON malformado ou com valor inválido (confira os tipos permitidos)", req, Map.of());
    }

    /** Ex.: GET /insumos/abc, onde o id deveria ser número. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tipoInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro '" + ex.getName() + "'", req, Map.of());
    }

    /** Rede de segurança: qualquer outra exceção. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> inesperado(Exception ex, HttpServletRequest req) {
        // Erros do próprio Spring (rota inexistente, método não permitido etc.)
        // já carregam o status correto, então preservamos esse status.
        if (ex instanceof ErrorResponse er) {
            String detalhe = er.getBody().getDetail();
            return montar(er.getStatusCode(),
                    detalhe != null ? detalhe : "Não foi possível processar a requisição", req, Map.of());
        }
        log.error("Erro inesperado em {}", req.getRequestURI(), ex);
        return montar(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno. Tente novamente mais tarde.", req, Map.of());
    }

    private ResponseEntity<ErroResposta> montar(HttpStatusCode status, String mensagem,
                                                HttpServletRequest req, Map<String, String> campos) {
        HttpStatus conhecido = HttpStatus.resolve(status.value());
        String erro = conhecido != null ? conhecido.getReasonPhrase() : "Erro";
        ErroResposta corpo = new ErroResposta(
                LocalDateTime.now(), status.value(), erro, mensagem, req.getRequestURI(), campos);
        return ResponseEntity.status(status).body(corpo);
    }
}
