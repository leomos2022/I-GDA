# 🌱 Calculadora Agroecológica I-GDA

## Descripción
Aplicación Android desarrollada en Kotlin con Jetpack Compose para calcular el Índice de Gestión de Distancia Agroecológica (I-GDA). Esta herramienta permite analizar la sostenibilidad de los alimentos basándose en su origen, transporte y modo de adquisición.

## ✨ Características Principales

### 🚀 Mejoras Implementadas (Última Versión)

#### **API y Consultas Mejoradas**
- **Cache local inteligente**: Almacenamiento local de datos de países para consultas más rápidas
- **Fallback robusto**: Sistema de respaldo con datos predefinidos cuando la API no está disponible
- **Timeouts optimizados**: Configuración de 15 segundos para mejor experiencia móvil
- **Reintentos automáticos**: Reintento automático en fallos de conexión
- **Logging detallado**: Registro completo de operaciones para debugging

#### **Módulo 6 Rediseñado**
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

## 📱 Módulos de la Aplicación

### **Módulo 1: Ubicación del País**
- Selección de país con API de GeoNames
- Cálculo automático de dimensiones geográficas
- Fallback a datos predefinidos

### **Módulo 2: Ingreso de Alimentos**
- Formulario para agregar alimentos
- Validación de datos en tiempo real
- Interfaz intuitiva y responsiva

### **Módulo 3: Tablas de Distancia**
- Configuración de distancias por nivel geográfico
- Interfaz de tabla editable
- Validación de rangos

### **Módulo 4: Distancias de Transporte**
- Configuración de medios de transporte
- Factores de impacto ambiental
- Cálculos automáticos

### **Módulo 5: Modos de Adquisición**
- Selección de modo (Produce, Cambia, Compra)
- Factores de sostenibilidad
- Interfaz de selección intuitiva

### **Módulo 6: Cálculo de Valores** ⭐ **MEJORADO**
- **Vista responsiva**: Adaptación automática a diferentes dispositivos
- **Tabla optimizada**: Mejor legibilidad y organización
- **Indicadores visuales**: Colores y iconos para sostenibilidad
- **Resumen estadístico**: Métricas clave en tiempo real
- **Cache inteligente**: Almacenamiento local de cálculos

### **Módulo 7: Resultados Finales**
- Cálculo del índice I-GDA
- Clasificación de sostenibilidad
- Reportes y exportación

## 🎨 Sistema de Diseño

### **Paleta de Colores**
- **Verde Ecológico**: Color principal (#5A8F6A)
- **Verde Suave**: Superficies (#E6F4EA)
- **Verde Oscuro**: Texto (#476810)
- **Acentos**: Naranja, amarillo y azul para feedback

### **Tipografía**
- **Títulos**: Roboto Bold para jerarquía clara
- **Cuerpo**: Roboto Regular para legibilidad
- **Etiquetas**: Roboto Medium para elementos interactivos
- **Escalado**: Sistema de tamaños consistente

### **Componentes**
- **Tarjetas**: Elevación y sombras sutiles
- **Botones**: Estados claros y feedback visual
- **Indicadores**: Colores semánticos para sostenibilidad
- **Navegación**: Menú hamburger mejorado

## 🚀 Instalación y Uso

### **Requisitos**
- Android 7.0 (API 24) o superior
- Kotlin 1.9.0+
- Android Studio Hedgehog o superior

### **Configuración**
1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar dependencias Gradle
4. Ejecutar en dispositivo o emulador

### **Configuración de APIs**
- **GeoNames**: Usuario demo configurado por defecto
- **RestCountries**: API pública sin autenticación
- **Fallback**: Datos predefinidos para países principales

## 📊 Métricas de Sostenibilidad

### **Cálculo de Huella de Carbono**
- **Pie/Bici**: Factor 1.0x
- **Camión**: Factor 1.3x
- **Barco**: Factor 1.8x
- **Avión**: Factor 3.0x

### **Evaluación de Sostenibilidad**
- **Alta**: < 10 unidades de CO₂
- **Media**: 10-30 unidades de CO₂
- **Baja**: > 30 unidades de CO₂

### **Índice I-GDA**
- **Local**: ≤ 0.25
- **Regional**: ≤ 0.50
- **Nacional**: ≤ 1.00
- **Continental**: ≤ 2.00
- **Internacional**: > 2.00

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

## 📈 Roadmap

### **Próximas Mejoras**
- [ ] Exportación a PDF
- [ ] Sincronización en la nube
- [ ] Múltiples idiomas
- [ ] Widgets de inicio
- [ ] Notificaciones de recordatorio

### **Optimizaciones Técnicas**
- [ ] Compose Multiplatform
- [ ] Kotlin Flow para estado
- [ ] Hilt para inyección de dependencias
- [ ] Room para base de datos local

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor, lee las guías de contribución antes de enviar un pull request.

## 📞 Contacto

Para preguntas o soporte, por favor abre un issue en el repositorio.

---

**🌱 Desarrollado con pasión por la agroecología y la tecnología sostenible**
