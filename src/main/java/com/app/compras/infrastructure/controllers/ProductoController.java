package com.app.compras.infrastructure.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.compras.application.services.IProductoService;
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

@RestController
@RequestMapping("/api/producto")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Productos", description = "API para la gestión de productos")
public class ProductoController {
    @Autowired
    private IProductoService productoService;

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos disponibles")
    @ApiResponse(responseCode = "200", description = "Lista recuperada correctamente", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Producto.class))))
    @GetMapping
    public List<Producto> list() {
        return productoService.findAll();
    }

    @Operation(summary = "Obtener un producto por ID", description = "Devuelve un producto específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado", 
                    content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{idProducto}")
    public ResponseEntity<?> view(
            @Parameter(description = "ID del producto a buscar", required = true, example = "1")
            @PathVariable int idProducto) {
        Optional<Producto> productoOptional = productoService.findById(idProducto);
        if (productoOptional.isPresent()) {
            return ResponseEntity.ok(productoOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponse(responseCode = "201", description = "Producto creado correctamente", 
                content = @Content(schema = @Schema(implementation = Producto.class)))
    @PostMapping
    public ResponseEntity<?> create(
            @Parameter(description = "Datos del producto a crear", required = true)
            @Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.save(producto));
    }

    @Operation(summary = "Actualizar un producto existente", description = "Actualiza los datos de un producto identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto actualizado correctamente", 
                    content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{idProducto}")
    public ResponseEntity<?> update(
            @Parameter(description = "Datos actualizados del producto", required = true)
            @Valid @RequestBody Producto producto, 
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1")
            @PathVariable int idProducto) {
        Optional<Producto> productoOptional = productoService.update(idProducto, producto);
        if (productoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(productoOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto existente según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente", 
                    content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno al eliminar el producto")
    })
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "1")
            @PathVariable int idProducto) {
        Optional<Producto> productoOptional = productoService.findById(idProducto);
        if (!productoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
        Optional<Producto> productoEliminado = productoService.delete(idProducto);
        if (productoEliminado.isPresent()) {
            return ResponseEntity.ok(productoEliminado.orElseThrow());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto");
    }
}