package com.challange.controller;

import com.challange.dto.requests.ProductoSeleccionadoDTO;
import com.challange.dto.responses.MensajeDTO;
import com.challange.service.CarritoService;
import com.challange.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private static final Logger LOGGER = LoggerFactory.getLogger( CarritoController.class );
    @Autowired
    private CarritoService carritoSvc;

    @Autowired
    private ProductoService productoSvc;

    @GetMapping("/productos/{idCarrito}")
    public ResponseEntity<?> obtenerTodosLosProductosDeUnCarrito(@PathVariable Long idCarrito){
        try{
            return ResponseEntity.ok(carritoSvc.obtenerTodosLosProductosDeUnCarrito(idCarrito));
        }
        catch (EntityNotFoundException ex){
            return ResponseEntity.ok(new MensajeDTO(ex.getMessage()));
        }
        catch(Exception e){
            LOGGER.error("Ocurrio un error al intentar obtener los productos del carrito {}: {}", idCarrito, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> obtenerCarritosAsociadosAUnCliente(@PathVariable Long idCliente){
        try{
            return ResponseEntity.ok(carritoSvc.obtenerCarritosPorCliente(idCliente));
        }
        catch (EntityNotFoundException ex){
            return ResponseEntity.ok(new MensajeDTO(ex.getMessage()));
        }
        catch(Exception e){
            LOGGER.error("Ocurrio un error al intentar obtener los carritos del cliente {}: {}", idCliente, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping("/crear/{idCliente}")
    public ResponseEntity<?> crearNuevoCarrito(@PathVariable Long idCliente){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(carritoSvc.crearCarrito(idCliente));
        }
        catch(Exception e){
            //TODO: agregar Log4j
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/agregar/{idCarrito}")
    public ResponseEntity<?> agregarProductoACarrito(@PathVariable Long idCarrito, @RequestBody ProductoSeleccionadoDTO producto){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(carritoSvc.agregarProductoACarrito(idCarrito, producto));
        }
        catch (EntityNotFoundException ex){
            return ResponseEntity.ok(new MensajeDTO(ex.getMessage()));
        }
        catch(Exception e){
            LOGGER.error("Ocurrio un problema al intentar agregar producto {} al carrito {}: {}", producto.getIdProducto(), idCarrito, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/finalizar/{idCarrito}")
    public ResponseEntity<?> finalizarCarrito(@PathVariable Long idCarrito){
        try{
            return ResponseEntity.ok(carritoSvc.finalizarCarrito(idCarrito));
        }
        catch(Exception e){
            //TODO: agregar Log4j
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @DeleteMapping("/eliminar/{idCarrito}")
    public ResponseEntity<?> eliminarProductoDelCarrito(@PathVariable Long idCarrito, @RequestBody ProductoSeleccionadoDTO producto){
        try{
            return ResponseEntity.ok().body(carritoSvc.eliminarProductoDelCarrito(idCarrito, producto));
        }
        catch (EntityNotFoundException ex){
            return ResponseEntity.ok(new MensajeDTO(ex.getMessage()));
        }
        catch(Exception e){
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/eliminar/{idCarrito}")
    public ResponseEntity<?> eliminarUnidadDeProductoDelCarrito(@PathVariable Long idCarrito, @RequestBody ProductoSeleccionadoDTO producto){
        try{
            carritoSvc.eliminarUnidadDeProductoDelCarrito(idCarrito, producto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
