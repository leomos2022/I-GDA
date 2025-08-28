# Changelog - Calculadora Agroecológica

## [2.0.0] - 2024-12-19

### ✨ Nuevas Funcionalidades

#### 🌍 Módulo 1 - Integración con GeoNames API
- **Reemplazo de ChatGPT**: Se eliminó la dependencia de OpenAI y se integró la API gratuita de GeoNames
- **Consulta automática de países**: Ahora se puede consultar información geográfica usando códigos ISO de países
- **Cálculo automático**: Se calculan automáticamente largo, ancho y PD (Promedio de Distancia) del país
- **Sin API Key**: No se requiere configuración de API key, funciona con username "demo"

#### 📊 Módulo 6 - Tabla de Sostenibilidad
- **Tabla mejorada**: Se reemplazó la tabla simple por una LazyColumn con scroll
- **Nuevas columnas**: 
  - Distancia recorrida (km)
  - Huella de carbono (kg CO₂eq)
  - Indicador de sostenibilidad (chip de color)
- **Cálculo de sostenibilidad**:
  - Alta sostenibilidad: < 10 kg CO₂eq (Verde)
  - Media sostenibilidad: 10-30 kg CO₂eq (Amarillo)
  - Baja sostenibilidad: > 30 kg CO₂eq (Rojo)

### 🔧 Mejoras Técnicas

#### Arquitectura
- **CountryRepository**: Nuevo repositorio para encapsular llamadas a GeoNames
- **GeoNamesApiService**: Interfaz Retrofit para la API de GeoNames
- **Cálculos automáticos**: Lógica de conversión de coordenadas geográficas a kilómetros
- **Persistencia mejorada**: Los datos de sostenibilidad se guardan en DataStore

#### Modelos de Datos
- **Alimento**: Se agregaron campos `huellaCarbono` y `sostenibilidad`
- **CountryCalculationResult**: Nuevo modelo para resultados de consulta de países

#### ViewModel
- **Nuevas funciones**:
  - `calcularHuellaCarbono()`: Calcula la huella de carbono por alimento
  - `evaluarSostenibilidad()`: Evalúa el nivel de sostenibilidad
  - `actualizarAlimentosConSostenibilidad()`: Actualiza todos los alimentos con cálculos

### 📱 Interfaz de Usuario

#### 🍔 Menú Hamburger Principal
- **Nuevo menú de navegación** ubicado en la parte superior izquierda
- **Acceso rápido** a todos los módulos desde cualquier pantalla
- **Navegación fluida** sin interrumpir el trabajo actual
- **Diseño organizado** con iconos y descripciones claras

#### Módulo 1
- Campo de entrada para código de país (ej: CO, MX, AR)
- Botón "Consultar país con GeoNames"
- Tarjeta de resultados mostrando nombre, largo, ancho y PD
- Manejo de errores mejorado
- **Información sobre el menú hamburger** para mejor navegación

#### Módulo 6
- Tabla con scroll horizontal y vertical
- Encabezados destacados con colores de Material 3
- Chips de colores para indicar sostenibilidad
- Mejor organización de la información

### 🚀 Configuración

#### Dependencias
- Retrofit 2.9.0 ✅
- Gson Converter ✅
- OkHttp Logging ✅
- Material 3 Components ✅

#### APIs
- **GeoNames**: http://api.geonames.org/countryInfoJSON
- **Username**: "demo" (gratuito para pruebas)
- **Parámetros**: country (código ISO), username

### 🔄 Migración

#### De ChatGPT a GeoNames
1. Se eliminó la dependencia de `BuildConfig.OPENAI_API_KEY`
2. Se reemplazó `OpenAIApiService` por `GeoNamesApiService`
3. Se simplificó la lógica de consulta
4. Se mantuvieron los datos predefinidos como fallback

#### Nuevos Campos de Alimento
- Los alimentos existentes se actualizarán automáticamente con valores por defecto
- La huella de carbono se calcula usando la fórmula: `distancia * factorTransporte`
- La sostenibilidad se evalúa automáticamente según la huella de carbono

### 📋 Uso

#### Consultar País
1. Ingresar código ISO del país (ej: "CO" para Colombia)
2. Presionar "Consultar país con GeoNames"
3. Los campos se llenan automáticamente
4. Continuar al siguiente módulo

#### Ver Sostenibilidad
1. Navegar al Módulo 6
2. La tabla muestra automáticamente:
   - Información del alimento
   - Distancia y modo de transporte
   - Huella de carbono calculada
   - Indicador visual de sostenibilidad

### 🐛 Correcciones
- Se eliminó la dependencia de API key de OpenAI
- Se mejoró el manejo de errores de red
- Se optimizó el rendimiento de la tabla con LazyColumn
- Se corrigieron problemas de persistencia de datos

### 📚 Documentación
- Se actualizó el README principal
- Se agregaron comentarios en el código
- Se documentaron las nuevas funciones del ViewModel
- Se incluyeron ejemplos de uso

---

**Nota**: Esta versión mantiene compatibilidad con datos existentes y mejora significativamente la funcionalidad de sostenibilidad de la aplicación.
