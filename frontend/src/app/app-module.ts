import { LOCALE_ID, NgModule, provideZoneChangeDetection } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import localeEsCo from '@angular/common/locales/es-CO';

import { AppRoutingModule } from './app-routing-module';
import { AppComponent } from './app';
import { sesionInterceptor } from './guards/sesion.interceptor';

import { NavbarComponent } from './components/navbar-component/navbar-component';
import { FooterComponent } from './components/footer-component/footer-component';
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

// Formato colombiano para los pipes de números y moneda: $ 364.000
registerLocaleData(localeEsCo);

/**
 * Módulo raíz de SIGEVET. Declara todos los componentes de la aplicación (ninguno es standalone),
 * importa los módulos que usan sus plantillas y registra el cliente HTTP con el interceptor de sesión.
 */
@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    LoginComponent,
    InicioComponent,
    ClientesComponent,
    ClienteFormComponent,
    ClienteDetalleComponent,
    MascotasComponent,
    MascotaFormComponent,
    MascotaDetalleComponent,
    AgendaComponent,
    CitaFormComponent,
    InventarioComponent,
    FacturasComponent,
    FacturaFormComponent,
    FacturaDetalleComponent,
    PersonalComponent,
    EmpleadoFormComponent,
    AlgebraComponent,
    EjercicioComponent,
    NoEncontradoComponent
  ],
  imports: [
    BrowserModule,      // también trae CommonModule: pipes currency y number
    FormsModule,        // [(ngModel)] de los formularios
    AppRoutingModule    // rutas y directivas routerLink / router-outlet
  ],
  providers: [
    // Detección de cambios con zone.js: las páginas actualizan sus propiedades dentro de subscribe() y se repintan solas.
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideHttpClient(withFetch(), withInterceptors([sesionInterceptor])),
    { provide: LOCALE_ID, useValue: 'es-CO' }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
