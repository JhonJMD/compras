package com.app.compras.domain.entity;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "compras")
@Data
@Schema(description = "Entidad que representa una compra realizada por un cliente")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    @Schema(description = "Identificador único de la compra", example = "1")
    private int idCompra;

    @NotNull(message = "La fecha es obligatoria")
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha y hora de la compra", example = "2025-05-01T14:30:00Z")
    private Date fecha;

    @NotBlank(message = "El medio de pago es obligatorio")
    @Size(min = 3, max = 20, message = "El medio de pago debe tener entre 3 y 20 caracteres")
    @Column(length = 20, nullable = false, name = "medio_pago")
    @Schema(description = "Método de pago utilizado", example = "Tarjeta de Crédito")
    private String medioPago;

    @Size(max = 100, message = "El comentario no puede exceder los 100 caracteres")
    @Column(length = 100, nullable = true)
    @Schema(description = "Comentarios o notas adicionales sobre la compra", example = "Entrega rápida por favor")
    private String comentario;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^[PEC]$", message = "El estado debe ser P (Pendiente), E (Entregado) o C (Cancelado)")
    @Column(length = 1, nullable = false)
    @Schema(description = "Estado de la compra: P (Pendiente), E (Entregado), C (Cancelado)", example = "P")
    private String estado;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @Schema(description = "Cliente que realizó la compra")
    private Cliente cliente;
}