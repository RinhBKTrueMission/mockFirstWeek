package com.example.mockfirstweek.core.Template;

import com.example.mockfirstweek.core.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class TemplateService<T extends TemplateEntity,ID extends Serializable> {
    private static Logger logger = LoggerFactory.getLogger(TemplateService.class);

    protected TemplateRepository<T,ID> repository;

    public List<T> getAll(){
        return repository.findAll();
    }
    public T get(ID id){
        return repository.findById(id).get();
    }
    public void beforeCreate(T entity){
        entity.setCreated(System.currentTimeMillis());
        entity.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    public void create(T entity){
        beforeCreate(entity);
        repository.save(entity);
        afterCreate(entity);
    }
    public void afterCreate(T entity){

    }

    public void beforeUpdate(T entity){
        entity.setUpdated(System.currentTimeMillis());
        entity.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    public void update(ID id, T entity){
        beforeUpdate(entity);
        T old  = get(id);
        if(Objects.isNull(entity.getCreated())){
            entity.setCreated(old.getCreated());
        }
        if(Objects.isNull(entity.getCreatedBy())){
            entity.setCreatedBy(old.getCreatedBy());
        }
        if(Objects.isNull(old)){
            throw new EntityNotFoundException("Not exist entity with id : " + id);
        }
        repository.save(entity);
        afterUpdate(old, entity);
    }
    public void afterUpdate(T old,T entity){

    }

    public void beforeDelete(T entity){
    }
    public void delete(ID id, T entity){
        beforeDelete(entity);
        T old  = get(id);
       if(Objects.isNull(old)){
           throw new EntityNotFoundException("Not exist entity with id = " + id);
       }
        repository.delete(entity);
        afterUpdate(old, entity);
    }
    public void afterDelete(T old,T entity){

    }
    public List<T> search(String query){
        if(Objects.isNull(query) || query.equals("")){
            return repository.findAll();
        }
        Node rootNode = new RSQLParser().parse(query);
        Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());
        return repository.findAll(spec);
    }

    public Page<T> search(String query, Pageable pageable){
        logger.info("query : {}", query);
        if(Objects.isNull(query) || query.equals("")){
            return repository.findAll(pageable);
        }
        try {
            Node rootNode = new RSQLParser().parse(query);
            Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());
            return repository.findAll(spec,pageable);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("SEARCH FAIL : {}", query);
            return emptyPage();
        }
    }

    public Page<T> emptyPage() {
        return new Page<T>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super T, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<T> getContent() {
                return null;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<T> iterator() {
                return null;
            }
        };
    }

}
