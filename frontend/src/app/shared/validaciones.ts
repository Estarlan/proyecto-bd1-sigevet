import { NgModel } from '@angular/forms';

// Formatos de los campos de los formularios. Son los MISMOS patrones de Validador.java en el backend: el navegador
// avisa antes de enviar, y el servidor vuelve a revisar (nunca se confía solo en el navegador).

/** Letras del español, con tildes y eñe. */
const L = 'A-Za-zÁÉÍÓÚÜÑáéíóúüñ';

export const PATRONES = {
  /** Nombres y apellidos: solo letras, palabras separadas por espacio, apóstrofo o guion. */
  nombrePersona: `^[${L}]+([ '\\-][${L}]+)*$`,
  /** CC, CE y PPT: de 6 a 10 dígitos. */
  documentoNumerico: '^[0-9]{6,10}$',
  /** Pasaporte: de 6 a 12 letras o dígitos. */
  pasaporte: '^[A-Za-z0-9]{6,12}$',
  /** Teléfono: de 7 a 10 dígitos. */
  telefono: '^[0-9]{7,10}$',
  /** Dirección: letras, dígitos y # . , ° - con al menos un número. */
  direccion: `^(?=.*[0-9])[${L}0-9#.,°\\- ]{5,120}$`,
  /** Correo: usuario@dominio.ext */
  correo: '^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$',
  /** Nombre de mascota: solo letras. */
  nombreMascota: `^[${L}]+([ '.\\-]+[${L}]+)*$`,
  /** Color: solo letras, separadas por espacios, comas o guiones. */
  color: `^[${L}]+([ ,\\-]+[${L}]+)*$`,
  /** Microchip: exactamente 15 dígitos. */
  microchip: '^[0-9]{15}$',
  /** Grupo sanguíneo: A, B, AB, DEA 1.1+... */
  grupoSanguineo: '^[A-Za-z0-9 .+\\-\\/]{1,10}$',
  /** Tarjeta profesional: TP-20037 */
  tarjeta: '^[A-Za-z]{2,4}-?[0-9]{3,10}$',
  /** Universidad o certificación. */
  institucion: `^[${L}0-9 .,\\-]{3,100}$`
};

/** Mensaje que se muestra debajo de cada campo cuando no cumple su patrón. */
export const MENSAJES = {
  nombrePersona: 'Solo letras, sin números ni símbolos (mínimo 2).',
  documentoNumerico: 'Solo números, de 6 a 10 dígitos.',
  pasaporte: 'De 6 a 12 letras o números, sin símbolos.',
  telefono: 'Solo números, de 7 a 10 dígitos.',
  direccion: 'Letras, números y # . , - con el número de la casa (por ejemplo Cl. 26 #7-12).',
  correo: 'Debe tener la forma usuario@dominio.com',
  nombreMascota: 'Solo letras, sin números ni símbolos.',
  color: 'Solo letras (por ejemplo Negro y blanco).',
  microchip: 'Exactamente 15 dígitos.',
  grupoSanguineo: 'Letras, números y + - . / (por ejemplo DEA 1.1+).',
  tarjeta: 'Letras y número, por ejemplo TP-20037.',
  institucion: 'Letras, números y . , - (mínimo 3).'
};

/** Patrón del número de documento según el tipo elegido. */
export function patronDocumento(tipo: string): string {
  return tipo === 'PA' ? PATRONES.pasaporte : PATRONES.documentoNumerico;
}

export function mensajeDocumento(tipo: string): string {
  return tipo === 'PA' ? MENSAJES.pasaporte : MENSAJES.documentoNumerico;
}

/**
 * Indica si un campo debe mostrarse en rojo: tiene un error y el usuario ya pasó por él o intentó guardar.
 * Se usa en las plantillas con [class.is-invalid] (Bootstrap muestra el .invalid-feedback que sigue al campo).
 */
export function marcarInvalido(campo: NgModel, intentado: boolean): boolean {
  return !!campo.invalid && (!!campo.touched || intentado);
}

/** Fecha de hoy en formato aaaa-mm-dd (el que usan los input type="date"), con años de diferencia. */
export function fechaISO(aniosAtras = 0): string {
  const fecha = new Date();
  fecha.setFullYear(fecha.getFullYear() - aniosAtras);
  fecha.setMinutes(fecha.getMinutes() - fecha.getTimezoneOffset());   // hora local, no UTC
  return fecha.toISOString().slice(0, 10);
}

/**
 * Revisa que una fecha aaaa-mm-dd esté en un rango. Las fechas en ese formato se pueden comparar como texto.
 * Los validadores min y max de Angular solo funcionan con números, por eso las fechas se revisan aquí.
 */
export function fechaFueraDeRango(valor: string | null | undefined, minimo: string, maximo: string): boolean {
  return !!valor && (valor < minimo || valor > maximo);
}
