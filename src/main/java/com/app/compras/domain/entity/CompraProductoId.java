package com.app.compras.domain.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CompraProductoId implements Serializable {
    private int idCompra;
    private int idProducto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompraProductoId)) return false;
        CompraProductoId that = (CompraProductoId) o;
        return idCompra == that.idCompra && idProducto == that.idProducto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra, idProducto);
    }
}