package com.sd2022.club.service.baseService;

import com.sd2022.club.dtos.base.BaseDTO;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.entities.models.BaseModel;

public abstract class BaseServiceImpl<D extends BaseDTO, E extends BaseModel, P extends BaseResultDTO<D>> implements IBaseService<D, P> {
    public abstract E toEntity(D dto) throws Exception;
    public abstract D toDTO(E entity);
}
