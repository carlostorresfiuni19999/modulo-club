package com.sd2022.club.dtos.traspaso;

import com.sd2022.club.dtos.base.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class TraspasoDTO extends BaseDTO implements Serializable {
    private int id;
    private Date fechaTraspaso;
    private static final long serialVersionUID = 1L;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaTraspaso() {
        return fechaTraspaso;
    }

    public void setFechaTraspaso(Date fechaTraspaso) {
        this.fechaTraspaso = fechaTraspaso;
    }
}
