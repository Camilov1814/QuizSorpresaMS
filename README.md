# Microservicios - E-commerce de Enanos 🏔️

Arquitectura de microservicios con Spring Boot, Spring Cloud, Eureka, Gateway, OpenFeign, Resilience4j y Docker.

---

## 📋 Tabla de Contenidos

- [Descripción del Proyecto](#-descripción-del-proyecto)
- [Arquitectura](#-arquitectura)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
  - [Opción 1: Con Docker (Recomendado)](#opción-1-con-docker-recomendado)
  - [Opción 2: Local sin Docker](#opción-2-local-sin-docker)
- [Endpoints API](#-endpoints-api)
- [Pruebas con Postman](#-pruebas-con-postman)
- [Resiliencia (Circuit Breaker)](#-resiliencia-circuit-breaker)
- [Monitoreo](#-monitoreo)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Variables de Entorno](#-variables-de-entorno)
- [Solución de Problemas](#-solución-de-problemas)
- [Autor](#-autor)

---

## 📖 Descripción del Proyecto

Sistema de e-commerce para comprar enanos (medianos) implementado con arquitectura de microservicios. El proyecto incluye:

- **3 Microservicios independientes** con bases de datos separadas
- **Service Discovery** con Eureka Server
- **API Gateway** con Spring Cloud Gateway
- **Comunicación entre servicios** con OpenFeign
- **Resiliencia** con Resilience4j (Circuit Breaker, Retry, Timeout)
- **Observabilidad** con Spring Boot Actuator
- **Contenerización** con Docker y docker-compose

---

## 🏗️ Arquitectura
```
                    ┌─────────────────┐
                    │  Eureka Server  │
                    │   (port 8761)   │
                    └────────┬────────┘
                             │
                             │ Service Discovery
                             │
                    ┌────────┴────────┐
                    │     Gateway     │
                    │   (port 8080)   │
                    └────────┬────────┘
                             │
           ┌─────────────────┼─────────────────┐
           │                 │                 │
    ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐
    │   Enanos    │  │   Photos    │  │   Orders    │
    │  (8081)     │◄─┤  (8082)     │  │  (8083)     │
    └──────┬──────┘  └──────┬──────┘  └──────┬──────┘
           │                │                 │
           │                │                 │
    ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐
    │  enanos_    │  │  photos_    │  │  orders_    │
    │  schema     │  │  schema     │  │  schema     │
    └─────────────┘  └─────────────┘  └─────────────┘
           PostgreSQL (port 5432)
```

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 21 | Lenguaje de programación |
| Spring Boot | 3.2.5 | Framework base |
| Spring Cloud | 2023.0.1 | Microservicios |
| Eureka Server | - | Service Discovery |
| Spring Cloud Gateway | - | API Gateway |
| OpenFeign | - | Comunicación entre servicios |
| Resilience4j | - | Circuit Breaker, Retry, Timeout |
| PostgreSQL | 15 | Base de datos |
| Docker | - | Contenerización |
| Docker Compose | - | Orquestación |
| Lombok | - | Reducir boilerplate |
| Spring Boot Actuator | - | Monitoreo y métricas |

---

## 📦 Requisitos Previos

### Para ejecutar con Docker (Recomendado):
- Docker Desktop instalado
- Docker Compose
- 8 GB RAM mínimo

### Para ejecutar localmente:
- Java 21 (JDK)
- Maven 3.8+
- PostgreSQL 15+
- 8 GB RAM mínimo

---

## 🚀 Instalación y Ejecución

### Opción 1: Con Docker (Recomendado)

#### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/microservicios-enanos.git
cd microservicios-enanos
```

#### 2. Crear archivo `.env`

Crea un archivo `.env` en la raíz:
```env
DB_USERNAME=postgres
DB_PASSWORD=postgres123
```

#### 3. Construir las imágenes
```bash
docker-compose build
```

⏱️ Esto tomará varios minutos la primera vez.

#### 4. Iniciar los servicios
```bash
docker-compose up -d
```

#### 5. Verificar que todo esté corriendo
```bash
docker-compose ps
```

**Deberías ver:**
```
NAME                      STATUS
microservicios-postgres   Up (healthy)
eureka-server             Up (healthy)
gateway-service           Up (healthy)
enanos-service            Up (healthy)
photos-service            Up (healthy)
orders-service            Up (healthy)
```

#### 6. Acceder a Eureka Dashboard
```
http://localhost:8761
```

**Espera 1-2 minutos** a que todos los servicios se registren.

---

### Opción 2: Local sin Docker

#### 1. Configurar PostgreSQL
```sql
-- Conectarse a PostgreSQL
psql -U postgres

-- Crear base de datos
CREATE DATABASE microservicios_db;

-- Conectarse a la base de datos
\c microservicios_db

-- Crear esquemas
CREATE SCHEMA IF NOT EXISTS enanos_schema;
CREATE SCHEMA IF NOT EXISTS photos_schema;
CREATE SCHEMA IF NOT EXISTS orders_schema;
```

#### 2. Configurar variables de entorno

En cada `application.yml` actualiza:
```yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/microservicios_db?currentSchema=SCHEMA_NAME
    username: postgres
    password: tu_password
```

#### 3. Iniciar servicios en orden

**Terminal 1 - Eureka Server:**
```bash
cd eureka-server
mvn spring-boot:run
```

**Terminal 2 - Gateway:**
```bash
cd gateway-service
mvn spring-boot:run
```

**Terminal 3 - Enanos Service:**
```bash
cd MedianoMS
mvn spring-boot:run
```

**Terminal 4 - Photos Service:**
```bash
cd PhotosMS
mvn spring-boot:run
```

**Terminal 5 - Orders Service:**
```bash
cd OrdersMS
mvn spring-boot:run
```

---

## 🌐 Endpoints API

### URL Base
```
http://localhost:8080
```

Todos los endpoints se acceden a través del **Gateway** (puerto 8080).

---

### 📊 Enanos Service

#### Crear un mediano
```http
POST /enanos/api/medianos
Content-Type: application/json

{
  "nombre": "Thorin Escudo de Roble",
  "altura": 145,
  "email": "thorin@erebor.com"
}
```

#### Obtener todos los medianos
```http
GET /enanos/api/medianos
```

#### Obtener mediano por nombre (con fotos)
```http
GET /enanos/api/medianos/filtrado/Thorin Escudo de Roble
```

**Respuesta:**
```json
{
  "id": "uuid",
  "nombre": "Thorin Escudo de Roble",
  "altura": 145.0,
  "email": "thorin@erebor.com",
  "photos": [
    {
      "id": "uuid",
      "path": "https://example.com/thorin.jpg",
      "description": "Rey bajo la montaña",
      "medianoName": "Thorin Escudo de Roble",
      "created": "2025-10-24"
    }
  ]
}
```

#### Verificar si un mediano existe
```http
GET /enanos/api/medianos/existe/Thorin Escudo de Roble
```

---

### 📸 Photos Service

#### Crear una foto
```http
POST /photos/api/photos
Content-Type: application/json

{
  "url": "https://example.com/thorin.jpg",
  "descripcion": "Thorin, Rey bajo la Montaña",
  "medianoName": "Thorin Escudo de Roble"
}
```

#### Obtener todas las fotos
```http
GET /photos/api/photos
```

#### Obtener fotos de un mediano específico
```http
GET /photos/api/photos/mediano/Thorin Escudo de Roble
```

---

### 🛒 Orders Service

#### Crear una orden
```http
POST /orders/api/orders
Content-Type: application/json

{
  "buyerName": "Bilbo Bolsón",
  "buyerEmail": "bilbo@shire.com",
  "medianoName": "Thorin Escudo de Roble",
  "quantity": 2
}
```

**Respuesta:**
```json
{
  "id": "uuid",
  "buyerName": "Bilbo Bolsón",
  "buyerEmail": "bilbo@shire.com",
  "medianoName": "Thorin Escudo de Roble",
  "quantity": 2,
  "totalPrice": 200.0,
  "status": "PENDIENTE",
  "createdAt": "2025-10-24T23:00:00"
}
```

#### Obtener todas las órdenes
```http
GET /orders/api/orders
```

#### Obtener orden por ID
```http
GET /orders/api/orders/{orderId}
```

#### Obtener órdenes por email del comprador
```http
GET /orders/api/orders/buyer/bilbo@shire.com
```

#### Obtener órdenes de un mediano
```http
GET /orders/api/orders/mediano/Thorin Escudo de Roble
```

#### Actualizar estado de una orden
```http
PATCH /orders/api/orders/{orderId}/status?status=PAGADA
```

**Estados posibles:**
- `PENDIENTE`
- `PAGADA`
- `ENVIADA`
- `COMPLETADA`
- `CANCELADA`

---

## 🧪 Pruebas con Postman

### Flujo completo de prueba:

#### 1️⃣ Crear un mediano
```http
POST http://localhost:8080/enanos/api/medianos
Content-Type: application/json

{
  "nombre": "Balin",
  "altura": 142,
  "email": "balin@moria.com"
}
```

#### 2️⃣ Crear fotos del mediano
```http
POST http://localhost:8080/photos/api/photos
Content-Type: application/json

{
  "url": "https://example.com/balin1.jpg",
  "descripcion": "Balin en Moria",
  "medianoName": "Balin"
}
```
```http
POST http://localhost:8080/photos/api/photos
Content-Type: application/json

{
  "url": "https://example.com/balin2.jpg",
  "descripcion": "Balin con armadura",
  "medianoName": "Balin"
}
```

#### 3️⃣ Crear una orden
```http
POST http://localhost:8080/orders/api/orders
Content-Type: application/json

{
  "buyerName": "Gandalf",
  "buyerEmail": "gandalf@istari.com",
  "medianoName": "Balin",
  "quantity": 1
}
```

#### 4️⃣ Obtener mediano con sus fotos
```http
GET http://localhost:8080/enanos/api/medianos/filtrado/Balin
```

#### 5️⃣ Ver todas las órdenes
```http
GET http://localhost:8080/orders/api/orders
```

---

## 🔄 Resiliencia (Circuit Breaker)

El sistema implementa **Resilience4j** para manejar fallos entre servicios.

### Probar Circuit Breaker:

#### 1. Detener photos-service
```bash
docker-compose stop photos-service
```

#### 2. Obtener mediano con fotos
```http
GET http://localhost:8080/enanos/api/medianos/filtrado/Balin
```

**Resultado:** Devuelve el mediano **sin fotos** (modo degradado) ✅
```json
{
  "id": "uuid",
  "nombre": "Balin",
  "altura": 142.0,
  "email": "balin@moria.com",
  "photos": []
}
```

#### 3. Intentar crear una foto
```http
POST http://localhost:8080/photos/api/photos
Content-Type: application/json

{
  "url": "https://example.com/test.jpg",
  "descripcion": "Test",
  "medianoName": "Balin"
}
```

**Resultado:** Error 503 (servicio no disponible) ❌

#### 4. Reiniciar photos-service
```bash
docker-compose start photos-service
```

Espera 10-15 segundos y vuelve a probar. El sistema se auto-recupera ✅

---

## 📊 Monitoreo

### Eureka Dashboard
```
http://localhost:8761
```

Ver todos los servicios registrados y su estado.

---

### Health Checks

#### Gateway:
```http
GET http://localhost:8080/actuator/health
```

#### Enanos Service:
```http
GET http://localhost:8081/actuator/health
```

**Respuesta:**
```json
{
  "status": "UP",
  "components": {
    "circuitBreakers": {
      "photosClient": {
        "state": "CLOSED",
        "failureRate": "0.0%"
      }
    },
    "db": {
      "status": "UP"
    }
  }
}
```

#### Photos Service:
```http
GET http://localhost:8082/actuator/health
```

#### Orders Service:
```http
GET http://localhost:8083/actuator/health
```

---

## 📁 Estructura del Proyecto
```
microservicios-enanos/
├── eureka-server/              # Service Discovery
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── gateway-service/            # API Gateway
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── MedianoMS/                  # Enanos Service
│   ├── src/
│   │   └── main/resources/
│   │       ├── application.yml
│   │       └── application-docker.yml
│   ├── Dockerfile
│   └── pom.xml
├── PhotosMS/                   # Photos Service
│   ├── src/
│   │   └── main/resources/
│   │       ├── application.yml
│   │       └── application-docker.yml
│   ├── Dockerfile
│   └── pom.xml
├── OrdersMS/                   # Orders Service
│   ├── src/
│   │   └── main/resources/
│   │       ├── application.yml
│   │       └── application-docker.yml
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml          # Orquestación de servicios
├── .env                        # Variables de entorno
├── init-db.sql                 # Inicialización de BD
└── README.md                   # Este archivo
```

---

## ⚙️ Variables de Entorno

### Archivo `.env`
```env
# Database credentials
DB_USERNAME=postgres
DB_PASSWORD=postgres123
```

### Variables en docker-compose.yml

Cada servicio recibe:
- `SPRING_PROFILES_ACTIVE=docker` - Activa el perfil Docker
- `DB_USERNAME` - Usuario de la base de datos
- `DB_PASSWORD` - Contraseña de la base de datos

---

## 🐛 Solución de Problemas

### Puerto ya en uso

**Error:** `Bind for 0.0.0.0:8080 failed: port is already allocated`

**Solución:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

O cambia el puerto en `docker-compose.yml`:
```yaml
ports:
  - "8090:8080"  # Puerto host diferente
```

---

### Servicio no está healthy

**Verificar logs:**
```bash
docker-compose logs -f enanos-service
```

**Posibles causas:**
- Base de datos no conecta
- Eureka no disponible
- Error en configuración

**Solución:**
```bash
docker-compose restart enanos-service
```

---

### Los datos no persisten

**Problema:** Al hacer `docker-compose down` se pierden los datos.

**Solución:** Usa volúmenes (ya configurado):
```yaml
volumes:
  postgres_data:  # Persiste los datos
```

Para limpiar los datos:
```bash
docker-compose down -v  # Elimina volúmenes
```

---

### Rebuild después de cambios en código
```bash
docker-compose down
docker-compose build enanos-service  # Solo el que cambió
docker-compose up -d
```

---

### Ver logs en tiempo real
```bash
# Todos los servicios
docker-compose logs -f

# Un servicio específico
docker-compose logs -f enanos-service
```

---

### Conectarse a PostgreSQL
```bash
docker-compose exec postgres psql -U postgres -d microservicios_db
```

**Ver esquemas:**
```sql
\dn
```

**Ver tablas de un esquema:**
```sql
\dt enanos_schema.*
```

---

### Reiniciar todo desde cero
```bash
# Detener y eliminar todo (incluyendo datos)
docker-compose down -v

# Reconstruir
docker-compose build

# Iniciar
docker-compose up -d
```

---

## 📚 Comandos Útiles de Docker
```bash
# Ver estado de contenedores
docker-compose ps

# Ver logs
docker-compose logs -f

# Detener servicios
docker-compose stop

# Iniciar servicios detenidos
docker-compose start

# Reiniciar un servicio
docker-compose restart enanos-service

# Detener y eliminar contenedores
docker-compose down

# Detener y eliminar contenedores + volúmenes (limpia BD)
docker-compose down -v

# Reconstruir una imagen
docker-compose build enanos-service

# Ver uso de recursos
docker stats

# Ejecutar comando en contenedor
docker-compose exec postgres psql -U postgres

# Ver redes
docker network ls

# Inspeccionar un contenedor
docker inspect enanos-service
```

---

## 👤 Autor

**Tu Nombre**
- GitHub: [@tu-usuario](https://github.com/tu-usuario)
- Email: tu-email@example.com

---

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la [Licencia MIT](LICENSE).

---

## 🙏 Agradecimientos

- Spring Cloud Team
- Netflix OSS
- Resilience4j Team
- Comunidad de Spring Boot

---

## 📌 Notas Adicionales

### Características implementadas:
✅ Service Discovery con Eureka  
✅ API Gateway con enrutamiento  
✅ Circuit Breaker, Retry y Timeout  
✅ Comunicación entre servicios con OpenFeign  
✅ Bases de datos separadas por servicio  
✅ Health checks y actuator  
✅ Contenerización con Docker  
✅ Orquestación con docker-compose  
✅ Logging con trace-id  
✅ CORS configurado  

### Posibles mejoras futuras:
- [ ] Autenticación con JWT
- [ ] Config Server para configuración centralizada
- [ ] Mensajería asíncrona con RabbitMQ/Kafka
- [ ] Distributed Tracing con Zipkin
- [ ] Kubernetes deployment
- [ ] CI/CD con GitHub Actions

---

**⭐ Si te gustó este proyecto, dale una estrella en GitHub!**
