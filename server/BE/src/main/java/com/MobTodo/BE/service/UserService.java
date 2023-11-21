package com.MobTodo.BE.service;

import com.MobTodo.BE.dto.Login;
import com.MobTodo.BE.models.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

@Service
public class UserService implements IUserService {
    private static final String COLLECTION_NAME = "user";

    @Override
    public Boolean logup(User user) throws ExecutionException, InterruptedException {
        if (checkExist(COLLECTION_NAME, "email", user.getEmail())) {
            return false;
        } else {
            if (postData(user, COLLECTION_NAME)) {
                return true;
            }
            return false;
        }
    }

    @Override
    public User getUserDetail(String id) throws ExecutionException, InterruptedException {
        return getDetail(COLLECTION_NAME, id, User.class);
    }

    @Override
    public Boolean login(Login login) {
        System.out.println(login.getEmail());
        if (!checkExist(COLLECTION_NAME, "email", login.getEmail())) {
            System.out.println("Passed 1");
            return false;
        }
        User user = getDetailByFieldName(COLLECTION_NAME, "email", login.getEmail(), User.class);
        if (user == null) {
            System.out.println("Passed 2");
            return false;
        }
        System.out.println("Passed 3");
        System.out.println(user);
        return user.getPassword().equals(login.getPassword());
    }
}
