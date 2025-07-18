package com.challange.dto.responses;

import java.util.List;

public class CarritoDTO {
    private Long carritoId;
    private ClienteDTO cliente;

    private List<CarritoProductoDTO> carritoProductos;

    public Long getCarritoId() {
        return carritoId;
    }

    public void setCarritoId(Long carritoId) {
        this.carritoId = carritoId;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public List<CarritoProductoDTO> getCarritoProductos() {
        return carritoProductos;
    }

    public void setCarritoProductos(List<CarritoProductoDTO> carritoProductos) {
        this.carritoProductos = carritoProductos;
    }
}
