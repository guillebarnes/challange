package com.challange.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
public class CarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrito_id")
    private Long carritoId;

    @ManyToOne
    private ClienteEntity cliente;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoProductoEntity> carritoProductos = new ArrayList<>();

    @ManyToOne()
    private EstadoCarritoEntity estado;

    public Long getCarritoId() {
        return carritoId;
    }

    public void setCarritoId(Long carritoId) {
        this.carritoId = carritoId;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public List<CarritoProductoEntity> getCarritoProductos() {
        return carritoProductos;
    }

    public void setCarritoProductos(List<CarritoProductoEntity> carritoProductos) {
        this.carritoProductos = carritoProductos;
    }

    public EstadoCarritoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoCarritoEntity estado) {
        this.estado = estado;
    }
}
