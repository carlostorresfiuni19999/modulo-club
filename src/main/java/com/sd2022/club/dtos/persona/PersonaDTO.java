package com.sd2022.club.dtos.persona;

import com.sd2022.club.dtos.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
public class PersonaDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    private String apellido;
    private int idClub;
    private String email;
    private String documentoTipo;
    private String documento;
    private String username;
    private Date nacimiento;
    private int cat;
    private int idRol;
}
