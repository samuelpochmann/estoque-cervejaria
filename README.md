# 🍺 Estoque Cervejaria

API REST para controle de estoque de insumos de uma microcervejaria caseira. Cadastre malte, lúpulo, levedura e outros ingredientes, registre entradas e saídas, e acompanhe o saldo de cada item em tempo real.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-build-C71A36?logo=apachemaven&logoColor=white)

---

## ✨ Funcionalidades

- **CRUD completo de insumos** (malte, lúpulo, levedura, adjuntos e outros)
- **Registro de movimentações** de entrada e saída, com observação (ex.: *"Brassagem IPA #3"*)
- **Saldo atualizado automaticamente** a cada movimentação
- **Bloqueio de estoque negativo**: não deixa registrar saída maior que o saldo
- **Estorno de movimentações**, desfazendo o efeito no saldo
- **Alerta de estoque baixo**: lista os insumos abaixo do mínimo definido
- **Validação de dados** nas requisições

## 🧱 Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot (Web, Data JPA, Validation) |
| Banco de dados | PostgreSQL |
| Build | Maven (Maven Wrapper incluso) |
| Utilitários | Lombok |

## 📋 Pré-requisitos

- JDK 21
- Git
- PostgreSQL

> O Maven não precisa estar instalado: o projeto já inclui o `./mvnw`.

### Instalação no CachyOS / Arch Linux

```bash
sudo pacman -S jdk21-openjdk git postgresql

# conferir a versão do Java
java -version
```

Inicialize e habilite o PostgreSQL (apenas na primeira vez):

```bash
sudo -iu postgres initdb --locale=C.UTF-8 -E UTF8 -D /var/lib/postgres/data
sudo systemctl enable --now postgresql
```

Crie o usuário e o banco:

```bash
sudo -iu postgres psql
```

```sql
CREATE USER cerveja WITH PASSWORD 'cerveja123';
CREATE DATABASE cervejaria OWNER cerveja;
\q
```

## 🚀 Como rodar

### Com Docker (recomendado)

Requer apenas Docker e Git.

```bash
git clone https://github.com/SEU_USUARIO/estoque-cervejaria.git
cd estoque-cervejaria
docker compose up -d --build
```

A API sobe em http://localhost:8080.

Para desenvolver, suba só o banco e rode a aplicação pela IDE:

```bash
docker compose up -d db
./mvnw spring-boot:run
```

## 📡 Endpoints

### Insumos

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/insumos` | Lista todos os insumos |
| `GET` | `/insumos/{id}` | Busca um insumo |
| `GET` | `/insumos/abaixo-do-minimo` | Lista insumos com estoque abaixo do mínimo |
| `POST` | `/insumos` | Cadastra um insumo |
| `PUT` | `/insumos/{id}` | Atualiza os dados de um insumo |
| `DELETE` | `/insumos/{id}` | Exclui um insumo (somente se não tiver movimentações) |

### Movimentações

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/movimentacoes` | Histórico, da mais recente para a mais antiga |
| `GET` | `/movimentacoes/{id}` | Busca uma movimentação |
| `POST` | `/movimentacoes` | Registra uma entrada ou saída |
| `DELETE` | `/movimentacoes/{id}` | Estorna a movimentação e corrige o saldo |

### Tipos aceitos

- **Tipo de insumo:** `MALTE`, `LUPULO`, `LEVEDURA`, `ADJUNTO`, `OUTRO`
- **Tipo de movimentação:** `ENTRADA`, `SAIDA`

## 🧪 Exemplos de uso

**Cadastrar um insumo**

```bash
curl -X POST localhost:8080/insumos -H 'Content-Type: application/json' \
  -d '{"nome":"Malte Pilsen","tipo":"MALTE","unidade":"kg","estoqueMinimo":5}'
```

**Registrar uma entrada de 25 kg**

```bash
curl -X POST localhost:8080/movimentacoes -H 'Content-Type: application/json' \
  -d '{"insumoId":1,"tipo":"ENTRADA","quantidade":25,"observacao":"Compra fornecedor"}'
```

**Registrar uma saída de 4,5 kg**

```bash
curl -X POST localhost:8080/movimentacoes -H 'Content-Type: application/json' \
  -d '{"insumoId":1,"tipo":"SAIDA","quantidade":4.5,"observacao":"Brassagem Pilsen #1"}'
```

**Conferir o saldo** (deve retornar `quantidadeAtual: 20.5`)

```bash
curl localhost:8080/insumos/1
```

**Ver o que está acabando**

```bash
curl localhost:8080/insumos/abaixo-do-minimo
```

## 🗂️ Estrutura do projeto

```
src/main/java/br/com/cervejaria/estoque/
├── Insumo.java                 # entidade
├── Movimentacao.java           # entidade
├── TipoInsumo.java             # enum
├── TipoMovimentacao.java       # enum
├── MovimentacaoRequest.java    # DTO de entrada
├── InsumoRepository.java
├── MovimentacaoRepository.java
├── MovimentacaoService.java    # regras de negócio do estoque
├── InsumoController.java
└── MovimentacaoController.java
```

## 💡 Decisões de projeto

- **O saldo não é editável diretamente.** `quantidadeAtual` só muda por meio de movimentações, garantindo rastreabilidade.
- **Movimentações não são editadas, e sim estornadas.** É a prática comum em controle de estoque: estorna-se e lança-se novamente.
- **Operações transacionais.** Saldo e movimentação são gravados juntos, ou nenhum dos dois.
- **`ddl-auto=update` é só para desenvolvimento.** Para produção, o ideal é versionar o schema com Flyway.

## 🗺️ Roadmap

- [x] Interface web para uso sem `curl`
- [x] Docker Compose com a aplicação e o PostgreSQL
- [ ] Migrações com Flyway
- [ ] Lote, validade, fornecedor e custo unitário
- [ ] Cadastro de receitas com baixa automática de insumos na brassagem
- [ ] Testes automatizados (JUnit + Testcontainers)
- [ ] Documentação interativa com Swagger/OpenAPI

## 🤝 Contribuindo

Este é um projeto de estudo, mas sugestões são bem-vindas. Abra uma *issue* ou envie um *pull request*.

## 📄 Licença

Distribuído sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

Feito com 🍻 para quem gosta de fazer a própria cerveja.
