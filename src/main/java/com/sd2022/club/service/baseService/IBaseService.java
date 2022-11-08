package com.sd2022.club.service.baseService;

import com.sd2022.club.dtos.base.BaseDTO;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.errors.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface IBaseService <D extends BaseDTO, P extends BaseResultDTO<D>> {
    public D findById(int id) throws  Exception;
    public P getAll(Pageable page) throws Exception;
    public void remove(int id) throws Exception;
    public D edit(int id, D dto) throws Exception;
    public D add(D dto) throws Exception;

}
