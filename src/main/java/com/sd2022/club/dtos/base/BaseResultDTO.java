package com.sd2022.club.dtos.base;


import java.io.Serializable;
import java.util.List;



public abstract class BaseResultDTO <D extends BaseDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<D> dtos;

    private int pages;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<D> getDtos() {
        return dtos;
    }

    public void setDtos(List<D> dtos) {
        this.dtos = dtos;
    }
}
