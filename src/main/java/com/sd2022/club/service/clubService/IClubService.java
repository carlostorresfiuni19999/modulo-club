package com.sd2022.club.service.clubService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.service.baseService.IBaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface IClubService extends IBaseService<ClubDTO, BaseResultDTO<ClubDTO>> {
    public ResponseEntity<BaseResultDTO<ClubDTO>> findBySede(String sede, Pageable page);
    public ResponseEntity<ClubDTO> findByCancha(String cancha);
}
