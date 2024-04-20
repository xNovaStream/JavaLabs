package ru.itmo.dao.impl;

import lombok.NonNull;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.itmo.dao.ICatDao;
import ru.itmo.entity.CatEntity;

import java.util.List;
import java.util.UUID;

public class CatDao implements ICatDao {
    private final SessionFactory sessionFactory;

    public CatDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CatEntity findById(@NonNull UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(CatEntity.class, id);
        }
    }

    @Override
    public List<CatEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from CatEntity", CatEntity.class).list();
        }
    }

    @Override
    public void save(@NonNull CatEntity cat) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(cat);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(@NonNull CatEntity cat) {
        updateAll(cat);
    }

    @Override
    public void updateAll(@NonNull CatEntity... cats) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (CatEntity cat : cats) {
                session.merge(cat);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(@NonNull UUID id) {
        try (Session session = sessionFactory.openSession()) {
            CatEntity cat = session.getReference(CatEntity.class, id);
            if (cat != null) {
                session.beginTransaction();
                session.remove(cat);
                session.getTransaction().commit();
            }
        }
    }

    @Override
    public List<CatEntity> getFriendIds(@NonNull UUID id) {
        try (Session session = sessionFactory.openSession()) {
            List<CatEntity> friends = session.getReference(CatEntity.class, id).getFriends();
            Hibernate.initialize(friends);
            return friends;
        }
    }
}
