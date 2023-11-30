package com.MobTodo.BE.service;

import com.MobTodo.BE.dto.Login;
import com.MobTodo.BE.dto.UpdateUser;
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
    public User logup(User user) throws ExecutionException, InterruptedException {
        if (checkExist(COLLECTION_NAME, "email", user.getEmail())) {
            return null;
        } else {
            user.setAva(randomPic());
            if (postData(user, COLLECTION_NAME)) {
                User result = getDetailByFieldName(COLLECTION_NAME, "email", user.getEmail(), User.class);
                return result;
            }
            return null;
        }
    }

    @Override
    public User getUserDetail(String id) throws ExecutionException, InterruptedException {
        return getDetail(COLLECTION_NAME, id, User.class);
    }

    @Override
    public User login(Login login) {
        if (!checkExist(COLLECTION_NAME, "email", login.getEmail())) {
            return null;
        }
        User user = getDetailByFieldName(COLLECTION_NAME, "email", login.getEmail(), User.class);
        if (user == null) {
            return null;
        }
        if (!user.getPassword().equals(login.getPassword())) {
            return null;
        }
        return user;
    }

    private Boolean updateUserFields(User existingUser, UpdateUser data) {
        if (data.getName() != null) {
            existingUser.setName(data.getName());
        }
        if (data.getUsername() != null) {
            existingUser.setUsername(data.getUsername());
        }

        // Update the user in Firestore using the existing updateData method
        updateData(COLLECTION_NAME, existingUser.getId(), existingUser);
        return true;
    }

    @Override
    public Boolean updateUser(UpdateUser data, String userId) {
        User existingUser = getDetail(COLLECTION_NAME, userId, User.class);

        if (existingUser != null) {
            return updateUserFields(existingUser, data);
        } else {
            return false;
        }
    }

}
