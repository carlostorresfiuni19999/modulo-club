package com.sd2022.club.dtos.traspaso;

import com.sd2022.club.dtos.base.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class TraspasoDTO extends BaseDTO implements Serializable {
    private int id;
    private String fechaTraspaso;
    private static final long serialVersionUID = 1L;

    private String descripcion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaTraspaso() {
        return fechaTraspaso;
    }

    public void setFechaTraspaso(String fechaTraspaso) {
        this.fechaTraspaso = fechaTraspaso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
