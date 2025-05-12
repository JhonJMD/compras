package com.app.compras.domain.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "clientes")
@Data
@Schema(description = "Entidad que representa un cliente")
public class Cliente {
    @Id
    @NotBlank(message = "El ID es obligatorio")
    @Size(min = 5, max = 30, message = "El ID debe tener entre 5 y 30 caracteres")
    @Column(length = 30, nullable = false)
    @Schema(description = "Identificador único del cliente", example = "123456789")
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
    @Column(length = 40, nullable = false)
    @Schema(description = "Nombre del cliente", example = "Juan")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 40, message = "El apellido debe tener entre 2 y 40 caracteres")
    @Column(length = 40, nullable = false)
    @Schema(description = "Apellido del cliente", example = "Pérez")
    private String apellido;

    @NotNull(message = "El número de celular es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Número de celular del cliente", example = "3001234567")
    private Long celular;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
    @Column(length = 200, nullable = false)
    @Schema(description = "Dirección física del cliente", example = "Calle 123, Ciudad")
    private String direccion;

    @Email(message = "El correo electrónico debe ser válido")
    @Column(name = "correo_electronico", length = 100, nullable = true)
    @Schema(description = "Correo electrónico del cliente", example = "juan.perez@example.com")
    private String correoElectronico;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    @Schema(hidden = true)
    private List<Compra> compra;
}