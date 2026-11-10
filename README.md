# Tutor Virtual ESUCAR

Aplicación Android académica con Kotlin, Jetpack Compose y Material 3. Conserva la paleta institucional del proyecto: verde `#007328`, verde claro `#DCE1D6`, dorado `#C5A059` y fondo `#F6FBF3`.

## Funciones

- Pantalla inicial en cada apertura: Comenzar para visitantes y saludo personalizado con transición automática de dos segundos para perfiles locales guardados.
- Inicio Ruta de titulación: cabecera institucional, acción para continuar, cinco etapas conectadas y próxima entrega.
- Navegación inferior Ruta / Tutor / Biblioteca y menú para guía, anteproyecto, avances y perfil.
- Cinco etapas metodológicas alimentadas desde `GuiaRepository`.
- Anteproyecto con ocho apartados, validación de completitud, borradores y resumen.
- Tutor de preguntas frecuentes con búsqueda textual y categorías, disponible sin conexión.
- Biblioteca de orientación local y enlace externo a APA Style.
- Registro de cuatro hitos, envío de versiones y revisión del profesor de prueba con observaciones, aprobación o solicitud de correcciones.
- Progreso calculado desde los apartados completados y compartido entre pantallas.
- Perfil editable, cámara mediante aplicación del sistema y galería con permiso persistente de lectura.
- Notificaciones locales aproximadas a las 09:00 del día de entrega.
- Guardado local con DataStore y ViewModel con StateFlow.

## Ejecutar

Abrir esta carpeta con Android Studio, sincronizar Gradle y ejecutar el módulo `app` en un dispositivo API 24 o superior. El SDK configurado es compileSdk 37 / targetSdk 36; usar el JDK integrado de Android Studio.

```powershell
./gradlew.bat assembleDebug testDebugUnitTest
./gradlew.bat connectedDebugAndroidTest lintDebug
```

APK de depuración: `app/build/outputs/apk/debug/app-debug.apk`.

Si el JDK de Windows muestra `Unable to establish loopback connection`, utilizar `tools/verify.ps1`, que configura una ruta temporal corta únicamente para el proceso de verificación.

## Organización

- `MainActivity.kt`: entrada y tema.
- `ui/screens/EsucarApp.kt`: navegación, componentes compartidos y pantallas.
- `ui/screens/Reminders.kt`: programación y recepción de recordatorios.
- `viewmodel/TutorViewModel.kt`: estado compartido y persistencia local.
- `data/repository`: guía y preguntas frecuentes didácticas.
- `ui/theme`: colores y tipografía.

La navegación activa utiliza `EsucarApp`; se eliminaron las pantallas anteriores para evitar mantener contenido duplicado.

## Uso y alcance

Guardar cada borrador antes de salir; «Completar» requiere al menos 20 caracteres, lo cual comprueba completitud mínima y no calidad académica. Se puede volver a guardar como borrador para reabrir el apartado. El tutor usa contenido local, no un servicio de IA. La app incluye dos identidades locales de demostración y recuerda la última seleccionada. No existe autenticación ni sincronización institucional.

Tras guardar o cambiar fechas y estados de envío, pulsar «Programar / actualizar recordatorios». Repetir después de reiniciar el teléfono. Los hitos enviados no se programan al actualizar recordatorios. El sistema puede retrasar las alarmas y requiere autorización de notificaciones desde Android 13.

La foto se conserva entre reinicios. La cámara se abre con una aplicación externa mediante FileProvider; no se solicita permiso de acceso directo al sensor. Cancelar conserva la foto anterior.

La bienvenida y el inicio utilizan el escudo suministrado por el usuario. El recorte del margen exterior se aplica al renderizar, conservando intacto el archivo original y su diseño. Quedan pendientes fotografías y documentos institucionales autorizados. El contenido local orienta y debe ser revisado por el profesor guía.

## Verificación

Las pruebas unitarias cubren el cálculo del progreso y los estados de entrega; las instrumentadas cubren la entrada a la guía, su detalle, los hitos y la conservación del borrador al recrear la actividad. Cámara, galería y entrega real de notificaciones requieren comprobación adicional en teléfono físico. La cuadrícula y el ancho del contenido se adaptan con WindowSizeClass; queda pendiente validar exhaustivamente tablet y escalas de fuente grandes.

## Icono de la aplicación

El icono de lanzamiento utiliza el escudo original sobre verde institucional, con variantes adaptativas, redondas y de compatibilidad para Android 7. La capa monocromática utiliza un escudo con libro para los iconos temáticos del sistema. Los recursos pueden regenerarse con `java tools/GenerateLauncherIcons.java .`.

## Diseño Ruta de titulación

El inicio implementa la propuesta visual de Ruta: fondo claro, cabecera verde oscuro con escudo y decoración vegetal, títulos serif, acentos dorados, una acción principal y una línea vertical de etapas. Los valores proceden del perfil, los borradores y las fechas guardadas; no se precargan ejemplos de progreso ni entregas.

Las etapas agrupan ocho apartados: problema/delimitación, objetivos/preguntas/justificación, marco conceptual, metodología y resultados. Al pulsar una etapa se abre y se desplaza al apartado pendiente. El menú de la cabecera conserva el acceso a perfil y módulos secundarios.

Los seis apartados originales permanecen guardados. Si estaban completos, representan el 75 % de los ocho apartados actuales y la ruta continúa en metodología. El estado de completitud registrado por el estudiante no equivale a aprobación docente.

## Dos perfiles de prueba asociados

- **Estudiante de prueba 1**: `estudiante1@esucar.example`.
- **Profesor de prueba 1**: `profesor1@esucar.example`.

El estudiante está asignado exclusivamente a ese profesor. El botón **Cambiar de perfil** está en ambas cabeceras, las pantallas internas y el menú. El cambio vuelve al inicio del rol seleccionado y conserva los cambios guardados. Las identidades de prueba y su asociación son fijas; la foto y asignatura se guardan separadas para cada perfil.

La primera apertura selecciona el estudiante; las posteriores recuerdan el último perfil. Es una demostración local sin contraseña. El registro personal anterior se conserva como respaldo y sus borradores se copian una sola vez al estudiante de prueba.

### Probar una revisión

1. Como estudiante, completar y guardar los apartados de un hito (mínimo 20 caracteres cada uno).
2. En Mis avances, pulsar **Enviar al profesor**. Se guarda una copia de la versión enviada.
3. Pulsar **Cambiar de perfil** y elegir Profesor de prueba 1.
4. Abrir las entregas del estudiante, leer la versión enviada y aprobarla o solicitar correcciones con una observación.
5. Volver al estudiante para ver el estado y la retroalimentación. Enviar una nueva versión inicia una nueva revisión.

Las ediciones posteriores del borrador no alteran la copia enviada. El profesor no modifica borradores del alumno y el alumno no puede asignarse aprobaciones. El ViewModel comprueba estas operaciones, además de separar sus pantallas. Son permisos de una demostración en un solo dispositivo; no equivalen a autenticación remota.
