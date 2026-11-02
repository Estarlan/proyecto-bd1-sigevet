import { platformBrowser } from '@angular/platform-browser';
import { AppModule } from './app/app-module';

// Arranque con módulos (NgModule): el módulo raíz AppModule declara todos los componentes y arranca AppComponent.
platformBrowser().bootstrapModule(AppModule)
  .catch((err) => console.error(err));
