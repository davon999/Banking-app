# Banking App

A banking application with a REST API built in Java and Spring Boot, using PostgreSQL for storage and a simple web interface.

![Banking App screenshot](docs/screenshot.png)

## Features

- Open bank accounts (RON, EUR, USD) with automatically generated IBANs
- View account details
- Deposit and withdraw money
- Transfer money between accounts
- Clear error messages for invalid operations (insufficient funds, unknown IBAN, blocked account, invalid amounts)
- Simple web interface for all operations, available at `http://localhost:8080`

## Tech stack

- Java 25
- Spring Boot (Web MVC, Data JPA)
- PostgreSQL
- Maven
- HTML, CSS and JavaScript for the web interface

## Architecture

```
Browser / client  →  Controller  →  Service  →  Repository  →  PostgreSQL
```

- **Controller**: receives HTTP requests and returns JSON responses
- **Service**: business logic (validation, transfers)
- **Repository**: database access through Spring Data JPA
- **Error handler**: turns exceptions into clear HTTP error responses

## How to run

1. Create a PostgreSQL database named `banking`
2. Run `src/main/resources/schema.sql` on it to create the tables
3. Set the environment variable `DB_PASSWORD` to your PostgreSQL password
4. Start the application (`BankingApplication`)
5. Open `http://localhost:8080` in your browser to use the web interface, or call the API directly (see below)

## API

| Method | Endpoint | Description |
|---|---|---|
| POST | `/accounts` | Open a new account |
| GET | `/accounts/{iban}` | Get account details |
| POST | `/accounts/{iban}/deposit` | Deposit money |
| POST | `/accounts/{iban}/withdraw` | Withdraw money |
| POST | `/accounts/transfer` | Transfer money between accounts |

### Example

Request:

```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"ownerName":"David Poturlu","currency":"RON"}'
```

Response:

```json
{
  "id": "3f2b8c1e-9a4d-4e7b-b6f1-2c8d5e9a7b03",
  "iban": "RO49DAVB0000000003",
  "ownerName": "David Poturlu",
  "currency": "RON",
  "balance": 0,
  "status": "ACTIVE"
}
```

Error example (withdrawing more than the balance):

```json
{
  "error": "Insufficient funds: requested 5000, available 1000.00"
}
```

## Design decisions

- **`BigDecimal` for money**: avoids the rounding errors of `double`
- **Custom exceptions** (`InsufficientFundsException`, `AccountNotFoundException`), mapped to clear HTTP error responses (400, 404)
- **`@Transactional` transfers**: if any step fails, the whole transfer is rolled back and no money is lost
- **Database constraints** (`UNIQUE`, `NOT NULL`, `CHECK balance >= 0`) as a second layer of protection
- **Database password in an environment variable**, never committed to the repository

## Possible improvements

- Transaction history for each account
- User authentication
- Automated tests