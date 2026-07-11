# URL Shortener

Uma aplicação backend desenvolvida em **Spring Boot** que permite encurtar URLs longas em identificadores únicos e compactos. A aplicação utiliza MongoDB como banco de dados e a biblioteca **Sqids** para geração de códigos únicos e seguros.

## 📋 Visão Geral

O URL Shortener é uma API REST que oferece funcionalidade para:
- **Encurtar URLs**: Transforma URLs longas em URLs curtas usando identificadores únicos
- **Redirecionar**: Redireciona usuários para a URL original usando o identificador curto
- **Expiração de URLs**: URLs têm tempo de expiração de 60 minutos por padrão

## 🛠️ Tecnologias Utilizadas

- **Java 25**
- **Spring Boot 4.1.0**
- **Spring Data MongoDB**
- **Spring Validation**
- **MongoDB**
- **Sqids** - Biblioteca para gerar IDs únicos e seguros
- **Apache Commons Lang 3.20.0**
- **Lombok** - Geração automática de getters/setters
- **Maven** - Gerenciador de dependências

## 📋 Pré-requisitos

- Java 25 ou superior
- Docker e Docker Compose (para executar MongoDB)
- Maven 3.6+
- Porta 8081 disponível (padrão da aplicação)
- Porta 27017 disponível (padrão do MongoDB)

## 🚀 Como Executar

### 1. Iniciando o MongoDB com Docker Compose

```bash
cd docker
docker-compose up -d
```

Este comando iniciará um container MongoDB com as seguintes credenciais:
- **Username**: admin
- **Password**: 123
- **Database**: shortenerdb
- **Porta**: 27017

### 2. Compilando o Projeto

```bash
mvn clean install
```

### 3. Executando a Aplicação

#### Opção A: Via Maven
```bash
mvn spring-boot:run
```

#### Opção B: Após compilar, executar o JAR
```bash
mvn clean package
java -jar target/url-shortener-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: `http://localhost:8081`

## 📚 Endpoints da API

### 1. Encurtar URL

**Endpoint:**
```
POST /shorten-url
```

**Descrição:** Cria uma URL curta a partir de uma URL longa fornecida.

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "url": "https://www.exemplo.com/caminho/muito/longo/que/voce/quer/encurtar"
}
```

**Response (200 OK):**
```json
{
  "url": "http://localhost:8081/X49C7"
}
```

**Validações:**
- O campo `url` é obrigatório
- O campo `url` não pode estar vazio (NotBlank)

**Exemplo com cURL:**
```bash
curl -X POST http://localhost:8081/shorten-url \
  -H "Content-Type: application/json" \
  -d '{"url":"https://www.google.com"}'
```

**Exemplo com JavaScript/Fetch:**
```javascript
fetch('http://localhost:8081/shorten-url', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    url: 'https://www.google.com'
  })
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Erro:', error));
```

---

### 2. Redirecionar para URL Original

**Endpoint:**
```
GET /{id}
```

**Descrição:** Redireciona o usuário para a URL original associada ao identificador curto.

**Path Parameters:**
- `id` (string, obrigatório): Identificador único da URL encurtada

**Response (302 Found):**
Redireciona para a URL original com status HTTP 302 (Found)

**Response (404 Not Found):**
Retorna 404 se o identificador não existir ou a URL tiver expirado.

**Exemplo com cURL:**
```bash
curl -i http://localhost:8081/X49C7
```

**Exemplo com JavaScript:**
```javascript
// Nota: O navegador seguirá automaticamente o redirecionamento
window.location.href = 'http://localhost:8081/X49C7';
```

---

## 📊 Estrutura de Dados

### UrlEntity (Documento MongoDB)

A entidade armazenada no MongoDB possui a seguinte estrutura:

```json
{
  "_id": "X49C7",
  "fullUrl": "https://www.exemplo.com/caminho/muito/longo",
  "expiresAt": "2024-08-11T15:30:00"
}
```

**Campos:**
- **_id** (String): Identificador único (código curto gerado pelo Sqids)
- **fullUrl** (String): URL original completa que será encurtada
- **expiresAt** (LocalDateTime): Data e hora de expiração da URL curta (padrão: 60 minutos após criação)

---

## ⚙️ Configuração da Aplicação

As configurações da aplicação estão no arquivo `src/main/resources/application.properties`:

```properties
spring.application.name=url-shortener
server.port=8081

