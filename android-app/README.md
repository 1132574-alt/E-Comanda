# TFG: Sistema de Comandas Digitales para Restaurante

## 📝 Descripción del Proyecto
Este proyecto es una solución integral para la gestión de un restaurante en tiempo real. Utiliza una arquitectura orientada a servicios en la nube (Firebase) para conectar a Clientes, Cocina y Camareros en un ecosistema unificado.

### Roles del Sistema
1. **Mesa (Cliente)**: Acceso a la carta, personalización de pedidos, seguimiento en tiempo real y avisos de servicio.
2. **Cocina**: Gestión de la cola de preparación y control de stock/disponibilidad de la carta.
3. **Camarero**: Atención de avisos de mesas y gestión de cierre de cuentas (ciclo de vida de la sesión).

---

## 🚀 Instrucciones de Configuración

### Requisitos previos
- Android Studio Iguana o superior.
- Una cuenta de Google para acceder a la consola de Firebase.

### Pasos para la puesta en marcha
1. **Firebase**: El archivo `google-services.json` ya está incluido en el módulo `/app`.
2. **Base de Datos**: 
   - Se utiliza **Firebase Realtime Database** (Región: Europa).
   - URL: `https://e-comanda-aa795-default-rtdb.europe-west1.firebasedatabase.app`
3. **Reglas de Seguridad**: Asegúrate de que las reglas de la base de datos permitan lectura/escritura (para pruebas):
   ```json
   {
     "rules": {
       ".read": "auth != null",
       ".write": "auth != null"
     }
   }
   ```

---

## 🧪 Datos de Prueba (Cuentas de Acceso)
Para probar los flujos, utiliza los siguientes usuarios (el sistema simula un email `usuario@restaurante.com` internamente):

| Rol | Usuario | Contraseña              | Destino |
| :--- | :--- |:------------------------| :--- |
| **Cocinero** | `cocina` | *234567*                | Menú de Gestión (Pedidos/Stock) |
| **Camarero** | `camarero` | *123456*                | Panel de Avisos y Cobros |
| **Mesa** | `mesa1` | *123456* | Carta Digital de Cliente |

---

## 📈 Resultados de las Pruebas Finales (MVP)
Se han ejecutado pruebas de estrés y concurrencia con los siguientes resultados:

1. **Flujo de Pedido**: ✅ Correcto. El pedido se persiste en Firebase y aparece en cocina en <1s.
2. **Sincronización de Stock**: ✅ Correcto. Al desactivar un producto en el Panel de Cocina, el cliente ve el aviso "AGOTADO" al instante.
3. **Gestión de Sesiones**: ✅ Correcto. Al cerrar una mesa como camarero, la sesión cambia a estado `FINALIZADA` y libera los recursos.
4. **Resiliencia**: ✅ Correcto. Manejo de errores en caso de falta de sesión o credenciales incorrectas.

---

## 🛠️ Tecnologías utilizadas
- **Lenguaje**: Java / Android SDK.
- **Base de Datos**: Firebase Realtime Database.
- **Autenticación**: Firebase Auth.
- **Diseño**: Material Design Components.
