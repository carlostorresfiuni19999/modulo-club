package com.sd2022.club.service.base;

import com.sd2022.club.dtos.base.BaseDTO;
import com.sd2022.club.dtos.base.BaseResultDTO;
import org.springframework.data.domain.Pageable;

public interface IBaseService <D extends BaseDTO, P extends BaseResultDTO<D>> {
    public D findById(int id);
    public P getAll(Pageable page);
    public boolean remove(int id);
    public D edit(int id, D dto);
    public D add(D dto);

}
