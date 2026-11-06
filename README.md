# Tutor Virtual ESUCAR

Aplicación Android académica con Kotlin, Jetpack Compose y Material 3. Conserva la paleta institucional del proyecto: verde `#007328`, verde claro `#DCE1D6`, dorado `#C5A059` y fondo `#F6FBF3`.

## Funciones

- Bienvenida e inicio con seis accesos, menú lateral y navegación inferior.
- Cinco etapas metodológicas alimentadas desde `GuiaRepository`.
- Anteproyecto con seis apartados, validación de completitud, borradores y resumen.
- Tutor de preguntas frecuentes con búsqueda textual y categorías, disponible sin conexión.
- Biblioteca de orientación local y enlace externo a APA Style.
- Registro de fechas y envíos de cuatro hitos, sin aprobaciones ni notas ficticias.
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

Guardar cada borrador antes de salir; «Completar» requiere al menos 20 caracteres, lo cual comprueba completitud mínima y no calidad académica. Se puede volver a guardar como borrador para reabrir el apartado. El tutor usa contenido local, no un servicio de IA. No existe autenticación ni sincronización institucional.

Tras guardar o cambiar fechas y estados de envío, pulsar «Programar / actualizar recordatorios». Repetir después de reiniciar el teléfono. Los hitos enviados no se programan al actualizar recordatorios. El sistema puede retrasar las alarmas y requiere autorización de notificaciones desde Android 13.

La foto se conserva entre reinicios. La cámara se abre con una aplicación externa mediante FileProvider; no se solicita permiso de acceso directo al sensor. Cancelar conserva la foto anterior.

La bienvenida y el inicio utilizan el escudo suministrado por el usuario. El recorte del margen exterior se aplica al renderizar, conservando intacto el archivo original y su diseño. Quedan pendientes fotografías y documentos institucionales autorizados. El contenido local orienta y debe ser revisado por el profesor guía.

## Verificación

Las pruebas unitarias cubren el cálculo del progreso y los estados de entrega; las instrumentadas cubren la entrada a la guía, su detalle, los hitos y la conservación del borrador al recrear la actividad. Cámara, galería y entrega real de notificaciones requieren comprobación adicional en teléfono físico. La cuadrícula y el ancho del contenido se adaptan con WindowSizeClass; queda pendiente validar exhaustivamente tablet y escalas de fuente grandes.
