package it.aeg2000srl.aeron.repositories;

import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.core.User;
import it.aeg2000srl.aeron.entities.EUser;

/**
 * Created by tiziano.michelessi on 13/10/2015.
 */
public class UserRepository implements IRepository<User> {

    protected User toBusinessObject(EUser entity) {
        User u = new User();
        u.setId(entity.getId());
        u.setUsername(entity.username);
        u.setCode(entity.code);
        return u;
    }

    @Override
    public User findById(long id) {
        User user = null;
        EUser entity = EUser.findById(EUser.class, id);
        if (entity != null) {
            user = toBusinessObject(entity);
        }
        return user;
    }

    @Override
    public long add(User user) {
        // solo un utente attivo alla volta
        if(EUser.count(EUser.class, null, null) == 0) {
            EUser entity = new EUser();
            entity.username = user.getUsername();
            entity.code = user.getCode();
            entity.save();
            return entity.getId();
        }
        return 0;
    }

    @Override
    public void edit(User user) {
        EUser entity = EUser.findById(EUser.class, user.getId());
        if (entity != null) {
            entity.username = user.getUsername();
            entity.code = user.getCode();
            entity.save();
        }
    }

    @Override
    public void remove(User user) {
        EUser entity = EUser.findById(EUser.class, user.getId());
        if (entity != null) {
            entity.delete();
        }
    }

    @Override
    public List<User> getAll() {
        List<EUser> entities = EUser.listAll(EUser.class);
        List<User> users = new ArrayList<>(entities.size());

        for (EUser entity : entities) {
            users.add(toBusinessObject(entity));
        }

        return users;
    }

    @Override
    public long size() {
        return EUser.count(EUser.class, null, null);
    }

    @Override
    public void addAll(List<User> items) {

    }
}
