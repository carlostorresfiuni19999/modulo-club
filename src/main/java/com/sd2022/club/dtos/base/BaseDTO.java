package com.sd2022.club.dtos.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class BaseDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
}
