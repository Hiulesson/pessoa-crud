package org.acme;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;


@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class PessoaService {
    
    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;

    //metodos de mostrar todos os dados 
    public List<Pessoa> findAll() {
        return this.pessoaMapper.toDomainList(pessoaRepository.findAll().list());
    }

    //Metodo de pesquisa
    public Optional<Pessoa> findById(@NonNull Integer pessoaId) {
        return pessoaRepository.findByIdOptional(pessoaId)
                .map(pessoaMapper::toDomain);
    }


    // Metodo que criar 
    @Transactional
    public void save(@Valid Pessoa pessoa) {
        log.debug("Saving Pessoa: {}", pessoa);
        PessoaEntity entity = pessoaMapper.toEntity(pessoa);
        pessoaRepository.persist(entity);
        pessoaMapper.updateDomainFromEntity(entity, pessoa);
    }

    //Metodo que atualiza
    @Transactional
    public void update(@Valid Pessoa pessoa) {
        log.debug("Updating Pessoa: {}", pessoa);
        if (Objects.isNull(pessoa.getPessoaId())) {
            throw new ServiceException("Pessoa does not have a pessoaId");
        }

        PessoaEntity entity = pessoaRepository.findByIdOptional(pessoa.getPessoaId())
                .orElseThrow(() -> 
                new ServiceException("No Pessoa found for pessoaId[%s]["+ pessoa.getPessoaId() +"]"));
        pessoaMapper.updateEntityFromDomain(pessoa, entity);
        pessoaRepository.persist(entity);
        pessoaMapper.updateDomainFromEntity(entity, pessoa);
    }



}
