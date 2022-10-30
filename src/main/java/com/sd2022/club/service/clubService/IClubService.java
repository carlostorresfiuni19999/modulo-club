package com.sd2022.club.service.clubService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.service.base.IBaseService;
import org.springframework.data.domain.Pageable;


public interface IClubService extends IBaseService<ClubDTO, BaseResultDTO<ClubDTO>> {
    public BaseResultDTO<ClubDTO> findBySede(String sede, Pageable page);
    public ClubDTO findByCancha(String cancha);
}
