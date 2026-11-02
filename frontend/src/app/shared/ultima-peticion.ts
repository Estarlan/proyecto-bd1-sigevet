import { Observable, Observer, Subscription } from 'rxjs';

/**
 * Recuerda la última petición HTTP que lanzó una pantalla. Cuando se lanza una nueva, la anterior se cancela:
 * así una respuesta lenta de una consulta vieja no puede pisar el resultado de la más reciente.
 *
 * Caso que resuelve: el usuario cambia el filtro de sede de A a B y enseguida a C. Si la respuesta de A llega al
 * final, sin esta clase la tabla mostraría las filas de A mientras el filtro dice C.
 *
 * Uso: private ultima = new UltimaPeticion();  y luego  this.ultima.lanzar(this.servicio.getAll(), { next: ... });
 */
export class UltimaPeticion {

  /** Suscripción de la petición que está en curso, o undefined si todavía no se lanzó ninguna. */
  private actual?: Subscription;

  /**
   * Cancela la petición anterior (si seguía en curso) y lanza esta.
   * @param peticion   Observable de HttpClient, por ejemplo this.clienteService.getAll(...)
   * @param observador funciones next y error, igual que en subscribe({ next, error })
   */
  lanzar<T>(peticion: Observable<T>, observador: Partial<Observer<T>>): void {
    this.actual?.unsubscribe();
    this.actual = peticion.subscribe(observador);
  }
}
