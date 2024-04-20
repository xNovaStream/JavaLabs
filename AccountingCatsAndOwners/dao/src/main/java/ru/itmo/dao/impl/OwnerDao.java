package ru.itmo.dao.impl;

import lombok.NonNull;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.itmo.dao.IOwnerDao;
import ru.itmo.entity.CatEntity;
import ru.itmo.entity.OwnerEntity;

import java.util.List;
import java.util.UUID;

public class OwnerDao implements IOwnerDao {
    private final SessionFactory sessionFactory;

    public OwnerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public OwnerEntity getById(@NonNull UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(OwnerEntity.class, id);
        }
    }

    @Override
    public List<OwnerEntity> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from OwnerEntity", OwnerEntity.class).list();
        }
    }

    @Override
    public void save(@NonNull OwnerEntity owner) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(owner);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(@NonNull OwnerEntity owner) {
        updateAll(owner);
    }

    @Override
    public void updateAll(@NonNull OwnerEntity... owners) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (OwnerEntity owner : owners) {
                session.merge(owner);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(@NonNull UUID id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(session.getReference(OwnerEntity.class, id));
            session.getTransaction().commit();
        }
    }

    @Override
    public List<CatEntity> getCats(@NonNull UUID id) {
        try (Session session = sessionFactory.openSession()) {
            OwnerEntity owner = session.get(OwnerEntity.class, id);
            if (owner == null) return null;
            Hibernate.initialize(owner.getCats());
            return owner.getCats();
        }
    }
}
