package com.cloud.serviceadmin.service.support.impl;

import com.cloud.serviceadmin.entity.support.BaseEntity;
import com.cloud.serviceadmin.jpa.dao.support.IBaseDao;
import com.cloud.serviceadmin.service.support.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class BaseServiceImpl<T extends BaseEntity, ID extends Serializable> implements IBaseService<T, ID> {


    //提供一个抽象方法 当前类的子类需要提供具体实现类的 Dao
    public abstract IBaseDao<T, ID> getBaseDao();

    @Override
    public Optional<T> findById(ID id) {
        return getBaseDao().findById(id);
    }

    @Override	
    public List<T> findAll() {
        return getBaseDao().findAll();
    }

    @Override
    public List<T> findAllByIds(ID[] ids) {
        List<ID> idList = Arrays.asList(ids);
        return getBaseDao().findAllById(idList);
    }

    @Override
    public List<T> findList(Specification<T> spec, Sort sort) {
        return getBaseDao().findAll(spec, sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return getBaseDao().findAll(pageable);
    }

    @Override
    public long count() {
        return getBaseDao().count();
    }

    @Override
    public long count(Specification<T> spec) {
        return getBaseDao().count(spec);
    }

    @Override
    public boolean exists(ID id) {
        return getBaseDao().existsById(id);
    }

    @Override
    public void save(T entity) {
        getBaseDao().save(entity);
    }

    public void save(Iterable<T> entitys) {
        getBaseDao().saveAll(entitys);
    }

    @Override
    public T update(T entity) {
        return getBaseDao().saveAndFlush(entity);
    }

    @Override
    public void delete(ID id) {
        getBaseDao().deleteById(id);
    }

    @Override
    public void deleteByIds(@SuppressWarnings("unchecked") ID... ids) {
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                ID id = ids[i];
                this.delete(id);
            }
        }
    }

    @Override
    public void delete(T[] entitys) {
        List<T> tList = Arrays.asList(entitys);
        getBaseDao().deleteAll(tList);
    }

    @Override
    public void delete(Iterable<T> entitys) {
        getBaseDao().deleteAll(entitys);
    }

    @Override
    public void delete(T entity) {
        getBaseDao().delete(entity);
    }

    @Override
    public List<T> findAllByIds(Iterable<ID> ids) {
        return getBaseDao().findAllById(ids);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        // TODO Auto-generated method stub
        return getBaseDao().findAll(spec, pageable);
    }

}
