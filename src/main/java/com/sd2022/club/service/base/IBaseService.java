package com.sd2022.club.service.base;

import com.sd2022.club.dtos.base.BaseDTO;
import com.sd2022.club.dtos.base.BaseResultDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface IBaseService <D extends BaseDTO, P extends BaseResultDTO<D>> {
    public ResponseEntity<D> findById(int id);
    public ResponseEntity<P> getAll(Pageable page);
    public ResponseEntity remove(int id);
    public ResponseEntity<D> edit(int id, D dto);
    public ResponseEntity<D> add(D dto);

}
