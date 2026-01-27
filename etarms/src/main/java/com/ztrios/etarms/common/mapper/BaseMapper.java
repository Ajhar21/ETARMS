package com.ztrios.etarms.common.mapper;

public interface BaseMapper<E, R, Q> {

    E mapToEntity(Q request);

    R mapToResponse(E entity);
}

