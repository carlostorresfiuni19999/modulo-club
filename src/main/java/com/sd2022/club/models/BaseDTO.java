package com.sd2022.club.models;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

@Getter
@Setter
public class BaseDTO implements Serializable{

    @XmlAttribute(name="id")
    private int id;
    
}
