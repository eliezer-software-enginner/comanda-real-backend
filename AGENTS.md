# AGENTS.md

This file contains guidelines and commands for agentic coding agents working in this Spring Boot backend repository.

## Development Commands

### Core Commands
- `./gradlew bootRun` - Start Spring Boot application
- `./gradlew build` - Build the application
- `./gradlew test` - Run tests
- `./gradlew bootJar` - Create executable JAR

### Code Quality Commands
- `./gradlew check` - Run all checks including tests
- `./gradlew spotlessCheck` - Check code formatting
- `./gradlew spotlessApply` - Apply code formatting

## Code Style Guidelines

### DTOs and Records
- **SEMPRE** usar `record` para DTOs em vez de classes tradicionais
- Records devem ser imutáveis e auto-gerar equals(), hashCode(), toString()
- Exemplo:
```java
public record PedidoDto(String nomeCliente, String numero, List<ItemDto> produtos, double total) {
    // Construtor compact record
}

public record ItemDto(String nome, int quantidade, double preco) {
    // Record aninhado para itens
}
```

### Naming Conventions
- **Files**: PascalCase (WhatsAppService.java, PedidoController.java)
- **Classes**: PascalCase (WhatsAppService, PedidoController)
- **Methods**: camelCase (enviarPedidoSimulado, formatarMensagem)
- **Variables**: camelCase
- **Constants**: UPPER_SNAKE_CASE for static values
- **Packages**: lowercase (comandareal.app.controller, comandareal.app.dto)

### Service Layer Patterns
- Use `@Service` annotation for service classes
- Inject dependencies via constructor injection
- Use `@Value` for configuration properties
- Handle exceptions appropriately and log errors

### Controller Patterns
- Use `@RestController` with `@RequestMapping`
- Return `ResponseEntity` for proper HTTP responses
- Use DTOs for request/response objects
- Handle validation errors properly

### Spring Boot Configuration
- Use `application.properties` for configuration with environment variables
- Externalize sensitive data (tokens, URLs) using `.env` files
- **NUNCA** commitar credenciais no repositório
- Usar variáveis de ambiente: `${VAR_NAME:default_value}`
- Copiar `.env.example` para `.env` com suas credenciais
- Arquivos `.env` já estão no `.gitignore`

### API Integration
- Use `RestTemplate` for HTTP calls to external APIs
- Handle HTTP response codes properly
- Set appropriate headers (Bearer token, Content-Type)
- Log API calls and responses

### Error Handling
- Use descriptive Portuguese error messages
- Log errors with proper context
- Return meaningful HTTP status codes
- Wrap checked exceptions in RuntimeException when appropriate

### Project Structure
- `src/main/java/comandareal/app/` - Main application code
- `src/main/java/comandareal/app/controller/` - REST controllers
- `src/main/java/comandareal/app/service/` - Business logic
- `src/main/java/comandareal/app/dto/` - Data Transfer Objects (records)
- `src/main/resources/` - Configuration files
- `src/test/java/` - Test classes

### Testing Guidelines
- Unit tests for service classes
- Integration tests for controllers
- Mock external dependencies
- Test both success and error scenarios

## Development Workflow
1. Configure Evolution API in `application.properties`
2. Start backend: `./gradlew bootRun`
3. Test endpoints with curl or Postman
4. Check logs for API calls and errors
5. Run tests: `./gradlew test`

## API Endpoints
- `POST /api/whatsapp/simular-pedido` - Test pedido simulation
- `POST /api/whatsapp/enviar-pedido` - Send pedido to specific number

## Important Notes
- Backend runs on port 8081 (Evolution API uses 8080)
- Use records for all DTOs
- Numbers should come from pedido object
- Always remove "55" prefix from phone numbers when needed