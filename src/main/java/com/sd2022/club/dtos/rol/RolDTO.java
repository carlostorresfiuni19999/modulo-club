package com.sd2022.club.dtos.rol;

import com.sd2022.club.dtos.base.BaseDTO;

import java.io.Serializable;



public class RolDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rol;
    private int id;

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
