import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { sesionGuard } from './guards/sesion.guard';

import { Login } from './pages/login/login';
import { Inicio } from './pages/inicio/inicio';
import { Clientes } from './pages/clientes/clientes';
import { ClienteFormPage } from './pages/cliente-form/cliente-form';
import { ClienteDetallePage } from './pages/cliente-detalle/cliente-detalle';
import { Mascotas } from './pages/mascotas/mascotas';
import { MascotaFormPage } from './pages/mascota-form/mascota-form';
import { MascotaDetallePage } from './pages/mascota-detalle/mascota-detalle';
import { Agenda } from './pages/agenda/agenda';
import { CitaFormPage } from './pages/cita-form/cita-form';
import { Inventario } from './pages/inventario/inventario';
import { Facturas } from './pages/facturas/facturas';
import { FacturaFormPage } from './pages/factura-form/factura-form';
import { FacturaDetallePage } from './pages/factura-detalle/factura-detalle';
import { Personal } from './pages/personal/personal';
import { EmpleadoFormPage } from './pages/empleado-form/empleado-form';
import { NoEncontrado } from './pages/no-encontrado/no-encontrado';

/**
 * Rutas de la aplicación. El inicio de sesión es la única página libre; todas las demás exigen sesión (sesionGuard).
 * Las rutas fijas ('nuevo', 'nueva') van antes de las que tienen parámetro (':id'), y la ruta '**' va al final.
 */
const routes: Routes = [
  { path: 'login', component: Login },

  { path: '', component: Inicio, canActivate: [sesionGuard] },

  { path: 'clientes',            component: Clientes,           canActivate: [sesionGuard] },
  { path: 'clientes/nuevo',      component: ClienteFormPage,    canActivate: [sesionGuard] },
  { path: 'clientes/:id/editar', component: ClienteFormPage,    canActivate: [sesionGuard] },
  { path: 'clientes/:id',        component: ClienteDetallePage, canActivate: [sesionGuard] },

  { path: 'mascotas',            component: Mascotas,           canActivate: [sesionGuard] },
  { path: 'mascotas/nueva',      component: MascotaFormPage,    canActivate: [sesionGuard] },
  { path: 'mascotas/:id/editar', component: MascotaFormPage,    canActivate: [sesionGuard] },
  { path: 'mascotas/:id',        component: MascotaDetallePage, canActivate: [sesionGuard] },

  { path: 'agenda/nueva',        component: CitaFormPage,       canActivate: [sesionGuard] },
  { path: 'agenda',              component: Agenda,             canActivate: [sesionGuard] },

  { path: 'inventario',          component: Inventario,         canActivate: [sesionGuard] },

  { path: 'facturas',            component: Facturas,           canActivate: [sesionGuard] },
  { path: 'facturas/nueva',      component: FacturaFormPage,    canActivate: [sesionGuard] },
  { path: 'facturas/:id',        component: FacturaDetallePage, canActivate: [sesionGuard] },

  { path: 'personal/nuevo',      component: EmpleadoFormPage,   canActivate: [sesionGuard] },
  { path: 'personal/:id/editar', component: EmpleadoFormPage,   canActivate: [sesionGuard] },
  { path: 'personal',            component: Personal,           canActivate: [sesionGuard] },


  // Cualquier otra dirección
  { path: '**',                  component: NoEncontrado,       canActivate: [sesionGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
