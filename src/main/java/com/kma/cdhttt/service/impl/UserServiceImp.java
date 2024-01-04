package com.kma.cdhttt.service.impl;

import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.model.requestbody.JwtLoginRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImp {
    @PersistenceContext
    private EntityManager entityManager;

    public List<UserEntity> findByEmail(String email) {
        var sql = "SELECT * FROM user WHERE EMAIL='" + email + "'";
        var query = entityManager.createNativeQuery(sql, UserEntity.class);
        return query.getResultList();
    }
}
