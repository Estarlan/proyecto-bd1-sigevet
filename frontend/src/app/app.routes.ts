import { Routes } from '@angular/router';

// Cada página se carga solo cuando se visita (loadComponent), igual que en el proyecto base.
export const routes: Routes = [
  { path: '', loadComponent: () => import('./pages/inicio/inicio').then(m => m.Inicio) },

  { path: 'clientes',     loadComponent: () => import('./pages/clientes/clientes').then(m => m.Clientes) },
  { path: 'clientes/:id', loadComponent: () => import('./pages/cliente-detalle/cliente-detalle').then(m => m.ClienteDetallePage) },

  { path: 'mascotas/:id',   loadComponent: () => import('./pages/mascota-detalle/mascota-detalle').then(m => m.MascotaDetallePage) },
  { path: 'agenda',         loadComponent: () => import('./pages/agenda/agenda').then(m => m.Agenda) },
  { path: 'inventario',     loadComponent: () => import('./pages/inventario/inventario').then(m => m.Inventario) },
  { path: 'facturas',       loadComponent: () => import('./pages/facturas/facturas').then(m => m.Facturas) },
  { path: 'facturas/:id',   loadComponent: () => import('./pages/factura-detalle/factura-detalle').then(m => m.FacturaDetallePage) },
  { path: 'personal',       loadComponent: () => import('./pages/personal/personal').then(m => m.Personal) },
  { path: 'algebra',        loadComponent: () => import('./pages/algebra/algebra').then(m => m.Algebra) },
  { path: 'algebra/:numero', loadComponent: () => import('./pages/ejercicio/ejercicio').then(m => m.Ejercicio) },

  // Cualquier otra dirección
  { path: '**', loadComponent: () => import('./pages/no-encontrado/no-encontrado').then(m => m.NoEncontrado) }
];
