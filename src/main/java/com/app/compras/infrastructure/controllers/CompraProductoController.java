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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/compraProducto")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Compras-Productos", description = "API para la gestión de productos en compras")
public class CompraProductoController {
    @Autowired
    private ICompraProductoService compraProductoService;

    @Autowired
    private ICompraService compraService;

    @Autowired
    private IProductoService productoService;

    @Operation(summary = "Obtener todos los detalles de compras", description = "Devuelve una lista de todos los productos incluidos en compras")
    @ApiResponse(responseCode = "200", description = "Lista recuperada correctamente", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = CompraProducto.class))))
    @GetMapping
    public List<CompraProducto> list() {
        return compraProductoService.findAll();
    }

    @Operation(summary = "Obtener un detalle de compra específico", description = "Devuelve un detalle específico de compra según su ID compuesto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle de compra encontrado", 
                    content = @Content(schema = @Schema(implementation = CompraProducto.class))),
        @ApiResponse(responseCode = "404", description = "Detalle de compra no encontrado")
    })
    @GetMapping("/{idCompra}/{idProducto}")
    public ResponseEntity<?> view(
            @Parameter(description = "ID de la compra", required = true, example = "1")
            @PathVariable int idCompra, 
            @Parameter(description = "ID del producto", required = true, example = "2")
            @PathVariable int idProducto) {
        CompraProductoId id = new CompraProductoId(idCompra, idProducto);
        Optional<CompraProducto> compraProductoOptional = compraProductoService.findById(id);
        if (compraProductoOptional.isPresent()) {
            return ResponseEntity.ok(compraProductoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear un nuevo detalle de compra", description = "Agrega un producto a una compra existente")
    @ApiResponse(responseCode = "201", description = "Detalle de compra creado correctamente", 
                content = @Content(schema = @Schema(implementation = CompraProducto.class)))
    @PostMapping
    public ResponseEntity<?> create(
            @Parameter(description = "Datos del detalle de compra a crear", required = true)
            @Valid @RequestBody CompraProducto compraProducto) {
        Optional<Compra> compra = compraService.findById(compraProducto.getId().getIdCompra());
        Optional<Producto> producto = productoService.findById(compraProducto.getId().getIdProducto());
        
        if (!compra.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La compra especificada no existe");
        }
        
        if (!producto.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El producto especificado no existe");
        }
        
        compraProducto.setCompra(compra.orElseThrow());
        compraProducto.setProducto(producto.orElseThrow());
        return ResponseEntity.status(HttpStatus.CREATED).body(compraProductoService.save(compraProducto));
    }

    @Operation(summary = "Actualizar un detalle de compra existente", description = "Actualiza los datos de un detalle de compra identificado por su ID compuesto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Detalle de compra actualizado correctamente", 
                    content = @Content(schema = @Schema(implementation = CompraProducto.class))),
        @ApiResponse(responseCode = "404", description = "Detalle de compra no encontrado")
    })
    @PutMapping("/{idCompra}/{idProducto}")
    public ResponseEntity<?> update(
            @Parameter(description = "Datos actualizados del detalle de compra", required = true)
            @Valid @RequestBody CompraProducto compraProducto, 
            @Parameter(description = "ID de la compra", required = true, example = "1")
            @PathVariable int idCompra,
            @Parameter(description = "ID del producto", required = true, example = "2")
            @PathVariable int idProducto) {
        CompraProductoId id = new CompraProductoId(idCompra, idProducto);
        Optional<CompraProducto> compraProductoOptional = compraProductoService.update(id, compraProducto);
        if (compraProductoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(compraProductoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar un detalle de compra", description = "Elimina un detalle de compra existente según su ID compuesto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle de compra eliminado correctamente", 
                    content = @Content(schema = @Schema(implementation = CompraProducto.class))),
        @ApiResponse(responseCode = "404", description = "Detalle de compra no encontrado")
    })
    @DeleteMapping("/{idCompra}/{idProducto}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID de la compra", required = true, example = "1")
            @PathVariable int idCompra, 
            @Parameter(description = "ID del producto", required = true, example = "2")
            @PathVariable int idProducto) {
        CompraProductoId id = new CompraProductoId(idCompra, idProducto);
        Optional<CompraProducto> compraProductoOptional = compraProductoService.delete(id);
        if (compraProductoOptional.isPresent()) {
            return ResponseEntity.ok(compraProductoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }
}