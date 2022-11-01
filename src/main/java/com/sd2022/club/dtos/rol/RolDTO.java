package com.sd2022.club.dtos.rol;

import com.sd2022.club.dtos.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class RolDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rol;
    private int id;
}
