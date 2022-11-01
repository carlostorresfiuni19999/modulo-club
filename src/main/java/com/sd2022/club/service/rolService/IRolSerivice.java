package com.sd2022.club.service.rolService;

import com.sd2022.club.dtos.rol.RolDTO;
import com.sd2022.club.dtos.rol.RolResultDTO;
import com.sd2022.club.service.baseService.IBaseService;
import com.sd2022.entities.models.Rol;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IRolSerivice extends IBaseService<RolDTO, RolResultDTO> {
    public RolDTO toDTO(Rol entity);
    public Rol toEntity(RolDTO dto);
    public ResponseEntity findByRol(String rol);
}
