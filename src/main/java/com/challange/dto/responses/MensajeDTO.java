package com.challange.dto.responses;

public class MensajeDTO {

    public static final String MENSAJE_PROCESO = "Estamos procesando su pedido...";
    public static final String MENSAJE_CLIENTE_NO_ENCONTRADO = "No se encontró cliente con ese id";
    public static final String MENSAJE_CARRITO_NO_ENCONTRADO = "No se encontró carrito con ese id";
    public static final String MENSAJE_PRODUCTO_NO_ENCONTRADO = "No se encontró producto con ese id";
    private String mensaje;

    public MensajeDTO(String mensaje){
        this.mensaje = mensaje;
    }
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
