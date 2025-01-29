# 🏗️ Core Muebles

## 📌 Descripción del Proyecto
**Backend Core Muebles** es una aplicación backend desarrollada en **Java con Spring Boot**, diseñada para gestionar la producción y asignación de empleados en una fábrica de muebles. La aplicación implementa **principios SOLID** y diversos **patrones de diseño**, garantizando un código modular, escalable y de fácil mantenimiento.  

## 🚀 Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA** (Gestión de persistencia)
- **Spring Security** (Autenticación y autorización)
- **H2 / MySQL** (Base de datos)
- **Maven** (Gestor de dependencias)
- **Lombok** (Reducir boilerplate)

---

## 📖 Principios SOLID Implementados
El código sigue los **principios SOLID**, mejorando la mantenibilidad y extensibilidad del software.

### 1️⃣ **Single Responsibility Principle (SRP)**
📍 _Cada clase tiene una única responsabilidad y está bien separada por capas._  
📌 **Ejemplo en el código:**
- **Estructura de capas:**  
  - **Controladores** (`Controlador/`) manejan las peticiones HTTP.  
  - **Servicios** (`Servicio/`) encapsulan la lógica de negocio.  
  - **Repositorios** (`Repositorio/`) gestionan la persistencia en base de datos.  
- **Validaciones y cálculos están delegados a sus respectivos servicios**, evitando sobrecargar los controladores.

📌 **Ejemplo específico:**  
- `ClienteService.java` gestiona la validación y lógica del cliente en lugar del controlador `ClienteController.java`.

---

### 2️⃣ **Liskov Substitution Principle (LSP)**
📍 _Las clases derivadas pueden ser sustituidas por sus clases base sin afectar la funcionalidad._  
📌 **Ejemplo en el código:**
- **Uso de interfaces en los repositorios y servicios:**  
  - Todos los repositorios (`JpaRepository`) pueden ser sustituidos sin romper el código.  
  - `Command` en `Command/` permite ejecutar diferentes comandos sin alterar la implementación de `AsignacionEmpleadoService`.

📌 **Ejemplo específico:**  
- La interfaz `Command.java` permite que `AsignarEmpleadosCommand`, `LiberarEmpleadosCommand` y `ReasignarEmpleadosCommand` sean intercambiables sin afectar la lógica de negocio.

---

### 3️⃣ **Dependency Inversion Principle (DIP)**
📍 _Los módulos de alto nivel no dependen de los de bajo nivel, sino de abstracciones._  
📌 **Ejemplo en el código:**
- **Uso de interfaces para la inyección de dependencias** en servicios y repositorios.
- **Los servicios dependen de las interfaces y no de implementaciones concretas**, lo que facilita los cambios en la lógica sin afectar otras partes del código.

📌 **Ejemplo específico:**  
- `AsignacionEmpleadoService.java` usa `Command.java` para ejecutar lógica sin depender de una implementación específica.

---

## 🏗️ Patrones de Diseño Aplicados

### 🏛 **1. Patrón Comando (Command Pattern)**
📍 _Encapsula una acción en un objeto y permite ejecutar comandos de forma flexible._  
📌 **Ejemplo en el código:**
- Implementado en `Command/`
- Se usa en `AsignacionEmpleadoService` para ejecutar acciones como:
  - `AsignarEmpleadosCommand`
  - `LiberarEmpleadosCommand`
  - `ReasignarEmpleadosCommand`

📌 **Beneficio:**  
- Permite agregar nuevas acciones sin modificar el servicio principal.

---

### 🏛 **2. Patrón Repositorio (Repository Pattern)**
📍 _Abstrae la capa de persistencia para separar la lógica de acceso a datos._  
📌 **Ejemplo en el código:**
- Implementado en `Repositorio/`
- Cada entidad (`Cliente`, `Empleado`, `Pedido`) tiene su propio repositorio.

📌 **Beneficio:**  
- Separa la lógica de acceso a datos de la lógica de negocio, facilitando la prueba y mantenimiento.

---

### 🏛 **3. Patrón Constructor (Builder Pattern)**
📍 _Permite crear objetos complejos paso a paso de forma flexible._  
📌 **Ejemplo en el código:**
- Implementado en `DetallePedido.java` con un `Builder` para construir objetos `DetallePedido` evitando constructores largos.

📌 **Beneficio:**  
- Mejora la legibilidad y evita la sobrecarga de constructores.

---

## 📂 Estructura del Proyecto


    BackendCoreMuebles/
    │── src/main/java/com/example/BackendCoreMuebles/
    │   ├── Command/              # Implementación del patrón Comando
    │   ├── Configuraciones/      # Configuración de seguridad y CORS
    │   ├── Controlador/          # Controladores REST
    │   ├── Modelos/              # Clases de modelo (Entidades JPA)
    │   ├── Repositorio/          # Interfaces de persistencia (Patrón Repositorio)
    │   ├── Servicio/             # Lógica de negocio y aplicación de principios SOLID
    │   ├── BackendCoreMueblesApplication.java  # Clase principal de Spring Boot
    │── resources/
    │   ├── application.properties  # Configuración de la base de datos
    │── pom.xml                    # Dependencias Maven
    │── README.md                   # Documentación del proyecto


---

## 🛠️ Instalación y Configuración

### 🔹 1. Clonar el repositorio
    
    git clone https://github.com/AleFe2425/CoreBuenasPracticas.git
    cd CoreBuenasPracticas

### 🔹 2. Configurar la base de datos
- Modificar el archivo application.properties según la configuración deseada (H2, MySQL, etc.).

### 🔹 3. Ejecutar la aplicación

    mvn spring-boot:run

-La API estará disponible en http://localhost:8080.








