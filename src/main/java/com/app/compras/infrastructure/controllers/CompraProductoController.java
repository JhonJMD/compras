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
import com.app.compras.domain.entity.CompraProducto;
import com.app.compras.domain.entity.CompraProductoId;

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

    @GetMapping
    public List<CompraProducto> list() {
        return compraProductoService.findAll();
    }
    
    @GetMapping("/{idCompraProducto}")
    public ResponseEntity<?> view(@PathVariable CompraProductoId id) {
        Optional<CompraProducto> compraProductoOptional = compraProductoService.findById(id);
        if (compraProductoOptional.isPresent()){
            return ResponseEntity.ok(compraProductoOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CompraProducto compraProducto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compraProductoService.save(compraProducto));
    }
    
    @PutMapping("/{idCompraProducto}")
    public ResponseEntity<?> update(@RequestBody CompraProducto compraProducto, @PathVariable CompraProductoId id) {
        Optional<CompraProducto> compraProductoOptional = compraProductoService.update(id, compraProducto);
        if (compraProductoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(compraProductoOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idCompraProducto}")
    public ResponseEntity<?> delete(@PathVariable CompraProductoId id) {
        Optional<CompraProducto> compraProductoOptional = compraProductoService.findById(id);
        if (!compraProductoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
        Optional<CompraProducto> compraProductoElminado = compraProductoService.delete(id);
        if (compraProductoElminado.isPresent()){
            return ResponseEntity.ok(compraProductoElminado.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }
}
