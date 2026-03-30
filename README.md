# 📋 Tasks API — GitHub Actions Demo

> API REST de gerenciamento de tarefas desenvolvida com **Spring Boot**, utilizada como projeto de demonstração do **GitHub Actions** em aula.

[![CI - Build & Test](https://github.com/SEU_USUARIO/spring-tasks-api/actions/workflows/ci.yml/badge.svg)](https://github.com/SEU_USUARIO/spring-tasks-api/actions/workflows/ci.yml)
[![CD - Deploy & Release](https://github.com/SEU_USUARIO/spring-tasks-api/actions/workflows/cd.yml/badge.svg)](https://github.com/SEU_USUARIO/spring-tasks-api/actions/workflows/cd.yml)
[![Code Quality - Checkstyle](https://github.com/SEU_USUARIO/spring-tasks-api/actions/workflows/code-quality.yml/badge.svg)](https://github.com/SEU_USUARIO/spring-tasks-api/actions/workflows/code-quality.yml)

---

## 📌 Sobre o Projeto

Este repositório demonstra dois recursos fundamentais do **GitHub Actions**:

| Workflow | Arquivo | Gatilho | O que faz |
|----------|---------|---------|-----------|
| **CI - Build & Test** | `.github/workflows/ci.yml` | Push ou PR para `main`/`develop` | Compila o projeto, executa testes unitários e de integração em paralelo |
| **CD - Deploy & Release** | `.github/workflows/cd.yml` | Push de tag `v*.*.*` | Executa testes, gera JAR versionado, cria GitHub Release e simula deploy |
| **Code Quality** | `.github/workflows/code-quality.yml` | Push ou PR para `main`/`develop` | Analisa o código com Checkstyle — nomenclatura, formatação e boas práticas |

---

## 🏗️ Arquitetura da API

```
tasks-api/
├── .github/
│   └── workflows/
│       ├── ci.yml          # Workflow de CI
│       └── cd.yml          # Workflow de CD
├── src/
│   ├── main/java/com/example/tasks/
│   │   ├── controller/     TaskController.java
│   │   ├── model/          Task.java
│   │   ├── repository/     TaskRepository.java
│   │   └── service/        TaskService.java
│   └── test/java/com/example/tasks/
│       ├── controller/     TaskControllerTest.java   (integração)
│       └── service/        TaskServiceTest.java      (unitário)
└── pom.xml
```

---

## 🚀 Como Rodar Localmente

**Pré-requisitos:** Java 17+, Maven 3.8+

```bash
# Clonar o repositório
git clone https://github.com/SEU_USUARIO/spring-tasks-api.git
cd spring-tasks-api

# Executar a aplicação
mvn spring-boot:run

# A API estará disponível em http://localhost:8080
```

---

## 🔌 Endpoints da API

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/tasks` | Listar todas as tarefas |
| `GET` | `/api/tasks?status=PENDING` | Filtrar por status |
| `GET` | `/api/tasks?search=bug` | Buscar por título |
| `GET` | `/api/tasks/{id}` | Buscar tarefa por ID |
| `POST` | `/api/tasks` | Criar nova tarefa |
| `PUT` | `/api/tasks/{id}` | Atualizar tarefa |
| `DELETE` | `/api/tasks/{id}` | Remover tarefa |

### Exemplo de Payload

```json
{
  "title": "Corrigir bug de autenticação",
  "description": "Falha no login via OAuth2",
  "status": "IN_PROGRESS"
}
```

**Status disponíveis:** `PENDING` | `IN_PROGRESS` | `DONE`

---

## ⚙️ GitHub Actions: Como Acionar

### CI (automático)
```bash
git add .
git commit -m "feat: nova funcionalidade"
git push origin main   # dispara o workflow CI automaticamente
```

### CD (ao criar uma tag de versão)
```bash
git tag v1.0.0
git push origin v1.0.0  # dispara o workflow CD automaticamente
```

---

## 🧪 Executar Testes Localmente

```bash
# Todos os testes
mvn test

# Apenas testes unitários
mvn test -Dtest=TaskServiceTest

# Apenas testes de integração
mvn test -Dtest=TaskControllerTest
```

---

## 📚 Referências

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [actions/setup-java](https://github.com/actions/setup-java)
- [softprops/action-gh-release](https://github.com/softprops/action-gh-release)
