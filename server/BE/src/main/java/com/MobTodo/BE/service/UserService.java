package com.MobTodo.BE.service;

import com.MobTodo.BE.models.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.checkExist;

@Service
public class UserService {
    private static final String COLLECTION_NAME = "user";
    // Đối tượng đại diện cho csdl
    private Firestore dbFirestore = FirestoreClient.getFirestore();
    public String logup(User user) throws ExecutionException, InterruptedException {
        if(checkExist(COLLECTION_NAME, user.getEmail())) {
            return "Email đã tồn tại. Đăng ký không thành công";
        } else {
            // Set dữ liệu vào Firestore
            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(user.getEmail()).set(user);
            // trả về thời điểm cập nhập document
            return collectionApiFuture.get().getUpdateTime().toString();
        }
    }
    public User getUserDetail(String email) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(email);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        User user = null;
        if(document.exists()) {
            user = document.toObject(User.class);
            return user;
        } else {
            return null;
        }
    }
}
