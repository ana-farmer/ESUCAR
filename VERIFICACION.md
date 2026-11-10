# Verificación del MVP — 6 de noviembre de 2026

## Resultado automatizado

Comando: `./tools/verify.ps1 -Tasks assembleDebug,testDebugUnitTest,lintDebug,connectedDebugAndroidTest`.

- Compilación y APK de depuración: correctos.
- Pruebas unitarias: 4 aprobadas, sin fallos.
- Pruebas instrumentadas: 4 aprobadas, sin fallos, en Resizable_Experimental (Android 17 / API 37).
- Lint: 0 errores y 21 advertencias. Incluyen actualizaciones disponibles, recursos de plantilla sin uso y sugerencias de Kotlin; no se cambiaron las versiones principales del proyecto para eliminarlas.
- `git diff --check`: sin errores.

Las pruebas cubren progreso inicial/parcial/completo, exclusión de borradores del progreso, separación entre envío y aprobación docente, contexto de la app, navegación a la guía y sus orientaciones, acceso a hitos y conservación del borrador tras recrear la actividad.

Después de la suite completa se ajustó únicamente la alineación de tarjetas de inicio para que las etiquetas de dos líneas mantengan alturas iguales; se repitieron compilación y lint.

Se revisó visualmente el inicio en anchos de 360 dp (móvil, dos columnas) y 960 dp (tablet, tres columnas), además de la bienvenida. Las capturas corresponden al APK final.

## Evidencias y artefactos

- APK: `app/build/outputs/apk/debug/app-debug.apk`.
- Capturas: `output/screenshots/`.
- Reporte de lint: `app/build/reports/lint-results-debug.html`.
- Reporte de pruebas unitarias: `app/build/reports/tests/testDebugUnitTest/index.html`.
- Reporte de pruebas instrumentadas: `app/build/reports/androidTests/connected/debug/index.html`.

## Comprobaciones manuales pendientes

- Cámara y galería en teléfono físico, incluida cancelación y conservación de la foto.
- Entrega real de notificaciones, permiso denegado y restricciones de batería del fabricante.
- Prueba exhaustiva de lectores de pantalla y fuentes ampliadas.
- Incorporación y revisión del contenido académico institucional, rúbricas y recursos gráficos originales.
- Revisión de la rúbrica específica de evaluación y evidencias de colaboración.

## Límites funcionales

El tutor utiliza búsqueda de preguntas frecuentes locales. El progreso mide apartados completados por el estudiante, no aprobación académica. El sistema no se conecta a servidores institucionales. Los recordatorios deben actualizarse al cambiar fechas/envíos y reprogramarse después de reiniciar el teléfono.


## Pantalla de entrada personalizada

Se reemplazó el salto directo al inicio por una bienvenida en cada apertura nueva de la aplicación. Los visitantes ven Comenzar y eligen cuándo entrar. Un perfil local con nombre y correo guardados recibe un saludo personalizado y una carga de dos segundos antes de navegar al inicio. La bienvenida se retira del historial al entrar para que Atrás no la vuelva a abrir. La recreación de una actividad conserva la pantalla interna.

Se añadieron pruebas del perfil local, del botón Comenzar y del acceso automático tras el saludo. La suite actual contiene seis pruebas unitarias y seis instrumentadas. Esta función utiliza el perfil guardado en el dispositivo; no autentica una cuenta remota.


## Rediseño Ruta de titulación — 10 de noviembre de 2026

Se implementó la propuesta izquierda del comparativo visual: cabecera institucional con escudo, degradado verde y detalle vegetal, curva dorada, bloque principal para continuar, títulos serif, cinco etapas conectadas, próxima entrega y navegación Ruta / Tutor / Biblioteca. El menú conserva los módulos secundarios.

El bloque principal y cada etapa abren, expanden y desplazan al apartado pendiente del anteproyecto. La ruta utiliza ocho apartados guardados; los seis originales se conservan y se incorporan metodología y resultados. La próxima entrega utiliza fechas registradas y muestra una invitación a configurarlas cuando no existen.

Resultado: compilación correcta, nueve pruebas unitarias y siete pruebas instrumentadas aprobadas; lint con cero errores y 34 advertencias. Se verificaron el avance entre etapas, la continuidad de datos anteriores, navegación, borradores, recreación de actividad y bienvenida. Se revisó visualmente la pantalla real en el emulador y se restauraron los datos locales anteriores a las pruebas.

Un bloqueo de un archivo temporal detuvo el primer análisis dentro de Dropbox. La verificación final se ejecutó con un directorio de compilación local externo y lint se ejecutó por separado con un trabajador. Los resultados finales fueron correctos.

Entregables actuales:
- APK: `output/TutorVirtual-ESUCAR.apk`.
- Captura real: `output/screenshots/ruta-titulacion.png`.
- Informe de lint: `output/verification/lint-ruta.txt`.


## Dos perfiles asociados de demostración — 10 de noviembre de 2026

Se incorporaron Estudiante de prueba 1 y Profesor de prueba 1, vinculados por identificadores estables. Cambiar de perfil conserva los datos guardados, abre el inicio correspondiente y recuerda la última cuenta. El perfil y la foto se almacenan por separado. El registro personal anterior permanece intacto y sus borradores se copian una sola vez al estudiante de prueba.

El profesor consulta a su estudiante asignado, sus hitos y las copias de versiones enviadas. Puede aprobar o solicitar correcciones con observaciones. El alumno recibe la revisión y puede reenviar una versión nueva. Los cambios de borrador no alteran la copia enviada. El ViewModel impide que el alumno escriba revisiones, que el profesor edite sus borradores o que se revise una versión obsoleta. Las entregas aprobadas se omiten al calcular la próxima acción y al reprogramar recordatorios.

Verificación: 14 pruebas unitarias y 9 instrumentadas aprobadas, compilación correcta y lint con 0 errores y 32 advertencias. Las pruebas cubren la asociación, permisos, versiones, solicitud de correcciones, reenvío, aprobación y lectura desde ambos perfiles. Dos comprobaciones de prueba se ajustaron durante la validación (coincidencia parcial de texto y preparación explícita del perfil/pantalla inicial).

Tras las pruebas se restauraron los datos previos del emulador y se revisaron visualmente ambos inicios y el selector. Se ajustó únicamente el color del diálogo a la paleta institucional y se repitieron compilación y lint.

APK actualizado: `output/TutorVirtual-ESUCAR.apk`.
Capturas: `output/screenshots/perfil-estudiante.png`, `perfil-profesor.png` y `cambiar-de-perfil.png`.
Informe de lint: `output/verification/lint-perfiles.txt`.

Estas cuentas son locales y no requieren contraseña; no constituyen autenticación de usuarios reales ni sincronización entre dispositivos.
