# Observaciones de Sergio para Sebastián — presentación

La presentación (`documentos/final/Presentacion_Sustentacion_SIGEVET.pptx`) la edita solo Sebastián. Este archivo
tiene lo que Sergio revisó. No se tocó el `.pptx`. Cada punto dice contra qué archivo del repositorio se comprobó.
Lo que no se pudo comprobar está marcado como **[No verificado]**.

## Diapositiva 11 — Infraestructura (la de Sergio)

### Lo que está bien (comprobado en el repositorio)

| Lo que dice la diapositiva | Comprobación |
|---|---|
| Fedora con KVM/QEMU y Lima | `vm/migrar_linux.sh`, paso 1/7: en Linux exige `/dev/kvm` y usa `vmType: qemu` |
| VM Oracle Linux 9 | `vm/sigevet-ol9.yaml`: imagen `_images/oraclelinux-9` |
| MySQL 8.4 | `vm/scripts/instalar_mysql.sh`: módulo `mysql:8.4` |
| App Spring Boot en el puerto 5000 | `app/src/main/resources/application.properties` (`server.port=5000`) y el servicio `sigevet-web` de `vm/scripts/instalar_app.sh` |
| `bash vm/migrar_linux.sh` crea la VM, instala MySQL, carga la base y deja la app funcionando | `vm/migrar_linux.sh`, pasos 5/7 y 6/7 (`instalar_mysql.sh`, `cargar_bd.sh --reiniciar`, `instalar_app.sh`) |
| Notas del orador: MySQL y la app corren dentro de la VM | `vm/scripts/instalar_app.sh` instala el servicio dentro de la VM |

### Cambios que propone Sergio

1. **Cuadro "Servidor Linux" (`TextBox 8`).** Hoy dice `Fedora` y `KVM/QEMU con Lima`. Propuesta:
   `Fedora 44` y `KVM/QEMU con Lima`. El dato "Fedora 44" lo dio Sergio y todavía no está respaldado por el
   `INFORME_SERVIDOR.md` (el que genera `vm/scripts/preparar_servidor_fedora.sh`). **[No verificado]** hasta que
   Juan David lo mande.
2. **La IP `192.168.2.40`.** Se puede poner en ese cuadro, pero es una dirección de la red local y puede cambiar si el
   router la reasigna. **[No verificado]**: confirmarla el día de la grabación. Si hay duda, mejor dejarla fuera de la
   diapositiva y decirla de palabra en el video.
3. **Cuadro "Quien consulta" (`TextBox 14`), línea "MySQL Workbench".** El repositorio se contradice sobre si
   Workbench llega al puerto 3306 desde otro equipo:
   - `vm/migrar_linux.sh` abre los puertos de la VM a `0.0.0.0` y `vm/scripts/instalar_mysql.sh` pone
     `bind-address = 0.0.0.0` y abre el 3306 en el firewall de la VM.
   - `TODO.md` dice que el script nuevo de Juan David publica el 3306 solo en `127.0.0.1`.

   **No puedo verificar esto** sin el servidor. Si solo está en `127.0.0.1`, Workbench entra por túnel SSH o desde el
   mismo portátil, y la diapositiva debería decir "MySQL Workbench (por túnel SSH)". Juan David tiene que confirmarlo
   y de eso depende también el bloque 6 del video.
4. **Nada más en la 11.** Las demás líneas coinciden con los scripts.

## Otras diapositivas que Sergio contrastó con el repositorio

Estas son de Sebastián y de Juan Miguel. Se mandan solo como aviso.

- **Diapositiva 10:** las cifras (22 tablas, 113 restricciones, 1.091 registros, 6 vistas, 2 funciones, 9 disparadores,
  3 procedimientos) coinciden con los archivos `database/`: se contaron en `01_crear_tablas.sql`, `02_dml/*.sql`,
  `03_vistas.sql` y `04_programas.sql`. "vet_app solo puede leer" es correcto en la práctica, pero el rol de lectura
  tiene `SELECT` y también `SHOW VIEW` (`database/01_ddl/00_crear_usuarios.sql`). Que las pruebas den "10/10 en OK" no
  se puede comprobar sin ejecutar: hay 10 pruebas definidas (`02_verificacion.sql`, líneas 73–82) y el registro
  `vm/logs/04_carga_bd.log` muestra 10 filas OK en una ejecución anterior. **[No verificado]** que el registro
  corresponda a los archivos actuales.
- **Diapositiva 14:** el ejercicio 41 de la diapositiva es idéntico al del archivo (líneas 314–321). El resultado de 8
  medicamentos sale también de contar los datos de inventario (ids 2, 7, 8, 13, 14, 15, 16 y 17) y coincide con
  `vm/logs/06_algebra_mysql.log`. "55 de 55 dan el mismo resultado que en la Fase 1": las 55 consultas son idénticas
  a las de `documentos/fase1/08_Consultas_SQL_Algebra_Relacional.sql`, pero el registro compara MySQL contra SQLite, no
  contra el documento de la Fase 1; el resultado no se pudo reproducir aquí.
- **Diapositivas 1 y 15:** solo aparecen tres integrantes. Es a propósito: los nombres se cambian todos juntos en la
  etapa 3 (`TODO.md`). No tocarlas antes.
- **Diapositiva 12:** `java -jar sigevet.jar` y puerto 5000 coinciden con `app/pom.xml` (`finalName` = `sigevet`) y con
  `instalar_app.sh`. La app ya está organizada en capas (`controller`, `service`, `dao`, `model`, `dto`), pero la
  diapositiva no lo dice. Si se quiere, añadir una línea: "Capas: controller → service → dao". Opcional.
- **Diapositiva 13:** el fragmento de código es una versión resumida. En `PersonalDAO.java` (líneas 62–77) el objeto se
  crea con `new Veterinario()`, `new Auxiliar()` o `new Empleado()` y después se llenan sus campos con setters; la
  diapositiva muestra solo la parte de la decisión por tipo. La idea (una lista de `Empleado` y tres versiones de
  `getDescripcion()`) es correcta. Conviene decir "resumido" en la nota del orador.
