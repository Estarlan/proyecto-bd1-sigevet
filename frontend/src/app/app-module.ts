import { LOCALE_ID, NgModule, provideZoneChangeDetection } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import localeEsCo from '@angular/common/locales/es-CO';

import { AppRoutingModule } from './app-routing-module';
import { AppComponent } from './app';
import { sesionInterceptor } from './guards/sesion.interceptor';

import { Navbarcomponent } from './components/navbarcomponent/navbarcomponent';
import { Footercomponent } from './components/footercomponent/footercomponent';
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

// Formato colombiano para los pipes de números y moneda: $ 364.000
registerLocaleData(localeEsCo);

/**
 * Módulo raíz de SIGEVET. Declara todos los componentes de la aplicación (ninguno es standalone),
 * importa los módulos que usan sus plantillas y registra el cliente HTTP con el interceptor de sesión.
 */
@NgModule({
  declarations: [
    AppComponent,
    Navbarcomponent,
    Footercomponent,
    Logincomponent,
    Iniciocomponent,
    Clientescomponent,
    Clienteformcomponent,
    Clientedetallecomponent,
    Mascotascomponent,
    Mascotaformcomponent,
    Mascotadetallecomponent,
    Agendacomponent,
    Citaformcomponent,
    Inventariocomponent,
    Facturascomponent,
    Facturaformcomponent,
    Facturadetallecomponent,
    Personalcomponent,
    Empleadoformcomponent,
    Algebracomponent,
    Ejerciciocomponent,
    Noencontradocomponent
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
