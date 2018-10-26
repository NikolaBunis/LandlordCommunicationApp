package com.landlordcommunication.web.repositories;

import com.landlordcommunication.web.models.Residence;
import com.landlordcommunication.web.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SqlResidenceRepository implements ResidenceRepository {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public List<Residence> getResidencesByUser(int userId) {

        User user = getUserById(userId);

        return user.getResidences();
    }

    private User getUserById(int id) {
        User result;

        try (
                Session session = sessionFactory.openSession()
        ) {
            session.beginTransaction();
            result = session.createQuery("from User where userId = :id", User.class)
                    .setParameter("id", id).uniqueResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
}
