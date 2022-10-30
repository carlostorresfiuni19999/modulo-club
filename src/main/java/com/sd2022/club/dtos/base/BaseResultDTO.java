package com.sd2022.club.dtos.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public abstract class BaseResultDTO <D extends BaseDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<D> dtos;
}
