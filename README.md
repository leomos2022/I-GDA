# 🥗 I-GDA - Índice de Dependencia Alimentaria

**I-GDA** es una calculadora agroecológica interactiva desarrollada en Android con Kotlin + Jetpack Compose. Esta app guía a comunidades y usuarios rurales o académicos a calcular el Índice de Dependencia Alimentaria (I-GDA) a partir de datos reales sobre sus prácticas de consumo, producción y transporte de alimentos.

---

## 🎥 Demo en video

Mira el funcionamiento completo de la aplicación en este video demostrativo:

🔗 [Ver demo en Google Drive](https://drive.google.com/file/d/1z1U7sqgOOtlcgIAYLuZBDkb5I6eagQi4/view?usp=sharing)

> El video muestra la navegación entre módulos, uso de la API de ChatGPT, interfaz adaptable a modo claro/oscuro, entradas personalizadas de alimentos, selección de métodos de adquisición y cálculo automático del I-GDA.

---

## 🧭 Flujo de módulos

| Módulo | Descripción |
|--------|-------------|
| 🌍 Módulo 1 | Ingreso de país y dimensiones geográficas (con consulta automática vía ChatGPT). |
| 🍽️ Módulo 2 | Registro de alimentos consumidos. |
| 📏 Módulo 3 | Ingreso de distancias desde los puntos de origen. |
| 🚚 Módulo 4 | Selección del medio de transporte utilizado. |
| 🛒 Módulo 5 | Método de adquisición: Compra, Cambia o Produce. |
| 📊 Módulo 6 | Cálculo del índice basado en variables ponderadas. |
| 📈 Módulo 7 | Visualización de resultados y recomendaciones finales. |

---

## ⚙️ Tecnología utilizada

- ✅ **Lenguaje:** Kotlin
- ✅ **Framework UI:** Jetpack Compose
- ✅ **Arquitectura:** ViewModel + StateFlow
- ✅ **Persistencia temporal:** MutableState y remember
- ✅ **Lógica de IA:** OpenAI ChatGPT API (consulta automatizada)
- ✅ **Compatibilidad UI:** Light/Dark Theme con `MaterialTheme`
- ✅ **Diseño adaptable:** UI centrado, accesibilidad visual
- ✅ **Componentes personalizados:** Dropdowns dinámicos, barra de progreso visual, validación de entrada

---

## 📐 Lógica matemática

**Fórmula estimada de PD (Parámetro de Distancia):**

```math
PD = (Largo del país + Ancho del país) / 2
