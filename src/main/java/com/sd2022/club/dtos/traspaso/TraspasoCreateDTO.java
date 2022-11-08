package com.sd2022.club.dtos.traspaso;

import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;

import java.util.List;

public class TraspasoCreateDTO extends TraspasoDTO{
    private List<TraspasoDetalleDTO> detalles;

    public List<TraspasoDetalleDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<TraspasoDetalleDTO> detalles) {
        this.detalles = detalles;
    }
}
