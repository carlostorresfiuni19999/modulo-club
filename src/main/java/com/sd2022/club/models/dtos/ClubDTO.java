package com.sd2022.club.models.dtos;

import com.sd2022.club.models.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name="Club")
public class ClubDTO extends BaseDTO {
    @XmlAttribute(name = "nombre")
    private String nombre;

    @XmlAttribute(name="sede")
    private String sede;

    @XmlAttribute(name = "cancha")
    private String cancha;
}
