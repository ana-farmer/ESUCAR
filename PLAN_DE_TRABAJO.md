# Plan de trabajo — Tutor Virtual ESUCAR

## Base revisada

- Proyecto Android en Kotlin, Jetpack Compose y Material 3.
- Caso DSY1105, guías 7 a 13 y documento de actividades autónomas, disponibles en `C:/Users/hug.leon/Desktop/proyectos/Esucar`.
- Referencia visual: `Tutor Virtual ESUCAR App Screens.png`, en la misma carpeta.
- Plan inicial seguido por la implementación del MVP. Consulta README.md para las funciones entregadas y sus límites; la evidencia de verificación se registra en VERIFICACION.md.

## Identidad visual existente

Conservar los valores definidos en `ui/theme/Color.kt`:

| Uso | Color |
| --- | --- |
| Principal institucional | `#007328` |
| Contenedores suaves | `#DCE1D6` |
| Acentos | `#C5A059` |
| Fondo | `#F6FBF3` |
| Tarjetas | `#FFFFFF` |
| Texto | `#191C19` |
| Estado aprobado | `#2E7D32` |
| Estado pendiente | `#E65100` |

Adaptar la estructura del prototipo a esta paleta: encabezados y acciones verdes, tarjetas claras, dorado en detalles. Verificar contraste antes de usar texto pequeño sobre dorado. El escudo y la fotografía de bienvenida requieren recursos originales adecuados; la imagen compuesta sirve como referencia.

## Diagnóstico del código

- Inicio: cuatro botones funcionales de navegación básica.
- Guía: cuatro etapas escritas directamente en la pantalla. `GuiaRepository` contiene cinco etapas con orientaciones, pero no se consume desde la UI.
- Avances: tres hitos fijos con fechas de ejemplo de 2025. El modelo `Hito` y su enumeración no alimentan la pantalla.
- Perfil y preguntas frecuentes: vistas provisionales.
- Navegación: cadena de texto y `when` dentro de `MainActivity`; sin NavHost ni historial de navegación completo.
- Hay modelos y repositorio iniciales, pero no ViewModels ni almacenamiento persistente.
- Existen dos pruebas instrumentadas de navegación; no se han ejecutado durante esta revisión.

## Alcance propuesto

El caso abarca Trabajo de Título I, II y III. El prototipo se concentra en Trabajo de Título I; mantener el alcance general del caso y comenzar con contenido de I, dejando explícita esa cobertura inicial.

El MVP funcionará localmente, sin integración con sistemas internos. El tutor ofrecerá búsqueda y respuestas desde un banco de contenido revisado. Una conversación con IA y la sincronización remota son ampliaciones que requieren definir un servicio y su alcance.

Los estudiantes podrán registrar su progreso y marcar entregas como enviadas. La aprobación y las calificaciones corresponden al docente; sin integración docente, solo se mostrarán como ejemplos identificados o se dejarán pendientes.

## Fases y criterios de cierre

| Fase | Trabajo | Criterio de cierre |
| --- | --- | --- |
| 1. Base técnica | Revisar estado Git, SDK/JDK y dependencias; compilar el estado actual; ordenar UI, modelos, repositorios y ViewModels con StateFlow. | Compilación comprobada y estado de presentación separado de las pantallas. |
| 2. Navegación y tema | Incorporar rutas y NavHost; encabezado compartido; barra inferior Inicio, Unidades, Anteproyecto y Avances; accesos a tutor, biblioteca y perfil. | Todos los destinos abren, Atrás funciona y no se duplican encabezados ni márgenes del sistema. |
| 3. Bienvenida e inicio | Bienvenida con recurso gráfico, botón Comenzar, saludo, progreso calculado y seis accesos en cuadrícula. | Flujo equivalente al prototipo, usando la paleta Kotlin. |
| 4. Unidades y guía | Conectar el repositorio, unificar las cinco etapas y agregar detalles con orientaciones y listas de verificación. | Cada etapa abre contenido y permite registrar progreso. |
| 5. Anteproyecto y formularios | Incorporar problema, delimitación, objetivos, preguntas, justificación y marco conceptual; validaciones y vista de resumen mediante ViewModel compartido. | Los campos muestran errores útiles y conservan los datos entre pantallas. |
| 6. Tutor y biblioteca | FAQ con búsqueda y categorías; interfaz de consultas guiadas; recursos metodológicos y pautas autorizadas. | Consultas y material incluido funcionan sin conexión; recursos externos se identifican. |
| 7. Avances y persistencia | Consumir modelos de hitos; calcular progreso coherente; guardar borradores y estados; preferencias con DataStore y almacenamiento apropiado para datos estructurados. | Inicio, perfil y avances muestran el mismo progreso y los datos sobreviven al reinicio. |
| 8. Perfil y recursos nativos | Formulario de perfil, resumen, avatar desde cámara y galería; manejo de cancelación y permisos según la API utilizada. | Captura y selección verificadas en teléfono físico, sin fallos al cancelar. |
| 9. Recordatorios y adaptación | Notificaciones locales de plazos; layouts por tamaño de ventana; animaciones de progreso y estados. | Pruebas en móvil/tablet, texto ampliado, permiso de notificaciones y recordatorios. |
| 10. Entrega | Pruebas de navegación, validación y persistencia; capturas; README de instalación y arquitectura; evidencias GitHub/Trello. | APK de demostración y checklist de evidencias, con limitaciones documentadas. |

## Relación con las guías

- Guía 7: arquitectura MVVM y evidencias de colaboración.
- Guía 8: pantalla base con Compose, imagen y componentes reutilizables.
- Guía 9: adaptación mediante clases de tamaño de ventana.
- Guía 10: navegación, rutas, barras y menú lateral según la entrega exigida.
- Guía 11: formulario reactivo, validación y resumen con ViewModel compartido.
- Guía 12: estado persistente con DataStore y animaciones.
- Guía 13: perfil con cámara, galería y comprobación en dispositivo físico.

Estas guías orientan la cobertura técnica; no sustituyen la rúbrica específica de la evaluación parcial 2, que no se encontró como archivo independiente.

## Orden de implementación recomendado

Primer bloque: fases 1 a 3 para obtener una base compilable y el inicio del prototipo con colores institucionales. Segundo bloque: guía, anteproyecto y tutor. Tercer bloque: persistencia, perfil, recordatorios y entrega.

No fijar fechas o duración hasta conocer el plazo real y la rúbrica de la entrega. No utilizar como datos reales el 45 % de progreso, las notas ni los plazos ilustrativos del prototipo.


## Estado de implementación

Se implementaron bienvenida, inicio, unidades con detalle, anteproyecto con formulario y resumen, consultas FAQ, biblioteca, avances, perfil, persistencia, cámara/galería, recordatorios locales, menú lateral y barra inferior. Se reutilizaron los modelos Hito y EtapaMetodologica y se eliminó contenido duplicado de las pantallas anteriores.

La adaptación utiliza WindowSizeClass y se aplican animaciones al progreso y a los paneles. Se añadió un script de verificación para el problema local de rutas temporales del JDK.

Quedan para la entrega académica definitiva: material institucional autorizado y rúbrica específica, evidencias de colaboración en GitHub/Trello y comprobación de cámara, galería y notificaciones en un teléfono físico. Los recordatorios deben reprogramarse después de reiniciar el dispositivo. La consulta local no utiliza IA generativa.

El escudo original suministrado por el usuario se integró en bienvenida e inicio, con recorte exterior aplicado al renderizar y sin modificar el dibujo.
