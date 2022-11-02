package com.sd2022.club.dtos.traspaso;

import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class TraspasoFullCreateDTO extends TraspasoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<TraspasoDetalleDTO> traspasoDetalle;

}
