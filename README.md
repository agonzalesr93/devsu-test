# Configuración y Uso del Proyecto

Este proyecto utiliza Docker Compose para configurar y ejecutar el entorno de desarrollo. A continuación se presentan las instrucciones para iniciar los servicios, reiniciar el entorno y utilizar las APIs.

## Iniciar los Servicios

Para iniciar los servicios, ejecuta el siguiente comando en el directorio raíz del proyecto:

```bash
docker compose up
```

Este comando construirá e iniciará los servicios definidos en el archivo docker-compose.yml. Los servicios incluidos son:

api-client-v1: Expone servicios en el puerto 8070 con la ruta base /api/v1/.

api-account-v1: Expone servicios en el puerto 8060 con la ruta base /api/v1/.

## Reiniciar el Entorno
Si necesitas comenzar todo desde cero, incluyendo la recreación de la base de datos, sigue estos pasos:

Detener y eliminar todos los contenedores y volúmenes:

```bash
docker compose down -v
```

Este comando eliminará los contenedores y volúmenes, incluyendo la base de datos. La opción -v elimina los volúmenes asociados a los contenedores.

## Iniciar los servicios nuevamente:

```bash
docker compose up
```

Esto recreará los contenedores e inicializará la base de datos utilizando el script ubicado en init-db/init-db.sql.

## Inicialización de la Base de Datos
La base de datos se crea e inicializa automáticamente con Docker Compose utilizando el script init-db/init-db.sql. Este script configura las tablas y secuencias necesarias para el funcionamiento de la aplicación.

## Detalles de las APIs

### api-client-v1:

- Puerto: 8070
- Ruta base: /api/v1/


### api-account-v1:

- Puerto: 8060
- Ruta base: /api/v1/


## Colección de Postman
Se proporciona una colección de Postman para probar las APIs. La colección está organizada en las siguientes carpetas:

- Clients: Contiene operaciones CRUD para clientes.
- Accounts: Contiene operaciones CRUD para cuentas y endpoints para obtener los movimientos por cuentas.
- Transactions: Contiene operaciones CRUD para transacciones.
- Report: Contiene un endpoint para obtener el reporte del estado de cuenta de un cliente por fechas y paginado.

## Instrucciones para Postman

### Importar la Colección:

Importa la colección de Postman proporcionada en tu aplicación Postman.


### Ejecutar Casos de Uso:

Ejecuta la carpeta Casos de Uso en Postman para insertar los datos tal como se muestra en los pasos del caso de uso del ejercicio.

Esto te permitirá probar todas las funcionalidades de la API con datos predefinidos.

