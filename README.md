# Guía para levantar el proyecto PurchaseOrder con Docker

## 1. Crear la red de Docker
Esto permite que los contenedores de la base de datos y la API se comuniquen entre sí.

```bash
docker network create red-lechonKevin
```

## 2. Crear el contenedor de la base de datos MySQL
Esto crea y ejecuta un contenedor MySQL con los datos persistentes y las credenciales necesarias.

```bash
docker run -d -p 3316:3306 \
  --name mysql-purchaseorder \
  --network red-lechonKevin \
  -e MYSQL_DATABASE=purchaseorderdb \
  -e MYSQL_ROOT_PASSWORD=abcd \
  -e MYSQL_USER=AppRoot \
  -e MYSQL_PASSWORD=abcd \
  -v mysql_purchaseorder_data:/var/lib/mysql \
  mysql:8.0
```

## 3. Generar el JAR de la aplicación
Compila el proyecto y genera el archivo JAR necesario para la imagen Docker.

```bash
mvn clean package
```

## 4. Construir la imagen Docker de la API
Esto crea la imagen Docker usando el Dockerfile del proyecto.

```bash
docker build -t kslechon/purchaseorder:1.0 .
```

## 5. Ejecutar la API en un contenedor
Esto levanta la API y la conecta a la red y base de datos creadas anteriormente.

```bash
docker run -d -p 8080:8080 \
  --name app-purchaseorder \
  --network red-lechonKevin \
  -e DB_HOST=mysql-purchaseorder \
  -e DB_PORT=3306 \
  -e DB_NAME=purchaseorderdb \
  -e DB_USER=AppRoot \
  -e DB_PASSWORD=abcd \
  kslechon/purchaseorder:1.0
```

## 6. Subir la imagen a Docker Hub (opcional)
Esto permite compartir la imagen públicamente o usarla en otros entornos.

```bash
docker push kslechon/purchaseorder:1.0
```

---

## Prueba de la API

Una vez que ambos contenedores estén corriendo, puedes probar la API con Postman o curl:

- Endpoint de ejemplo:
  - `GET http://localhost:8080/api/v1/purchase-orders`

Asegúrate de que el puerto 8080 esté libre en tu máquina local.

---

## Notas
- Si necesitas detener y eliminar los contenedores:
  ```bash
  docker stop app-purchaseorder mysql-purchaseorder
  docker rm app-purchaseorder mysql-purchaseorder
  ```
- Para ver los logs de la API:
  ```bash
  docker logs app-purchaseorder
  ```
- Para ver los contenedores activos:
  ```bash
  docker ps
  ```

---

¡Listo! Así puedes levantar y probar tu proyecto completo usando Docker.