JAVA CODE CHALLENGE
 
Crear una API REST que simule un sistema de procesamiento de pedidos de alta concurrencia. El sistema debe ser capaz de manejar múltiples solicitudes simultáneas de manera eficiente, evitando problemas comunes de concurrencia como condiciones de carrera, deadlocks y uso ineficiente de recursos.
 
Requisitos del Proyecto
 
# API:
- Desarrollar una API REST con los siguientes casos de uso:
	- Crear un carrito de compras: Recibe el ID de usuario y crea un carrito asociado a ese usuario.
	- Agregar un producto a un carrito: Recibe el código del carrito y el código del producto a agregar.
	- Eliminar un producto de un carrito: Recibe el código del carrito y el código del producto a eliminar.
	- Listar los productos de un carrito: Recibe el código del carrito y devuelve la lista completa de productos del carrito, incluyendo toda la información de la tabla de productos.
	- Procesar el pedido de un carrito: Recibe el código del carrito y procesa la compra, aplicando descuentos cuando corresponda y simulando la validación y cálculo del pedido. Una vez recibido el llamado, el servicio debe devolver un mensaje de “Estamos procesando su orden” y continuar con el procesamiento asincrónicamente.
	- Listar los carritos asociados a un cliente.
- Cada endpoint debe demostrar el uso correcto de validaciones y código según el escenario.
- Implementar seguridad en todos los endpoints.
- Mantener un código limpio, utilizando patrones, modularizado y bien documentado.
 
# Persistencia:
- Toda la información de los carritos debe ser almacenada en una base de datos en memoria (H2).
- La base de datos debe tener las siguientes tablas con una carga inicial:
	- Usuarios: para asociarlos a los carritos.
	- Productos: incluyendo una columna de categoría.
	- Descuentos: tabla con descuentos por categoría.
  
# Pruebas de Estrés:
- Crear un plan de pruebas de stress con alguna herramienta de mercado para simular X rafagas de Y solicitudes concurrentes cada Z segundos. Documentar resultados (tiempos de respuesta, throughput, etc).

# Pruebas y Validación:
- Escribir pruebas unitarias y de integración para la lógica de negocio.
- Validar la ausencia de race conditions y deadlocks.

# Puntos extra:  
- Generar y obtener métricas de uso de cada uno de los endpoints (cantidad de llamados, llamados exitosos, llamados fallidos, tiempo de respuesta, etc).
- Usar herramientas de perfilado para detectar cuellos de botella. Documentar los hallazgos.
 
# Entregables
- Repositorio público con el código fuente (ej: GitHub).
- Informe de rendimiento (obtenido de las pruebas de stress.
- Instrucciones para levantar y probar la API.