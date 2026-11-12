import { LOCALE_ID, NgModule, provideZoneChangeDetection } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import localeEsCo from '@angular/common/locales/es-CO';

import { AppRoutingModule } from './app-routing-module';
import { AppComponent } from './app';
import { sesionInterceptor } from './guards/sesion.interceptor';

import { NavbarComponent } from './shared/components/navbar/navbar';
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
    Login,
    Inicio,
    Clientes,
    ClienteFormPage,
    ClienteDetallePage,
    Mascotas,
    MascotaFormPage,
    MascotaDetallePage,
    Agenda,
    CitaFormPage,
    Inventario,
    Facturas,
    FacturaFormPage,
    FacturaDetallePage,
    Personal,
    EmpleadoFormPage,
    NoEncontrado
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
