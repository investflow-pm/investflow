package com.mvp.investservice.web.mappers;

import ru.tinkoff.piapi.contract.v1.Share;

import java.util.List;

public interface Mappable<E, D> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dto);

    List<D> toDto(List<E> entities);
}
