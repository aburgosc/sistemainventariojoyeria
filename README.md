# Sistema de Inventario y Ventas de Joyería

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-3.9.2-red?logo=apachemaven)
![HikariCP](https://img.shields.io/badge/HikariCP-pool-lightgrey)

**Sistema de escritorio desarrollado en Java 21 y PostgreSQL 16 para la gestión integral de inventario y ventas de una joyería.**

---

## Descripción

Aplicación de escritorio para administrar:

- **Productos y lotes**: control de stock, precios y costos.
- **Clientes**: registro, validación y búsqueda.
- **Ventas**: asignación de lotes según stock disponible y control FIFO.
- **Movimientos de stock**: historial de ingresos y salidas de productos.

Diseñado con **arquitectura MVC**, principios de **Clean Code** y **SOLID**, con separación de capas: modelos (VO), DAO, servicios, controladores y vistas.

---

## Tecnologías

- **Lenguaje:** Java 21  
- **Base de datos:** PostgreSQL 16  
- **UI:** Java Swing con componentes reutilizables  
- **Conexión a DB:** JDBC con **HikariCP (Connection Pooling)**  
- **Gestión de dependencias:** Maven  
- **IDE recomendado:** NetBeans  

---

## Características Principales

1. **Gestión de Inventario**
   - Registro y actualización de productos y lotes.
   - Control de stock disponible y movimientos históricos.
   - Cálculo automático de precio promedio por producto.

2. **Gestión de Clientes**
   - Registro de clientes con validaciones de nombre, apellido, email y teléfono.
   - Búsqueda de clientes por texto libre.

3. **Gestión de Ventas**
   - Creación de ventas con asignación automática de lotes según stock disponible.
   - Registro de movimientos de salida de productos.
   - Validaciones de stock y control FIFO para ventas.

4. **Excepciones y Seguridad**
   - Manejo de excepciones específicas por capa: `DAOException` y `ServiceException`.
   - Validaciones de datos en capas de servicio.

---

## Instalación y Ejecución

- Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/SistemaInventarioJoyeria.git
cd SistemaInventarioJoyeria

```
- Compilar y generar el JAR usando Maven:

```
mvn clean package
```

- Ejecutar el JAR generado

```bash
java -jar target/SistemaInventarioJoyeria-1.0-SNAPSHOT.jar
```
- ⚠️ Asegúrate de tener PostgreSQL instalado y ejecutándose en localhost:5432.
El sistema crea automáticamente la base de datos sistemainventario si no existe.

- Estructura del Proyecto

```
SistemaInventarioJoyeria/
├─ controller/       # Controladores de la aplicación
├─ dao/              # Interfaces DAO y sus implementaciones
├─ exception/        # Clases de excepciones (DAOException, ServiceException)
├─ model/            # Clases de entidad (Cliente, Producto, Lote, Stock, MovimientoStock)
├─ service/          # Interfaces de servicios y sus implementaciones
├─ ui/               # Vistas Java Swing
├─ util/             # Utilidades (DBConnection, DBInitializer, ResultSetMapper)
├─ SistemaInventarioJoyeria.java # Clase principal con método main
```

- DAO: Acceso a base de datos con HikariCP.
- Service: Lógica de negocio y validaciones.
- UI: Interfaces Swing para interacción con el usuario.
- Model: Clases VO que representan las entidades del sistema.
