package com.sd2022.club.dtos.traspasodetalle;

import com.sd2022.club.dtos.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;


public class TraspasoDetalleDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;
    private int id;
    private int clubOrigen;
    private int clubDestino;
    private int idPersona;
    private int idTraspaso;
    private int costo;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getClubOrigen() {
        return clubOrigen;
    }

    public void setClubOrigen(int clubOrigen) {
        this.clubOrigen = clubOrigen;
    }

    public int getClubDestino() {
        return clubDestino;
    }

    public void setClubDestino(int clubDestino) {
        this.clubDestino = clubDestino;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public int getIdTraspaso() {
        return idTraspaso;
    }

    public void setIdTraspaso(int idTraspaso) {
        this.idTraspaso = idTraspaso;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }
}
