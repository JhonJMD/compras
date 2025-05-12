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

import com.app.compras.application.services.ICompraService;
import com.app.compras.domain.entity.Compra;

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
@RequestMapping("/api/compra")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Compras", description = "API para la gestión de compras")
public class CompraController {
    @Autowired
    private ICompraService compraService;

    @Operation(summary = "Obtener todas las compras", description = "Devuelve una lista de todas las compras registradas")
    @ApiResponse(responseCode = "200", description = "Lista recuperada correctamente", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Compra.class))))
    @GetMapping
    public List<Compra> list() {
        return compraService.findAll();
    }

    @Operation(summary = "Obtener una compra por ID", description = "Devuelve una compra específica según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Compra encontrada", 
                    content = @Content(schema = @Schema(implementation = Compra.class))),
        @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @GetMapping("/{idCompra}")
    public ResponseEntity<?> view(
            @Parameter(description = "ID de la compra a buscar", required = true, example = "1")
            @PathVariable int idCompra) {
        Optional<Compra> compraOptional = compraService.findById(idCompra);
        if (compraOptional.isPresent()) {
            return ResponseEntity.ok(compraOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear una nueva compra", description = "Registra una nueva compra en el sistema")
    @ApiResponse(responseCode = "201", description = "Compra creada correctamente", 
                content = @Content(schema = @Schema(implementation = Compra.class)))
    @PostMapping
    public ResponseEntity<?> create(
            @Parameter(description = "Datos de la compra a crear", required = true)
            @Valid @RequestBody Compra compra) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compraService.save(compra));
    }

    @Operation(summary = "Actualizar una compra existente", description = "Actualiza los datos de una compra identificada por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compra actualizada correctamente", 
                    content = @Content(schema = @Schema(implementation = Compra.class))),
        @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @PutMapping("/{idCompra}")
    public ResponseEntity<?> update(
            @Parameter(description = "Datos actualizados de la compra", required = true)
            @Valid @RequestBody Compra compra, 
            @Parameter(description = "ID de la compra a actualizar", required = true, example = "1")
            @PathVariable int idCompra) {
        Optional<Compra> compraOptional = compraService.update(idCompra, compra);
        if (compraOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(compraOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar una compra", description = "Elimina una compra existente según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Compra eliminada correctamente", 
                    content = @Content(schema = @Schema(implementation = Compra.class))),
        @ApiResponse(responseCode = "404", description = "Compra no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno al eliminar la compra")
    })
    @DeleteMapping("/{idCompra}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID de la compra a eliminar", required = true, example = "1")
            @PathVariable int idCompra) {
        Optional<Compra> compraOptional = compraService.findById(idCompra);
        if (!compraOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compra no encontrada");
        }
        Optional<Compra> compraEliminado = compraService.delete(idCompra);
        if (compraEliminado.isPresent()) {
            return ResponseEntity.ok(compraEliminado.orElseThrow());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la compra");
    }
}