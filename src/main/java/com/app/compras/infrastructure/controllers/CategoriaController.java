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

import com.app.compras.application.services.ICategoriaService;
import com.app.compras.domain.entity.Categoria;

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
@RequestMapping("/api/categoria")
@CrossOrigin(origins = "http://localhost:5173") 
@Tag(name = "Categorías", description = "API para la gestión de categorías de productos")
public class CategoriaController {
    @Autowired
    private ICategoriaService categoriaService;

    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista de todas las categorías disponibles")
    @ApiResponse(responseCode = "200", description = "Lista recuperada correctamente", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Categoria.class))))
    @GetMapping
    public List<Categoria> list() {
        return categoriaService.findAll();
    }

    @Operation(summary = "Obtener una categoría por ID", description = "Devuelve una categoría específica según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada", 
                    content = @Content(schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{idCategoria}")
    public ResponseEntity<?> view(
            @Parameter(description = "ID de la categoría a buscar", required = true, example = "1")
            @PathVariable int idCategoria) {
        Optional<Categoria> categoriaOptional = categoriaService.findById(idCategoria);
        if (categoriaOptional.isPresent()) {
            return ResponseEntity.ok(categoriaOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría en el sistema")
    @ApiResponse(responseCode = "201", description = "Categoría creada correctamente", 
                content = @Content(schema = @Schema(implementation = Categoria.class)))
    @PostMapping
    public ResponseEntity<?> create(
            @Parameter(description = "Datos de la categoría a crear", required = true)
            @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(categoria));
    }

    @Operation(summary = "Actualizar una categoría existente", description = "Actualiza los datos de una categoría identificada por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría actualizada correctamente", 
                    content = @Content(schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{idCategoria}")
    public ResponseEntity<?> update(
            @Parameter(description = "Datos actualizados de la categoría", required = true)
            @Valid @RequestBody Categoria categoria, 
            @Parameter(description = "ID de la categoría a actualizar", required = true, example = "1")
            @PathVariable int idCategoria) {
        Optional<Categoria> categoriaOptional = categoriaService.update(idCategoria, categoria);
        if (categoriaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría existente según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente", 
                    content = @Content(schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno al eliminar la categoría")
    })
    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID de la categoría a eliminar", required = true, example = "1")
            @PathVariable int idCategoria) {
        Optional<Categoria> categoriaOptional = categoriaService.findById(idCategoria);
        if (!categoriaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria no encontrada");
        }
        Optional<Categoria> categoriaEliminado = categoriaService.delete(idCategoria);
        if (categoriaEliminado.isPresent()) {
            return ResponseEntity.ok(categoriaEliminado.orElseThrow());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la categoria");
    }
}