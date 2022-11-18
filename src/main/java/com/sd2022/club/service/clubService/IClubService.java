package com.sd2022.club.service.clubService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.service.baseService.IBaseService;



public interface IClubService extends IBaseService<ClubDTO, BaseResultDTO<ClubDTO>> {
    BaseResultDTO<ClubDTO> getAll();
}
