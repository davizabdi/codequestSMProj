# CodeQuest

Protótipo de plataforma de aprendizado de programação (Java 17 + Javalin 6 + Thymeleaf + PostgreSQL).

## Como rodar

1. Suba o banco de dados local (precisa de Docker):
   ```bash
   docker compose up -d
   ```

2. Abra o projeto no IntelliJ (ele reconhece o `pom.xml` automaticamente e baixa as dependências).

3. Rode a classe `com.codequest.App`.

4. Acesse:
   - Site: http://localhost:7000
   - Login: http://localhost:7000/login
   - Módulos (público): http://localhost:7000/modulos
   - **Admin de módulos (CRUD):** http://localhost:7000/admin/modulos

## Variáveis de ambiente (opcional)

Por padrão, conecta em `localhost:5432/codequest` com usuário/senha `postgres`/`postgres`
(igual o `docker-compose.yml`). Para usar outro banco, defina antes de rodar:

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=codequest
DB_USER=postgres
DB_PASSWORD=postgres
```

## Estrutura

```
src/main/java/com/codequest/
  Main.java                 -> ponto de entrada, registra as rotas
  config/Database.java      -> HikariCP + migrations (CREATE TABLE IF NOT EXISTS)
  model/                    -> Modulo, Questao
  repository/                -> acesso a dados via JDBC puro
  service/                   -> validações de negócio
  controller/                 -> handlers das rotas Javalin (admin CRUD)

src/main/resources/
  templates/                 -> páginas públicas (index, login, modulos)
  templates/admin/            -> CRUD de módulos e questões
  templates/fragments/        -> navbar e footer reutilizados via th:replace
  static/css/style.css        -> tema visual (dark + verde terminal)
```

## O que já tem CRUD completo

- **Módulos**: criar, listar, editar, excluir (`/admin/modulos`)
- **Questões**: criar, listar, editar, excluir, vinculadas a um módulo (`/admin/modulos/{id}/questoes`)

A página pública `/modulos` já lê os módulos direto do banco (não é mais estático).

## Próximos passos sugeridos

- CRUD de usuários + autenticação real (login/cadastro ainda são só telas, sem persistência)
- Página de "Conteúdo" do módulo e execução do quiz de fato (usando as questões cadastradas)
- Registro de progresso do aluno por módulo
