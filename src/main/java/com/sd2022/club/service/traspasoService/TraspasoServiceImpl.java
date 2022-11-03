package com.sd2022.club.service.traspasoService;

import com.sd2022.club.dao.ITraspasoDetalleRepository;
import com.sd2022.club.dao.ITraspasoRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTOResult;
import com.sd2022.club.dtos.traspaso.TraspasoFullCreateDTO;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.club.service.traspasoDetalleService.TraspasoDetalleServiceImpl;
import com.sd2022.entities.models.Traspaso;
import com.sd2022.entities.models.TraspasoDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class TraspasoServiceImpl extends BaseServiceImpl<TraspasoDTO, Traspaso, BaseResultDTO<TraspasoDTO>> implements ITraspasoService{

    @Autowired
    private ITraspasoRepository traspasoRepo;

    @Autowired
    private TraspasoDetalleServiceImpl service;

    @Autowired
    private ITraspasoDetalleRepository detalleRepo;
    @Override
    public Traspaso toEntity(TraspasoDTO dto) {
        Traspaso cabecera = new Traspaso();
        cabecera.setFechaTraspaso(new Date());
        cabecera.setCostoTotal(0);
        return cabecera;
    }

    @Override
    public TraspasoDTO toDTO(Traspaso entity) {
        TraspasoDTO dto = new TraspasoDTO();
        dto.setCostoTotal(entity.getCostoTotal());
        dto.setId(entity.getId());
        dto.setFechaTraspaso(entity.getFechaTraspaso());
        return dto;
    }

    @Override
    public ResponseEntity<TraspasoDTO> findById(int id) {
        Traspaso traspaso = traspasoRepo.findById(id);

        if(traspaso == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<TraspasoDTO>(toDTO(traspaso), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResultDTO<TraspasoDTO>> getAll(Pageable page) {
        List<TraspasoDTO> dtos = traspasoRepo.findAll(page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<TraspasoDTO> result = new TraspasoDTOResult();
        result.setDtos(dtos);
        return new ResponseEntity<BaseResultDTO<TraspasoDTO>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity remove(int id) {
        Traspaso cabecera = traspasoRepo.findById(id);
        if(cabecera == null) {
            return new ResponseEntity("Recurso no encotrado", HttpStatus.NOT_FOUND);
        }



        List<TraspasoDetalle> detalles = detalleRepo.findByIdTraspaso(id);

        for(TraspasoDetalle detalle : detalles){
            try{
               detalleRepo.deleteById(detalle.getId());
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        try{
            traspasoRepo.deleteById(id);
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TraspasoDTO> edit(int id, TraspasoDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<TraspasoDTO> add(TraspasoDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity filtrarEntreFechas(Date inicio, Date fin, Pageable page) {
        List<TraspasoDTO> dtos = traspasoRepo.filtrarEntreFechas(inicio, fin, page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<TraspasoDTO> result = new TraspasoDTOResult();
        result.setDtos(dtos);
        return new ResponseEntity<BaseResultDTO<TraspasoDTO>>(result, HttpStatus.OK);
    }

    @Override
    public Traspaso toEntity(TraspasoFullCreateDTO traspaso) {
        return toEntity(traspaso);
    }

    @Override
    public ResponseEntity add(TraspasoFullCreateDTO traspaso) {
        Traspaso cabecera = toEntity(traspaso);
        cabecera.setCostoTotal(0);
        AtomicInteger costoTotal = new AtomicInteger();
        try{
            traspasoRepo.save(cabecera);
                    traspaso.getTraspasoDetalle()
                            .stream()
                            .map(td -> {
                                try {
                                    return service.toEntity(td);
                                } catch (Exception e) {
                                    remove(cabecera.getId());
                                    throw new RuntimeException(e);
                                }
                            }).forEach(td -> {
                                try {
                                    costoTotal.addAndGet(td.getCosto());
                                    detalleRepo.save(td);
                                } catch (Exception e) {
                                    remove(cabecera.getId());
                                    throw new RuntimeException(e);
                                }

                            });

            cabecera.setCostoTotal(costoTotal.get());
            traspasoRepo.save(cabecera);
        }  catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity(toDTO(cabecera), HttpStatus.OK);
    }

    @Override
    public ResponseEntity edit(int id, TraspasoFullCreateDTO traspaso) {
        if(id != traspaso.getId()){
            return new ResponseEntity("Las referencias no se actualizan", HttpStatus.BAD_REQUEST);
        }

        Traspaso cabecera = traspasoRepo.findById(id);

        if(cabecera == null){
            return new ResponseEntity( HttpStatus.NOT_FOUND);
        }

        AtomicInteger costoTotal = new AtomicInteger();

        detalleRepo.findByIdTraspaso(id).forEach(td -> detalleRepo.deleteById(td.getId()));

        try{
            traspaso.getTraspasoDetalle()
                    .stream()
                    .map(td -> {
                        try {
                            return service.toEntity(td);
                        } catch (Exception e) {
                            remove(cabecera.getId());
                            throw new RuntimeException(e);
                        }
                    }).forEach(td -> {
                        try {
                            costoTotal.addAndGet(td.getCosto());
                            detalleRepo.save(td);
                        } catch (Exception e) {
                            remove(cabecera.getId());
                            throw new RuntimeException(e);
                        }

                    });

            cabecera.setCostoTotal(costoTotal.get());
            traspasoRepo.save(cabecera);
        }  catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity(toDTO(cabecera), HttpStatus.OK);
    }
}
