# 🌱 Calculadora Agroecológica I-GDA

## 📱 Descripción
La **Calculadora Agroecológica I-GDA** es una aplicación móvil desarrollada para Android que permite a comunidades, productores, investigadores y consumidores analizar y visualizar la dependencia alimentaria de su dieta o territorio. 

Utiliza el **Índice de Gestión de Distancia Agroecológica (I-GDA)** para cuantificar la procedencia de los alimentos y su impacto en la soberanía alimentaria y la sostenibilidad.

### 🎯 **Objetivo Principal**
Promover la **soberanía alimentaria** ayudando a identificar si la dieta es local, regional, nacional o internacional, y fomentar la **sostenibilidad** visualizando el impacto de la distancia y el transporte de los alimentos.

---

## ✨ Características Principales

### 🚀 Mejoras Implementadas (Última Versión)

#### **API y Consultas Mejoradas**
- **Cache local inteligente**: Almacenamiento local de datos de países para consultas más rápidas
- **Fallback robusto**: Sistema de respaldo con datos predefinidos cuando la API no está disponible
- **Timeouts optimizados**: Configuración de 15 segundos para mejor experiencia móvil
- **Reintentos automáticos**: Reintento automático en fallos de conexión
- **Logging detallado**: Registro completo de operaciones para debugging

#### **Módulo 6 Rediseñado** ⭐ **ESTRELLA**
- **Vista responsiva**: Adaptación automática a diferentes tamaños de pantalla
- **Tabla optimizada**: Vista de tabla completa para tablets y landscape
- **Tarjetas móviles**: Vista de tarjetas individuales para dispositivos móviles
- **Indicadores visuales**: Colores y iconos para sostenibilidad (🟢🟡🔴)
- **Resumen estadístico**: Panel de resumen con métricas clave
- **Mejor legibilidad**: Tipografía optimizada y espaciado consistente

#### **Coherencia Visual y UX**
- **Paleta de colores unificada**: Esquema de colores coherente en toda la aplicación
- **Tipografía mejorada**: Sistema de tipos consistente y legible
- **Componentes reutilizables**: Biblioteca de componentes para mantener consistencia
- **Temas adaptativos**: Soporte completo para modo claro y oscuro
- **Navegación mejorada**: Menú hamburger rediseñado con mejor información

#### **Rendimiento y Estabilidad**
- **Cache de cálculos**: Almacenamiento local de resultados para evitar recálculos
- **Manejo de errores**: Sistema robusto de manejo de excepciones
- **Persistencia de datos**: Almacenamiento local con DataStore
- **Logging estructurado**: Sistema de logs para monitoreo y debugging

---

## 🏗️ Arquitectura

### **Tecnologías Utilizadas**
- **Kotlin**: Lenguaje principal de desarrollo
- **Jetpack Compose**: UI declarativa moderna
- **Material Design 3**: Sistema de diseño actualizado
- **MVVM**: Arquitectura de presentación
- **DataStore**: Almacenamiento de preferencias
- **Retrofit**: Cliente HTTP para APIs
- **Coroutines**: Programación asíncrona

### **Estructura del Proyecto**
```
app/src/main/java/com/example/calculadoraagroecologica/
├── MainActivity.kt              # Actividad principal con navegación
├── ui/                          # Capa de presentación
│   ├── CalculadoraViewModel.kt  # ViewModel principal
│   ├── Modulo6Screen.kt         # Pantalla del módulo 6 mejorada
│   ├── CountryRepository.kt     # Repositorio de países con cache
│   ├── CountryApiService.kt     # Servicio de API mejorado
│   ├── Extensions.kt            # Utilidades y componentes reutilizables
│   └── theme/                   # Sistema de temas
│       ├── Theme.kt             # Tema principal mejorado
│       ├── Color.kt             # Paleta de colores unificada
│       └── Type.kt              # Tipografía optimizada
└── model/                       # Modelos de datos
    └── DataModels.kt            # Estructuras de datos
```

---

## 📱 Módulos de la Aplicación

### **🌍 Módulo 1: Ubicación del País**
**Funcionalidad**: Configuración inicial del territorio de análisis
- **Selección de país**: Búsqueda por código ISO (CO, MX, AR, BR, ES, US, etc.)
- **API de GeoNames**: Consulta automática de dimensiones geográficas
- **Fallback inteligente**: Datos predefinidos para países principales
- **Cálculo automático**: Dimensión promedio (PD) del territorio
- **Validación**: Verificación de códigos de país válidos

