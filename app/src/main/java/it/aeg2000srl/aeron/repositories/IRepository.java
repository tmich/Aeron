package it.aeg2000srl.aeron.repositories;

import java.util.List;

import it.aeg2000srl.aeron.core.IOrder;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public interface IRepository<T> {
    T findById(long id);
    long add(T t);
    void edit(T t);
    void remove(T t);
    List<T> getAll();
    long size();
    void addAll(List<T> items);
}
