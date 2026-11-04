import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { sesionGuard } from './guards/sesion.guard';

import { Logincomponent } from './components/logincomponent/logincomponent';
import { Iniciocomponent } from './components/iniciocomponent/iniciocomponent';
import { Clientescomponent } from './components/clientescomponent/clientescomponent';
import { Clienteformcomponent } from './components/clienteformcomponent/clienteformcomponent';
import { Clientedetallecomponent } from './components/clientedetallecomponent/clientedetallecomponent';
import { Mascotascomponent } from './components/mascotascomponent/mascotascomponent';
import { Mascotaformcomponent } from './components/mascotaformcomponent/mascotaformcomponent';
import { Mascotadetallecomponent } from './components/mascotadetallecomponent/mascotadetallecomponent';
import { Agendacomponent } from './components/agendacomponent/agendacomponent';
import { Citaformcomponent } from './components/citaformcomponent/citaformcomponent';
import { Inventariocomponent } from './components/inventariocomponent/inventariocomponent';
import { Facturascomponent } from './components/facturascomponent/facturascomponent';
import { Facturaformcomponent } from './components/facturaformcomponent/facturaformcomponent';
import { Facturadetallecomponent } from './components/facturadetallecomponent/facturadetallecomponent';
import { Personalcomponent } from './components/personalcomponent/personalcomponent';
import { Empleadoformcomponent } from './components/empleadoformcomponent/empleadoformcomponent';
import { Algebracomponent } from './components/algebracomponent/algebracomponent';
import { Ejerciciocomponent } from './components/ejerciciocomponent/ejerciciocomponent';
import { Noencontradocomponent } from './components/noencontradocomponent/noencontradocomponent';

/**
 * Rutas de la aplicación. El inicio de sesión es la única página libre; todas las demás exigen sesión (sesionGuard).
 * Las rutas fijas ('nuevo', 'nueva') van antes de las que tienen parámetro (':id'), y la ruta '**' va al final.
 */
const routes: Routes = [
  { path: 'login', component: Logincomponent },

  { path: '', component: Iniciocomponent,         canActivate: [sesionGuard] },

  { path: 'clientes',            component: Clientescomponent,       canActivate: [sesionGuard] },
  { path: 'clientes/nuevo',      component: Clienteformcomponent,    canActivate: [sesionGuard] },
  { path: 'clientes/:id/editar', component: Clienteformcomponent,    canActivate: [sesionGuard] },
  { path: 'clientes/:id',        component: Clientedetallecomponent, canActivate: [sesionGuard] },

  { path: 'mascotas',            component: Mascotascomponent,       canActivate: [sesionGuard] },
  { path: 'mascotas/nueva',      component: Mascotaformcomponent,    canActivate: [sesionGuard] },
  { path: 'mascotas/:id/editar', component: Mascotaformcomponent,    canActivate: [sesionGuard] },
  { path: 'mascotas/:id',        component: Mascotadetallecomponent, canActivate: [sesionGuard] },

  { path: 'agenda/nueva',        component: Citaformcomponent,       canActivate: [sesionGuard] },
  { path: 'agenda',              component: Agendacomponent,         canActivate: [sesionGuard] },

  { path: 'inventario',          component: Inventariocomponent,     canActivate: [sesionGuard] },

  { path: 'facturas',            component: Facturascomponent,       canActivate: [sesionGuard] },
  { path: 'facturas/nueva',      component: Facturaformcomponent,    canActivate: [sesionGuard] },
  { path: 'facturas/:id',        component: Facturadetallecomponent, canActivate: [sesionGuard] },

  { path: 'personal/nuevo',      component: Empleadoformcomponent,   canActivate: [sesionGuard] },
  { path: 'personal/:id/editar', component: Empleadoformcomponent,   canActivate: [sesionGuard] },
  { path: 'personal',            component: Personalcomponent,       canActivate: [sesionGuard] },

  { path: 'algebra',             component: Algebracomponent,        canActivate: [sesionGuard] },
  { path: 'algebra/:numero',     component: Ejerciciocomponent,      canActivate: [sesionGuard] },

  // Cualquier otra dirección
  { path: '**',                  component: Noencontradocomponent,   canActivate: [sesionGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