# MongoDB Config
spring.mongodb.authentication-database=admin
spring.mongodb.auto-index-creation=true
spring.mongodb.host=localhost
spring.mongodb.port=27017
spring.mongodb.database=shortenerdb
spring.mongodb.username=admin
spring.mongodb.password=123
```

### Modificando Configurações

Para usar um banco de dados diferente ou mudar a porta, edite o arquivo `application.properties` antes de executar a aplicação.

---

## 🏗️ Arquitetura do Projeto

```
url-shortener/
├── src/
│   ├── main/
│   │   ├── java/tech/buildrun/url_shortener/
│   │   │   ├── UrlShortenerApplication.java       # Classe principal
│   │   │   ├── controller/
│   │   │   │   └── UrlController.java             # Endpoints da API
│   │   │   ├── service/
│   │   │   │   └── UrlService.java                # Lógica de negócio
│   │   │   ├── repository/
│   │   │   │   └── UrlRepository.java             # Acesso ao MongoDB
│   │   │   ├── entity/
│   │   │   │   └── UrlEntity.java                 # Modelo de dados
│   │   │   ├── dto/
│   │   │   │   ├── ShortenUrlRequest.java         # DTO de requisição
│   │   │   │   ├── ShortenUrlResponse.java        # DTO de resposta
│   │   │   │   └── ExceptionDTO.java              # DTO de erro
│   │   │   └── config/
│   │   │       ├── GlobalExceptionHandler.java    # Tratamento global de exceções
│   │   │       └── MongoConfig.java               # Configuração do MongoDB
│   │   └── resources/
│   │       └── application.properties             # Configurações
│   └── test/
│       └── java/tech/buildrun/url_shortener/
│           └── UrlShortenerApplicationTests.java  # Testes
├── docker/
│   └── docker-compose.yml                          # Configuração do Docker Compose
└── pom.xml                                         # Dependências Maven
```

---

## 🔍 Detalhes da Geração de Códigos Únicos

A aplicação utiliza a biblioteca **Sqids** para gerar identificadores únicos e compactos com as seguintes características:

- **Alfabeto customizado**: `X49C7K2WDSA3Q8RE6ZJVPTN5FGYBMH`
- **Comprimento mínimo**: 5 caracteres
- **Algoritmo**: Sqids (seguro e amigável para URLs)

Os códigos são gerados baseados no timestamp (System.currentTimeMillis()), garantindo unicidade mesmo em ambiente multi-thread.

---

## 🐛 Tratamento de Erros

A aplicação implementa um tratamento global de exceções através do `GlobalExceptionHandler`.

**Respostas de erro comuns:**

1. **Validação fallha** (400 Bad Request):
```json
{
  "message": "URL cannot be empty",
  "status": 400,
  "timestamp": "2024-08-11T15:30:00"
}
```

2. **URL não encontrada** (404 Not Found):
```
HTTP/1.1 404 Not Found
```

---

## 📝 Fluxo da Aplicação

### Encurtar uma URL

```
1. Cliente envia POST /shorten-url com URL longa
   └─> UrlController.shortenUrl()
       ├─> UrlService.generateUniqueCode() [Gera código via Sqids]
       ├─> Cria UrlEntity com expiração de 60 minutos
       ├─> UrlService.save() [Salva no MongoDB]
       └─> Retorna URL encurtada (http://localhost:8081/{código})
```

### Redirecionar para URL Original

```
1. Cliente acessa GET /{id}
   └─> UrlController.redirect()
       ├─> UrlService.findById() [Procura no MongoDB]
       ├─> Se encontrado: Retorna 302 Found com redirect para URL original
       └─> Se não encontrado: Retorna 404 Not Found
```

---

## 🧪 Testes

Para executar os testes:

```bash
mvn test
```

A suite de testes está localizada em `src/test/java/tech/buildrun/url_shortener/`

---

## ⚠️ Notas Importantes

1. **Segurança**: As credenciais padrão do MongoDB (admin/123) são apenas para desenvolvimento. **Não use em produção!**

2. **Colisão de IDs**: O código atual usa `System.currentTimeMillis()` para gerar IDs, o que pode resultar em colisões em ambientes com alta concorrência. Para produção, considere usar um contador distribuído ou UUID.

3. **Expiração de URLs**: URLs expiram após 60 minutos da criação. Esta configuração está hardcoded no controlador e pode ser parametrizada em futuras melhorias.

4. **Persistência**: A aplicação depende de conexão com o MongoDB. Se o banco não estiver disponível, a aplicação falhará ao iniciar.

---

## 📖 Exemplos Práticos

### Exemplo Completo: Encurtar e Acessar URL

```bash
# 1. Encurtar a URL
curl -X POST http://localhost:8081/shorten-url \
  -H "Content-Type: application/json" \
  -d '{"url":"https://github.com/seu-usuario/seu-repo"}'

# Resposta:
# {"url":"http://localhost:8081/X49C7"}

# 2. Acessar a URL encurtada (será redirecionado automaticamente)
curl -i http://localhost:8081/X49C7

# Resposta:
# HTTP/1.1 302 Found
# Location: https://github.com/seu-usuario/seu-repo
```

---

## 🤝 Contribuindo

Para contribuir com melhorias:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto é fornecido como está, sem licença específica definida.

---

## 📧 Suporte

Para dúvidas ou problemas, abra uma issue no repositório ou entre em contato com o desenvolvedor.

---

## 🚀 Futuras Melhorias

- [ ] Implementar sistema de fila para geração de IDs (Redis)
- [ ] Adicionar autenticação e autorização (JWT)
- [ ] Criar dashboard para estatísticas de acessos
- [ ] Implementar rate limiting
- [ ] Adicionar suporte a múltiplos bancos de dados
- [ ] Criar documentação Swagger/OpenAPI
- [ ] Implementar testes de integração mais robustos
- [ ] Adicionar cache (Redis) para URLs frequentemente acessadas
- [ ] Implementar lógica de limpeza de URLs expiradas

---