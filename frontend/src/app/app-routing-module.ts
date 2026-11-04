import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { sesionGuard } from './guards/sesion.guard';

import { LoginComponent } from './components/login-component/login-component';
import { InicioComponent } from './components/inicio-component/inicio-component';
import { ClientesComponent } from './components/clientes-component/clientes-component';
import { ClienteFormComponent } from './components/cliente-form-component/cliente-form-component';
import { ClienteDetalleComponent } from './components/cliente-detalle-component/cliente-detalle-component';
import { MascotasComponent } from './components/mascotas-component/mascotas-component';
import { MascotaFormComponent } from './components/mascota-form-component/mascota-form-component';
import { MascotaDetalleComponent } from './components/mascota-detalle-component/mascota-detalle-component';
import { AgendaComponent } from './components/agenda-component/agenda-component';
import { CitaFormComponent } from './components/cita-form-component/cita-form-component';
import { InventarioComponent } from './components/inventario-component/inventario-component';
import { FacturasComponent } from './components/facturas-component/facturas-component';
import { FacturaFormComponent } from './components/factura-form-component/factura-form-component';
import { FacturaDetalleComponent } from './components/factura-detalle-component/factura-detalle-component';
import { PersonalComponent } from './components/personal-component/personal-component';
import { EmpleadoFormComponent } from './components/empleado-form-component/empleado-form-component';
import { AlgebraComponent } from './components/algebra-component/algebra-component';
import { EjercicioComponent } from './components/ejercicio-component/ejercicio-component';
import { NoEncontradoComponent } from './components/no-encontrado-component/no-encontrado-component';

/**
 * Rutas de la aplicación. El inicio de sesión es la única página libre; todas las demás exigen sesión (sesionGuard).
 * Las rutas fijas ('nuevo', 'nueva') van antes de las que tienen parámetro (':id'), y la ruta '**' va al final.
 */
const routes: Routes = [
  { path: 'login', component: LoginComponent },

  { path: '', component: InicioComponent,         canActivate: [sesionGuard] },

  { path: 'clientes',            component: ClientesComponent,       canActivate: [sesionGuard] },
  { path: 'clientes/nuevo',      component: ClienteFormComponent,    canActivate: [sesionGuard] },
  { path: 'clientes/:id/editar', component: ClienteFormComponent,    canActivate: [sesionGuard] },
  { path: 'clientes/:id',        component: ClienteDetalleComponent, canActivate: [sesionGuard] },

  { path: 'mascotas',            component: MascotasComponent,       canActivate: [sesionGuard] },
  { path: 'mascotas/nueva',      component: MascotaFormComponent,    canActivate: [sesionGuard] },
  { path: 'mascotas/:id/editar', component: MascotaFormComponent,    canActivate: [sesionGuard] },
  { path: 'mascotas/:id',        component: MascotaDetalleComponent, canActivate: [sesionGuard] },

  { path: 'agenda/nueva',        component: CitaFormComponent,       canActivate: [sesionGuard] },
  { path: 'agenda',              component: AgendaComponent,         canActivate: [sesionGuard] },

  { path: 'inventario',          component: InventarioComponent,     canActivate: [sesionGuard] },

  { path: 'facturas',            component: FacturasComponent,       canActivate: [sesionGuard] },
  { path: 'facturas/nueva',      component: FacturaFormComponent,    canActivate: [sesionGuard] },
  { path: 'facturas/:id',        component: FacturaDetalleComponent, canActivate: [sesionGuard] },

  { path: 'personal/nuevo',      component: EmpleadoFormComponent,   canActivate: [sesionGuard] },
  { path: 'personal/:id/editar', component: EmpleadoFormComponent,   canActivate: [sesionGuard] },
  { path: 'personal',            component: PersonalComponent,       canActivate: [sesionGuard] },

  { path: 'algebra',             component: AlgebraComponent,        canActivate: [sesionGuard] },
  { path: 'algebra/:numero',     component: EjercicioComponent,      canActivate: [sesionGuard] },

  // Cualquier otra dirección
  { path: '**',                  component: NoEncontradoComponent,   canActivate: [sesionGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
