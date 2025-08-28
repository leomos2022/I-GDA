# 📱 Guía de Uso - Calculadora Agroecológica

## 🍔 **Menú Hamburger - Navegación Principal**

### **¿Dónde está?**
El **menú hamburger (☰)** está ubicado en la **parte superior izquierda** de la aplicación, en la barra de título.

### **¿Qué hace?**
- ✅ **Acceso rápido** a todos los módulos desde cualquier pantalla
- ✅ **Navegación fluida** sin interrumpir el trabajo actual
- ✅ **Menú organizado** con iconos y descripciones claras

### **Cómo usar:**
1. **Toca el icono ☰** en la parte superior izquierda
2. **Selecciona el módulo** que quieras visitar
3. **Navega instantáneamente** sin perder tu progreso

---

## 🌍 **Módulo 1: Consulta de Países**

### **¿Cómo funciona?**
La aplicación ahora usa una **API gratuita de GeoNames** para obtener información geográfica de países automáticamente.

### **Pasos para consultar un país:**

1. **Ingresa el código ISO del país** en el campo de texto
   - Ejemplo: `CO` para Colombia
   - Ejemplo: `MX` para México
   - Ejemplo: `AR` para Argentina

2. **Presiona "Consultar país con GeoNames"**
   - La app intentará obtener datos de la API
   - Si la API no está disponible, usará datos predefinidos

3. **Los campos se llenan automáticamente:**
   - ✅ Nombre del país
   - 📏 Largo (km)
   - 📐 Ancho (km)
   - 📊 PD (Promedio de Distancia)

### **Códigos de países disponibles:**
- **América Latina**: CO, MX, AR, BR, CL, PE, EC, VE, BO, PY, UY
- **Europa**: ES, FR, DE, IT, UK
- **Norteamérica**: US, CA
- **Asia**: CN, JP, IN
- **Oceanía**: AU
- **Europa del Este**: RU

### **¿Qué pasa si la API falla?**
- La app usa automáticamente **datos predefinidos** de alta calidad
- Los datos se calculan usando coordenadas geográficas reales
- No hay interrupción en el flujo de trabajo
- **Indicadores visuales** muestran si se usaron datos de API o predefinidos

### **Estados de la consulta:**
- 🟢 **Datos de API**: Se obtuvieron datos en tiempo real de GeoNames
- 🟡 **Datos predefinidos**: Se usaron datos locales de alta calidad
- ❌ **Error**: Código de país no reconocido

---

## 📊 **Módulo 6: Tabla de Sostenibilidad**

### **Nueva tabla mejorada:**
La tabla ahora es **mucho más legible** y organizada:

#### **Columnas disponibles:**
1. **🌾 Alimento** - Nombre del alimento
2. **📏 Dist.** - Distancia recorrida en km
3. **🌍 Nivel** - Nivel de procedencia (Local, Regional, Nacional, etc.)
4. **🛒 Modo** - Modo de adquisición (Produce, Cambia, Compra)
5. **📊 Valor** - Valor acumulado calculado
6. **🌱 CO₂** - Huella de carbono en kg CO₂eq
7. **⭐ Sosten.** - Indicador de sostenibilidad

### **Indicadores de sostenibilidad:**
- 🟢 **Alta sostenibilidad**: < 10 kg CO₂eq
- 🟡 **Media sostenibilidad**: 10-30 kg CO₂eq  
- 🔴 **Baja sostenibilidad**: > 30 kg CO₂eq

### **Mejoras visuales:**
- ✅ **Cards separadas** para cada fila
- ✅ **Colores distintivos** para encabezados
- ✅ **Iconos emoji** para mejor identificación
- ✅ **Espaciado optimizado** para fácil lectura
- ✅ **Elevación y sombras** para profundidad visual

---

## 🚀 **Flujo de Trabajo Completo**

### **Paso 1: Configurar País**
1. **Usa el menú hamburger ☰** para ir al Módulo 1
2. Ingresa código de país (ej: `CO`)
3. Presiona "Consultar país con GeoNames"
4. Verifica que los datos sean correctos
5. Presiona "Continuar"

**💡 Consejo**: Puedes usar el menú hamburger en cualquier momento para cambiar de módulo sin perder tu progreso.

### **Paso 2: Ingresar Alimentos**
1. **Módulo 2**: Ingresa alimentos básicos
2. **Módulo 3**: Configura tablas de distancia
3. **Módulo 4**: Define distancias de transporte
4. **Módulo 5**: Selecciona modos de adquisición

### **Paso 3: Ver Resultados**
1. **Módulo 6**: Tabla de sostenibilidad mejorada
   - Visualiza todos los alimentos
   - Analiza huella de carbono
   - Evalúa sostenibilidad
2. **Módulo 7**: Resultados finales

---

## 🔧 **Solución de Problemas**

### **Problema: "No se pudo obtener información"**
**Solución**: 
- La app automáticamente usa datos predefinidos
- Los datos son de alta calidad y precisos
- No afecta el funcionamiento de la aplicación

### **Problema: Tabla difícil de leer**
**Solución**: 
- La nueva tabla usa cards separadas
- Colores distintivos para cada sección
- Mejor espaciado y tipografía

### **Problema: Código de país no reconocido**
**Solución**: 
- Usa solo los códigos listados arriba
- Los códigos son estándar ISO
- Respeta mayúsculas/minúsculas

---

## 💡 **Consejos de Uso**

### **Para mejores resultados:**
1. **Usa códigos de país estándar** (CO, MX, AR, etc.)
2. **Verifica los datos** antes de continuar
3. **Revisa la tabla de sostenibilidad** para análisis completo
4. **Guarda los resultados** para comparaciones futuras

### **Interpretación de resultados:**
- **PD bajo**: País pequeño, alimentos más locales
- **PD alto**: País grande, mayor dependencia externa
- **Huella de carbono baja**: Alimentos más sostenibles
- **Huella de carbono alta**: Oportunidad de mejora

---

## 🎯 **Beneficios de la Nueva Implementación**

### **Para el usuario:**
- ✅ **Más rápido**: Consulta automática de países
- ✅ **Más preciso**: Datos geográficos reales
- ✅ **Más visual**: Tabla mejorada y legible
- ✅ **Más informativo**: Análisis de sostenibilidad

### **Para el análisis:**
- ✅ **Datos confiables**: Coordenadas geográficas precisas
- ✅ **Cálculos automáticos**: Sin errores manuales
- ✅ **Perspectiva ambiental**: Huella de carbono incluida
- ✅ **Evaluación completa**: Sostenibilidad + dependencia alimentaria

---

**¡La aplicación ahora es más inteligente, visual y fácil de usar!** 🎉
