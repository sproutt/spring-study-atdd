package support.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;

public interface GenericMapper<D, E> {

    D toDto(E e);

    E toEntity(D d);

    @BeanMapping
    E updateFromDto(D dto, @MappingTarget E entity);
}
