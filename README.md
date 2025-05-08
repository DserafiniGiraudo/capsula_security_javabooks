# 🛡️ Torneo Cápsula de Seguridad — **Librería Java Books**  

La prestigiosa **Librería Java Books** desea implementar un sistema de gestión de libros para poder realizar altas, bajas y consultas de libros, con una seguridad adecuada para proteger los datos. Este sistema debe ser capaz de manejar tanto la gestión interna de los libros como las restricciones de acceso según los roles de los usuarios. 

## 📚 Requisitos Funcionales

Desarrollar los siguientes endpoints para la gestión y consulta de libros (con datos almacenados **en memoria**): 

- `POST /book` — **Alta de libro**  
  Requiere los siguientes parámetros:  
  - `titulo` (String)  
  - `autor` (String)  
  - `cantidadPaginas` (Integer)  
  - `categoria` (String)  
  - `contenido` (String)  

- `DELETE /book/{id}` — **Baja de libro**  
  El id del libro que se desea eliminar. 

- `GET /book/title/{title}` — **Consulta de libro por título**  
  Retorna el libro que coincida con el título proporcionado.

- `GET /book/author/{author}` — **Consulta de libros por autor**  
  Retorna todos los libros escritos por el autor proporcionado.

> ⚠️ **Nota**: Todo el modelo de datos se almacenan **en memoria**. 

---

## 🔐 Requisitos de Seguridad

### ✅ Authorization Server (OAuth2)

Implementar un **Authorization Server** con **Authorization Code Flow** como flujo principal para manejar la autenticación de usuarios. 

### 🔑 Custom JWT Tokens: 

Los tokens JWT generados deben incluir los claims habituales, como `sub (subject)`, `exp (expiration)`, `iat (issued at)`, etc. 
Además de los claims estándar, se debe agregar un **claim personalizado** `categorias` que contenga una lista de las categorías a las que el usuario tiene acceso. Este claim es crucial para validar si el usuario tiene permisos sobre las categorías de libros. 
Nota: Analizar si es conveniente incluir los autores directamente en el token o que  la autorización por autor se realice mediante validación en los endpoints, basado en los datos asociados al usuario. 
Roles y autorización: 

### 👥 Roles y Autorización

- `ADMIN`: Puede crear y eliminar libros (`POST`, `DELETE`).
- `CLIENT`: Solo puede consultar libros (`GET`).

La autorización también debe verificar que el usuario tenga acceso a la categoría del libro o a los autores de los libros en función de su perfil. 

### 🧭 Scopes y Validación de Recursos

Los tokens deben incluir scopes como:
- `libros.read` → Para lectura de libros.
- `libros.write` → Para escritura de libros.

El **Resource Server** (la API de libros) debe validar:
- Presencia de **roles** y **scopes** apropiados.
- Acceso autorizado a **categorías** y **autores** según los claims del token.
