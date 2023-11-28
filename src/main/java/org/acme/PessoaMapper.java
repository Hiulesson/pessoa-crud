package org.acme;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface PessoaMapper {

    List<Pessoa> toDomainList(List<PessoaEntity> entities);

    Pessoa toDomain(PessoaEntity entity);

    @InheritInverseConfiguration(name = "toDomain")
    PessoaEntity toEntity(Pessoa domain);

    void updateEntityFromDomain(Pessoa domain, @MappingTarget PessoaEntity entity);

    void updateDomainFromEntity(PessoaEntity entity, @MappingTarget Pessoa domain);
}