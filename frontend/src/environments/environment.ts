// Ruta relativa: en desarrollo, ng serve la reenvía a Spring Boot con proxy.conf.json;
// en producción, Angular se sirve desde el mismo Spring Boot (puerto 5000), así que no hace falta CORS.
export const environment = {
  production: false,
  apiUrl: '/api'
};
