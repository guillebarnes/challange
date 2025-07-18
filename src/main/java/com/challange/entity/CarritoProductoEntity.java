package com.challange.entity;

import com.challange.entity.id.CarritoProductoId;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "carrito_producto")
public class CarritoProductoEntity {
    @EmbeddedId
    private CarritoProductoId id;

    @ManyToOne
    @MapsId("carritoId")
    @JoinColumn(name = "carrito_id")
    @JsonIgnore
    private CarritoEntity carrito;

    @ManyToOne
    @MapsId("productoId")
    @JoinColumn(name = "producto_id")
    private ProductoEntity producto;

    @Column
    private Integer cantidad;
    @Column
    private Double subtotal;

    @Column(name = "en_promocion")
    private boolean enPromocion;


    public CarritoProductoId getId() {
        return id;
    }

    public void setId(CarritoProductoId id) {
        this.id = id;
    }

    public CarritoEntity getCarrito() {
        return carrito;
    }

    public void setCarrito(CarritoEntity carrito) {
        this.carrito = carrito;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public boolean isEnPromocion() {
        return enPromocion;
    }

    public void setEnPromocion(boolean enPromocion) {
        this.enPromocion = enPromocion;
    }
}
