# SIGEVET · Frontend en Angular

Interfaz web de SIGEVET en **Angular 21** con **Bootstrap 5** y **Bootstrap Icons**. Consulta y modifica datos a través de la
API REST de Spring Boot (`/api/*`) y nunca se conecta a MySQL. El SQL sigue escrito a mano en los DAO del backend.

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
  guards/                   sesionGuard (exige sesión) e interceptor que lleva a /login con un 401
  pages/login               inicio de sesión del personal
  app-module.ts             módulo raíz (NgModule): declara todos los componentes, HttpClient e interceptor
  app-routing-module.ts     rutas de la aplicación (RouterModule.forRoot) con sesionGuard
```

## Compilar para producción

```bash
npx ng build                    # genera dist/sigevet-front/browser/
cd ../app && ./mvnw package     # Maven copia ese build a static/ dentro del jar
```

Spring Boot sirve el frontend y la API en el mismo puerto (5000). `ConfiguracionWeb` devuelve `index.html` en las
rutas de Angular, así que recargar `/clientes/5` o abrir un enlace directo funciona.
