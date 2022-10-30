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
}