### **🍽️ Módulo 2: Ingreso de Alimentos**
**Funcionalidad**: Registro de alimentos a analizar
- **Formulario dinámico**: Agregar/eliminar alimentos según necesidad
- **Validación en tiempo real**: Verificación de datos ingresados
- **Interfaz intuitiva**: Campos claros y fáciles de completar
- **Persistencia local**: Almacenamiento automático de datos
- **Navegación fluida**: Transición suave entre pasos

### **📏 Módulo 3: Tablas de Distancia**
**Funcionalidad**: Configuración de rangos de distancia por nivel geográfico
- **Niveles configurables**: Mundial, Continental, Nacional, Regional, Zonal, Local
- **Categorías de distancia**: Muy lejano, Lejano, Intermedio, Cercano
- **Interfaz de tabla editable**: Modificación directa de rangos
- **Validación de rangos**: Verificación de coherencia entre niveles
- **Presets predefinidos**: Configuraciones estándar disponibles

### **🚚 Módulo 4: Distancias de Transporte**
**Funcionalidad**: Registro de kilómetros recorridos por cada alimento
- **Clasificación automática**: Determinación de nivel y categoría según distancia
- **Validación de datos**: Verificación de rangos de distancia
- **Interfaz responsiva**: Adaptación a diferentes tamaños de pantalla
- **Cálculos en tiempo real**: Actualización automática de clasificaciones
- **Feedback visual**: Indicadores claros de nivel y categoría

### **🛒 Módulo 5: Modos de Adquisición**
**Funcionalidad**: Selección del método de obtención de alimentos
- **Opciones disponibles**: Produce, Cambia, Compra
- **Factores de sostenibilidad**: Impacto ambiental de cada modo
- **Medios de transporte**: Pie/Bici, Camión, Barco, Avión
- **Barra de progreso**: Seguimiento visual del avance
- **Validación completa**: Verificación de todos los campos requeridos

### **📊 Módulo 6: Cálculo de Valores** ⭐ **COMPLETAMENTE REDISEÑADO**
**Funcionalidad**: Análisis integral de sostenibilidad y valores acumulados
- **Vista responsiva**: Adaptación automática a diferentes dispositivos
  - **Tablets/Landscape**: Vista de tabla completa con todas las columnas
  - **Móviles**: Vista de tarjetas individuales para mejor legibilidad
- **Cálculos avanzados**: Valor acumulado, huella de carbono, sostenibilidad
- **Indicadores visuales**: 
  - 🟢 **Alta sostenibilidad**: < 10 unidades de CO₂
  - 🟡 **Media sostenibilidad**: 10-30 unidades de CO₂
  - 🔴 **Baja sostenibilidad**: > 30 unidades de CO₂
- **Resumen estadístico**: Panel con métricas clave en tiempo real
- **Cache inteligente**: Almacenamiento local de cálculos para evitar recálculos

### **📈 Módulo 7: Resultados Finales**
**Funcionalidad**: Presentación del análisis completo y recomendaciones
- **Índice I-GDA**: Cálculo final del índice de dependencia alimentaria
- **Clasificación automática**: Local, Regional, Nacional, Continental, Internacional
- **Estadísticas detalladas**: Resumen completo de la huella agroecológica
- **Visualizaciones**: Gráficos y tablas de resultados
- **Exportación**: Opciones para imprimir y enviar por correo

---

## 🎨 Sistema de Diseño

