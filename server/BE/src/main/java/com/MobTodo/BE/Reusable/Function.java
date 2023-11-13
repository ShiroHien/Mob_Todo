package com.MobTodo.BE.Reusable;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;

public class Function {
    // Đối tượng đại diện cho csdl
    private static Firestore dbFirestore = FirestoreClient.getFirestore();
    public static boolean checkExist(String collectionName, String document) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(collectionName).document(document);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot result = future.get();
        if(result.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
