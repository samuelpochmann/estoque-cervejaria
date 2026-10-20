#!/bin/bash
set -e

cd ~/projetos/estoque/src/main/java/br/com/cervejaria/estoque

mkdir -p model repository service controller dto exception

mover() {
  pkg=$1; shift
  for f in "$@"; do
    [ -f "$f.java" ] || continue
    mv "$f.java" "$pkg/$f.java"
    sed -i "s/^package br.com.cervejaria.estoque;/package br.com.cervejaria.estoque.$pkg;/" "$pkg/$f.java"
  done
}

mover model      Insumo Movimentacao TipoInsumo TipoMovimentacao
mover repository InsumoRepository MovimentacaoRepository
mover service    InsumoService MovimentacaoService
mover controller InsumoController MovimentacaoController HomeController
mover dto        InsumoRequest InsumoResponse MovimentacaoRequest MovimentacaoResponse
mover exception  RecursoNaoEncontradoException RegraNegocioException ConflitoException ErroResposta GlobalExceptionHandler

for f in model/*.java repository/*.java service/*.java controller/*.java dto/*.java exception/*.java; do
  sed -i '/^package /a import br.com.cervejaria.estoque.model.*;\nimport br.com.cervejaria.estoque.repository.*;\nimport br.com.cervejaria.estoque.service.*;\nimport br.com.cervejaria.estoque.controller.*;\nimport br.com.cervejaria.estoque.dto.*;\nimport br.com.cervejaria.estoque.exception.*;' "$f"
done

echo "Pronto! Estrutura organizada."
