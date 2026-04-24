💸 Sistema de Transações Financeiras Simuladas

## 📌 Objetivo

Construir uma API REST em Java utilizando Spring Boot para simular transferências de dinheiro entre contas, garantindo:

- **Integridade** → uso de transações (`@Transactional`)
- **Confidencialidade** → autenticação e autorização com Spring Security
- **Disponibilidade** → arquitetura simples, resiliente e escalável

---

## 🧱 Requisitos Funcionais

- Criar conta com:
  - Nome
  - CPF (único, usado como chave)
- Toda conta inicia com saldo de **R$ 100**
- Transferência entre contas via CPF:
  - Débito de uma conta
  - Crédito em outra
- Consultar saldo
- Consultar histórico de transações

---

## 🔐 Requisitos Não Funcionais

- Senhas criptografadas (BCrypt)
- Autenticação (Basic Auth ou JWT)
- Controle de concorrência
- Transações atômicas com `@Transactional`

---

## 🏗️ Arquitetura

```text
Controller → Service → Repository → Database

📡 Endpoints
Criar conta
POST /accounts
Transferir dinheiro
POST /transfer
Content-Type: application/json

{
  "fromCpf": "12345678900",
  "toCpf": "98765432100",
  "amount": 50.00
}
Consultar saldo
GET /accounts/{cpf}
Histórico de transações
GET /transactions/{cpf}
