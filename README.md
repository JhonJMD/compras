# Sistema de Gestión de Compras 🛒

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/License-MIT-green)

> Una API RESTful desarrollada con Spring Boot para gestionar compras, productos, categorías y clientes.

## 📝Descripción

Este sistema de gestión permite la administración completa del proceso de compras, desde el mantenimiento de catálogos de productos organizados por categorías, hasta la gestión de clientes y sus transacciones. La aplicación implementa una arquitectura basada en principios de Clean Architecture y sigue patrones de diseño REST.

## 🛠️ Tecnologías

- **Backend**: Java 17, Spring Boot 3.3.4
- **Persistencia**: Spring Data JPA, Hibernate
- **Base de Datos**: MySQL 8
- **Herramientas de Desarrollo**: Maven, Lombok
- **Testing**: JUnit 5
- **Documentación**: OpenAPI/Swagger

## 📁 Estructura del Proyecto

El proyecto sigue los principios de Clean Architecture con las siguientes capas:

### Organización de Paquetes

```
com.app.compras/
├── application/
│   └── services/              # Interfaces que definen la lógica de negocio
│       ├── ICategoriaService.java
│       ├── IClienteService.java
│       ├── ICompraProductoService.java
│       ├── ICompraService.java
│       └── IProductoService.java
├── domain/
│   └── entity/                # Entidades de dominio (modelos JPA)
│       ├── Categoria.java
│       ├── Cliente.java
│       ├── Compra.java
│       ├── CompraProducto.java
│       ├── CompraProductoId.java
│       └── Producto.java
├── infrastructure/
│   ├── config/                # Configuraciones
│   │   └── OpenApiConfig.java 
│   ├── controllers/           # Controladores REST
│   │   ├── CategoriaController.java
│   │   ├── ClienteController.java
│   │   ├── CompraController.java
│   │   ├── CompraProductoController.java
│   │   └── ProductoController.java
│   └── repositories/          # Repositorios e implementaciones
│       ├── CategoriaRepository.java
│       ├── ClienteRepository.java
│       ├── CompraProductoRepository.java
│       ├── CompraRepository.java
│       ├── ProductoRepository.java
│       └── implement/         # Implementaciones de servicios
│           ├── CategoriaImpl.java
│           ├── ClienteImpl.java
│           ├── CompraImpl.java
│           ├── CompraProductoImpl.java
│           └── ProductoImpl.java
└── ComprasApplication.java    # Punto de entrada de la aplicación
```

### Descripción de las Capas

1. **Capa de Dominio (`domain`)**: Entidades principales del negocio
2. **Capa de Aplicación (`application`)**: Interfaces que definen la lógica de negocio
3. **Capa de Infraestructura (`infrastructure`)**: Implementaciones concretas, controladores y repositorios

## ⚙️ Requisitos

