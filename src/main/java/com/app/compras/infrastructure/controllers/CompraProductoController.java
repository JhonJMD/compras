package com.app.compras.infrastructure.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.compras.application.services.ICompraProductoService;
import com.app.compras.application.services.ICompraService;
import com.app.compras.application.services.IProductoService;
import com.app.compras.domain.entity.Compra;
import com.app.compras.domain.entity.CompraProducto;
import com.app.compras.domain.entity.CompraProductoId;
import com.app.compras.domain.entity.Producto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/compraProducto")
@CrossOrigin(origins = "http://localhost:5173")
public class CompraProductoController {
    @Autowired
    private ICompraProductoService compraProductoService;

    @Autowired
    private ICompraService compraService;

    @Autowired
    private IProductoService productoService;

    // Obtener todas las compras de productos
    @GetMapping
    public List<CompraProducto> list() {
        return compraProductoService.findAll();
    }

    // Obtener una compra de producto por su clave compuesta
    @GetMapping("/{idCompra}/{idProducto}")
    public ResponseEntity<?> view(@PathVariable int idCompra, @PathVariable int idProducto) {
        CompraProductoId id = new CompraProductoId(idCompra, idProducto);
        Optional<CompraProducto> compraProductoOptional = compraProductoService.findById(id);
        if (compraProductoOptional.isPresent()) {
            return ResponseEntity.ok(compraProductoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Crear una nueva compra de producto
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CompraProducto compraProducto) {
        Optional<Compra> service = compraService.findById(compraProducto.getId().getIdCompra());
        Optional<Producto> supply = productoService.findById(compraProducto.getId().getIdProducto());
        compraProducto.setCompra(service.orElseThrow());
        compraProducto.setProducto(supply.orElseThrow());
        return ResponseEntity.status(HttpStatus.CREATED).body(compraProductoService.save(compraProducto));
    }

    // Actualizar una compra de producto existente
    @PutMapping("/{idCompra}/{idProducto}")
    public ResponseEntity<?> update(@RequestBody CompraProducto compraProducto, @PathVariable int idCompra,
            @PathVariable int idProducto) {
        CompraProductoId id = new CompraProductoId(idCompra, idProducto);
        Optional<CompraProducto> compraProductoOptional = compraProductoService.update(id, compraProducto);
        if (compraProductoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(compraProductoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar una compra de producto por su clave compuesta
    @DeleteMapping("/{idCompra}/{idProducto}")
    public ResponseEntity<?> delete(@PathVariable int idCompra, @PathVariable int idProducto) {
        CompraProductoId id = new CompraProductoId(idCompra, idProducto);
        Optional<CompraProducto> compraProductoOptional = compraProductoService.delete(id);
        if (compraProductoOptional.isPresent()) {
            return ResponseEntity.ok(compraProductoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

}
