package com.mvp.crudmicroservice.bond.web.mapper;

import com.mvp.crudmicroservice.Mappable;
import com.mvp.crudmicroservice.bond.domain.Bond;
import com.mvp.crudmicroservice.bond.web.dto.BondDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BondMapper extends Mappable<Bond, BondDto> {
}