- JDK 17 o superior
- Maven 3.8+
- MySQL 8.0+
- IDE compatible con Spring Boot (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/compras.git
   cd compras
   ```

2. Compila el proyecto con Maven:
   ```bash
   ./mvnw clean install
   ```

3. Ejecuta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

## ⚙️ Configuración

### Base de Datos

La aplicación está configurada para conectarse a una base de datos MySQL. La configuración se encuentra en el archivo `application.properties`:

```properties
spring.application.name=compras
spring.datasource.url=jdbc:mysql://localhost:3306/dbcompras?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=1607
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

La configuración actual:
- Crea la base de datos automáticamente si no existe
- Actualiza el esquema automáticamente según las entidades JPA
- Muestra las consultas SQL en la consola para depuración

## 📌 Uso

### Frontend

El backend está configurado para aceptar solicitudes desde `http://localhost:5173` mediante CORS.

### API REST

La API expone los siguientes endpoints principales:

- `/api/categoria`: Gestión de categorías de productos
- `/api/cliente`: Gestión de clientes
- `/api/producto`: Gestión de productos
- `/api/compra`: Gestión de compras
- `/api/compraProducto`: Gestión de productos asociados a compras

## 📖 Documentación de API

### Implementación de Swagger/OpenAPI

Una vez configurado, podrás acceder a la interfaz de Swagger UI en:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Documentación OpenAPI JSON: `http://localhost:8080/api-docs`

## 📊 Diagrama de Entidades

```
┌────────────┐     ┌─────────────┐     ┌────────────┐
│  Cliente   │     │   Compra    │     │  Producto  │
├────────────┤     ├─────────────┤     ├────────────┤
│ id (PK)    │──┐  │ idCompra(PK)│  ┌──│ idProducto │
│ nombre     │  └─>│ idCliente   │  │  │ nombre     │
│ apellido   │     │ fecha       │  │  │ codigoBarras│
│ celular    │     │ medioPago   │  │  │ precioVenta│
│ direccion  │     │ comentario  │  │  │ cantidadStock│
│ correoElec.│     │ estado      │  │  │ estado     │
└────────────┘     └─────────────┘  │  │ idCategoria│
                          │         │  └────────────┘
                          │         │        ▲
                          ▼         │        │
                   ┌──────────────┐ │        │
                   │CompraProducto│ │    ┌────────────┐
                   ├──────────────┤ │    │ Categoria  │
                   │ idCompra     │<┘    ├────────────┤
                   │ idProducto   │─────>│ idCategoria│
                   │ cantidad     │      │ descripcion│
                   │ total        │      │ estado     │
                   │ estado       │      └────────────┘
                   └──────────────┘
```

## 📚 Guía de Usuario

### Operaciones Principales

#### Gestión de Categorías

```bash
# Listar todas las categorías
curl -X GET http://localhost:8080/api/categoria

# Obtener una categoría específica
curl -X GET http://localhost:8080/api/categoria/1

# Crear una nueva categoría
curl -X POST http://localhost:8080/api/categoria \
  -H "Content-Type: application/json" \
  -d '{
    "descripcion": "Electrónicos",
    "estado": 1
  }'

# Actualizar una categoría
curl -X PUT http://localhost:8080/api/categoria/1 \
  -H "Content-Type: application/json" \
  -d '{
    "descripcion": "Electrónicos y Computación",
    "estado": 1
  }'

# Eliminar una categoría
curl -X DELETE http://localhost:8080/api/categoria/1
```

#### Gestión de Productos

```bash
# Crear un nuevo producto
curl -X POST http://localhost:8080/api/producto \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop HP",
    "codigoBarras": "789012345678",
    "precioVenta": 1299.99,
    "cantidadStock": 10,
    "estado": 1,
    "categoria": {
      "idCategoria": 1
    }
  }'
```

#### Proceso de Compra Completo

1. Verificar/crear cliente:
```bash
curl -X POST http://localhost:8080/api/cliente \
  -H "Content-Type: application/json" \
  -d '{
    "id": "123456789",
    "nombre": "Juan",
    "apellido": "Pérez",
    "celular": 3001234567,
    "direccion": "Calle 123, Ciudad",
    "correoElectronico": "juan.perez@example.com"
  }'
```

2. Crear compra:
```bash
curl -X POST http://localhost:8080/api/compra \
  -H "Content-Type: application/json" \
  -d '{
    "fecha": "2025-05-01T14:30:00Z",
    "medioPago": "Tarjeta de Crédito",
    "comentario": "Entrega rápida por favor",
    "estado": "P",
    "cliente": {
      "id": "123456789"
    }
  }'
```

3. Añadir productos a la compra:
```bash
curl -X POST http://localhost:8080/api/compraProducto \
  -H "Content-Type: application/json" \
  -d '{
    "id": {
      "idCompra": 1,
      "idProducto": 1
    },
    "cantidad": 2,
    "total": 2599.98,
    "estado": 1
  }'
```

### Códigos de Estado HTTP

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **400 Bad Request**: Solicitud incorrecta (verificar formato JSON)
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error en el servidor

### Convenciones

- Utiliza el estilo de código estándar de Java
- Escribe pruebas unitarias para nuevas funcionalidades
- Sigue los principios SOLID y de Clean Architecture

---

## 👨‍💻 Autor

Desarrollado como proyecto de demostración de Spring Boot por [JhonJMD](https://github.com/JhonJMD).

