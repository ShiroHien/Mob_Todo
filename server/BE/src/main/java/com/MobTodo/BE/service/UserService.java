package com.MobTodo.BE.service;

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
            if(postData( user, COLLECTION_NAME )) {
                return true;
            }
            return false;
        }
    }
    @Override
    public User getUserDetail(String id) throws ExecutionException, InterruptedException {
        return getDetail(COLLECTION_NAME, id, User.class);
    }
}
