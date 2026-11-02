import { Routes } from '@angular/router';
import { sesionGuard } from './guards/sesion.guard';

// Cada página se carga solo cuando se visita (loadComponent), igual que en el proyecto base.
const paginas: Routes = [
  { path: '', loadComponent: () => import('./pages/inicio/inicio').then(m => m.Inicio) },

  { path: 'clientes',            loadComponent: () => import('./pages/clientes/clientes').then(m => m.Clientes) },
  // las rutas fijas ('nuevo') van antes de las que tienen parámetro (':id')
  { path: 'clientes/nuevo',      loadComponent: () => import('./pages/cliente-form/cliente-form').then(m => m.ClienteFormPage) },
  { path: 'clientes/:id/editar', loadComponent: () => import('./pages/cliente-form/cliente-form').then(m => m.ClienteFormPage) },
  { path: 'clientes/:id',        loadComponent: () => import('./pages/cliente-detalle/cliente-detalle').then(m => m.ClienteDetallePage) },

  { path: 'mascotas',            loadComponent: () => import('./pages/mascotas/mascotas').then(m => m.Mascotas) },
  { path: 'mascotas/nueva',      loadComponent: () => import('./pages/mascota-form/mascota-form').then(m => m.MascotaFormPage) },
  { path: 'mascotas/:id/editar', loadComponent: () => import('./pages/mascota-form/mascota-form').then(m => m.MascotaFormPage) },

  { path: 'mascotas/:id',   loadComponent: () => import('./pages/mascota-detalle/mascota-detalle').then(m => m.MascotaDetallePage) },
  { path: 'agenda/nueva',     loadComponent: () => import('./pages/cita-form/cita-form').then(m => m.CitaFormPage) },
  { path: 'agenda',         loadComponent: () => import('./pages/agenda/agenda').then(m => m.Agenda) },
  { path: 'inventario',     loadComponent: () => import('./pages/inventario/inventario').then(m => m.Inventario) },
  { path: 'facturas',       loadComponent: () => import('./pages/facturas/facturas').then(m => m.Facturas) },
  { path: 'facturas/nueva',   loadComponent: () => import('./pages/factura-form/factura-form').then(m => m.FacturaFormPage) },
  { path: 'facturas/:id',   loadComponent: () => import('./pages/factura-detalle/factura-detalle').then(m => m.FacturaDetallePage) },
  { path: 'personal/nuevo',      loadComponent: () => import('./pages/empleado-form/empleado-form').then(m => m.EmpleadoFormPage) },
  { path: 'personal/:id/editar', loadComponent: () => import('./pages/empleado-form/empleado-form').then(m => m.EmpleadoFormPage) },
  { path: 'personal',       loadComponent: () => import('./pages/personal/personal').then(m => m.Personal) },
  { path: 'algebra',        loadComponent: () => import('./pages/algebra/algebra').then(m => m.Algebra) },
  { path: 'algebra/:numero', loadComponent: () => import('./pages/ejercicio/ejercicio').then(m => m.Ejercicio) },

  // Cualquier otra dirección
  { path: '**', loadComponent: () => import('./pages/no-encontrado/no-encontrado').then(m => m.NoEncontrado) }
];

// El inicio de sesión es la única página libre; todas las demás exigen sesión (sesionGuard).
export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./pages/login/login').then(m => m.Login) },
  ...paginas.map(pagina => ({ ...pagina, canActivate: [sesionGuard] }))
];
