# SIGEVET · Frontend en Angular

Interfaz web de SIGEVET en **Angular 21** con **Bootstrap 5** y **Bootstrap Icons**. Solo muestra datos: los pide a
la API REST de Spring Boot (`/api/*`) y nunca se conecta a MySQL. El SQL sigue escrito a mano en los DAO del backend.

## Desarrollo

Se necesitan dos terminales:

```bash
# 1) backend (carpeta app/), con la base del servidor en SIGEVET_DB_URL y SIGEVET_DB_PASSWORD
./mvnw spring-boot:run          # API en http://localhost:5000/api

# 2) frontend (esta carpeta)
npm install
npm start                       # http://localhost:4200
```

`ng serve` reenvía todo lo que empieza por `/api` a `http://localhost:5000` (ver `proxy.conf.json`), así que el
navegador cree que todo viene del mismo servidor y no hace falta configurar CORS.

## Estructura

```text
src/app/
  models/models.ts          interfaces TypeScript con los mismos campos que el JSON de la API
  services/api.service.ts   un servicio por recurso (HttpClient → Observable)
  shared/components/navbar  menú superior
  pages/                    una carpeta por página
  app.routes.ts             rutas con carga diferida (loadComponent)
```

## Compilar para producción

```bash
npm run build                   # genera dist/sigevet-front/browser/
```

Ese contenido se copia a `app/src/main/resources/static/` y Spring Boot lo sirve en el puerto 5000.
