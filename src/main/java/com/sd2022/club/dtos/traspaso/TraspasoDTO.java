package com.sd2022.club.dtos.traspaso;

import com.sd2022.club.dtos.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class TraspasoDTO extends BaseDTO implements Serializable {
    private int id;
    private Date fechaTraspaso;
    private static final long serialVersionUID = 1L;
}