### **Paleta de Colores**
- **Verde Ecológico**: Color principal (#5A8F6A) - Sostenibilidad
- **Verde Suave**: Superficies (#E6F4EA) - Fondos y contenedores
- **Verde Oscuro**: Texto (#476810) - Contraste y legibilidad
- **Acentos**: Naranja, amarillo y azul para feedback y estados

### **Tipografía**
- **Títulos**: Roboto Bold para jerarquía clara
- **Cuerpo**: Roboto Regular para legibilidad óptima
- **Etiquetas**: Roboto Medium para elementos interactivos
- **Escalado**: Sistema de tamaños consistente y accesible

### **Componentes**
- **Tarjetas**: Elevación y sombras sutiles para profundidad
- **Botones**: Estados claros y feedback visual inmediato
- **Indicadores**: Colores semánticos para sostenibilidad
- **Navegación**: Menú hamburger mejorado con información contextual

---

## 🚀 Instalación y Uso

### **Requisitos del Sistema**
- **Android**: 7.0 (API 24) o superior
- **Kotlin**: 1.9.0+
- **Android Studio**: Hedgehog o superior
- **Memoria**: Mínimo 2GB RAM recomendado

### **Configuración del Proyecto**
1. **Clonar repositorio**:
   ```bash
   git clone https://github.com/leomos2022/I-GDA.git
   ```
2. **Abrir en Android Studio**
3. **Sincronizar dependencias** Gradle
4. **Ejecutar** en dispositivo o emulador

### **Configuración de APIs**
- **GeoNames**: Usuario demo configurado por defecto
- **RestCountries**: API pública sin autenticación
- **Fallback**: Datos predefinidos para países principales
- **Red**: Configuración de seguridad para comunicaciones HTTP

---

## 📊 Métricas de Sostenibilidad

### **Cálculo de Huella de Carbono**
- **Pie/Bici**: Factor 1.0x (Más sostenible)
- **Camión**: Factor 1.3x (Impacto moderado)
- **Barco**: Factor 1.8x (Impacto alto)
- **Avión**: Factor 3.0x (Impacto muy alto)

### **Evaluación de Sostenibilidad**
- **Alta**: < 10 unidades de CO₂ (🟢 Recomendado)
- **Media**: 10-30 unidades de CO₂ (🟡 Moderado)
- **Baja**: > 30 unidades de CO₂ (🔴 Mejorar)

### **Índice I-GDA (Clasificación)**
- **Local**: ≤ 0.25 (Soberanía alimentaria alta)
- **Regional**: ≤ 0.50 (Dependencia externa baja)
- **Nacional**: ≤ 1.00 (Dependencia externa moderada)
- **Continental**: ≤ 2.00 (Dependencia externa alta)
- **Internacional**: > 2.00 (Dependencia externa muy alta)

---

## 🔧 Desarrollo y Contribución

### **Estructura de Commits**
- `feat:` Nuevas características
- `fix:` Correcciones de bugs
- `refactor:` Refactorización de código
- `docs:` Documentación
- `style:` Mejoras de estilo y UI

### **Estándares de Código**
- **Kotlin**: Estilo oficial de Google
- **Compose**: Patrones recomendados de Material Design 3
- **Arquitectura**: MVVM con Clean Architecture
- **Testing**: Unit tests para ViewModels

---

## 📈 Roadmap y Próximas Mejoras

### **Funcionalidades Planificadas**
- [ ] **Exportación a PDF**: Generación de reportes imprimibles
- [ ] **Sincronización en la nube**: Backup y sincronización entre dispositivos
- [ ] **Múltiples idiomas**: Soporte para español, inglés y otros idiomas
- [ ] **Widgets de inicio**: Acceso rápido a métricas clave
- [ ] **Notificaciones**: Recordatorios y alertas personalizables

### **Optimizaciones Técnicas**
- [ ] **Compose Multiplatform**: Extensión a iOS y web
- [ ] **Kotlin Flow**: Mejoras en el manejo de estado
- [ ] **Hilt**: Inyección de dependencias avanzada
- [ ] **Room**: Base de datos local robusta

---

## 📄 Licencia

Este proyecto está bajo la **Licencia MIT**. Ver el archivo `LICENSE` para más detalles.

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas y altamente valoradas. Por favor:
1. **Fork** el repositorio
2. **Crea** una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abre** un Pull Request

---

## 📞 Contacto y Soporte

- **Repositorio**: [https://github.com/leomos2022/I-GDA](https://github.com/leomos2022/I-GDA)
- **Issues**: Para reportar bugs o solicitar features
- **Discussions**: Para preguntas y colaboración
- **Wiki**: Documentación detallada y guías de uso

---

## 🌟 Agradecimientos

- **Comunidad agroecológica** por la inspiración y feedback
- **Contribuidores** que han mejorado la aplicación
- **Material Design** por el sistema de diseño
- **Jetpack Compose** por la UI declarativa moderna

---

**🌱 Desarrollado con pasión por la agroecología y la tecnología sostenible**

*¡Juntos por una alimentación más local, justa y sostenible!*
