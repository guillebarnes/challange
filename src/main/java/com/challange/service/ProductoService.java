package com.challange.service;

import com.challange.entity.ProductoEntity;
import com.challange.repository.CarritoProductoRepository;
import com.challange.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoDao;

    @Autowired
    private CarritoProductoRepository carritoProductoDao;

    @Autowired
    private CarritoService carritoSvc;

    public ProductoEntity findById(Long idProducto){
        return productoDao.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró producto asociado al id " + idProducto));
    }


}
