package com.sd2022.club.dtos.traspasodetalle;

import com.sd2022.club.dtos.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraspasoDetalleDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;
    private int id;
    private int clubOrigen;
    private int clubDestino;
    private int idPersona;
    private int idTraspaso;
    private int costo;
}
