package com.sd2022.club.dtos.club;

import com.sd2022.club.dtos.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClubDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String sede;
    private String cancha;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getCancha() {
        return cancha;
    }

    public void setCancha(String cancha) {
        this.cancha = cancha;
    }
}
