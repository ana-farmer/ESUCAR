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
