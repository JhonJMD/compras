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

import com.app.compras.application.services.IClienteService;
import com.app.compras.domain.entity.Cliente;

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
@RequestMapping("/api/cliente")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Clientes", description = "API para la gestión de clientes")
public class ClienteController {
    @Autowired
    private IClienteService clienteService;

    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes registrados")
    @ApiResponse(responseCode = "200", description = "Lista recuperada correctamente", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Cliente.class))))
    @GetMapping
    public List<Cliente> list() {
        return clienteService.findAll();
    }

    @Operation(summary = "Obtener un cliente por ID", description = "Devuelve un cliente específico según su ID (documento)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado", 
                    content = @Content(schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> view(
            @Parameter(description = "ID del cliente a buscar (documento)", required = true, example = "123456789")
            @PathVariable String id) {
        Optional<Cliente> clienteOptional = clienteService.findById(id);
        if (clienteOptional.isPresent()) {
            return ResponseEntity.ok(clienteOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear un nuevo cliente", description = "Registra un nuevo cliente en el sistema")
    @ApiResponse(responseCode = "201", description = "Cliente creado correctamente", 
                content = @Content(schema = @Schema(implementation = Cliente.class)))
    @PostMapping
    public ResponseEntity<?> create(
            @Parameter(description = "Datos del cliente a crear", required = true)
            @Valid @RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(cliente));
    }

    @Operation(summary = "Actualizar un cliente existente", description = "Actualiza los datos de un cliente identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente actualizado correctamente", 
                    content = @Content(schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "Datos actualizados del cliente", required = true)
            @Valid @RequestBody Cliente cliente, 
            @Parameter(description = "ID del cliente a actualizar", required = true, example = "123456789")
            @PathVariable String id) {
        Optional<Cliente> clienteOptional = clienteService.update(id, cliente);
        if (clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente existente según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente", 
                    content = @Content(schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno al eliminar el cliente")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del cliente a eliminar", required = true, example = "123456789")
            @PathVariable String id) {
        Optional<Cliente> clienteOptional = clienteService.findById(id);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }
        Optional<Cliente> clienteEliminado = clienteService.delete(id);
        if (clienteEliminado.isPresent()) {
            return ResponseEntity.ok(clienteEliminado.orElseThrow());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el cliente");
    }
}