package com.MobTodo.BE.Reusable;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Function {
    // Đối tượng đại diện cho csdl
    private static Firestore dbFirestore = FirestoreClient.getFirestore();

    public static boolean checkExist(String collectionName, String fieldName, String value) {
        try {
            ApiFuture<QuerySnapshot> future = dbFirestore.collection(collectionName).whereEqualTo(fieldName, value).get();
            QuerySnapshot querySnapshot = future.get();
            return !querySnapshot.isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception as needed
            e.printStackTrace();
            return false;
        }
    }

    public static <T> T getDetail(String collectionName, String document, Class<T> valueType) {
        try {
            DocumentReference documentReference = dbFirestore.collection(collectionName).document(document);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot result = future.get();
            return result.toObject(valueType);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> boolean postData(T data, String collection_name) throws ExecutionException, InterruptedException {
        CollectionReference collection = FirestoreClient.getFirestore().collection(collection_name);
        ApiFuture<DocumentReference> documentReferenceApiFuture = collection.add(data);
        DocumentReference documentReference = documentReferenceApiFuture.get();
        String documentId = documentReference.getId();
        try {
            data.getClass().getMethod("setId", String.class).invoke(data, documentId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        ApiFuture<WriteResult> updateFuture = documentReference.set(data, SetOptions.merge());
        return true;
    }

    public static <T> List<T> getListData(String collectionName, String fieldName, String id, Class<T> classType) {
        List<T> resultList = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = dbFirestore.collection(collectionName).whereEqualTo(fieldName, id).get();
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                T data = document.toObject(classType);
                resultList.add(data);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static <T> Boolean updateData(String collectionName, String document, T data) {
        try {
            DocumentReference documentReference = dbFirestore.collection(collectionName).document(document);
            ApiFuture<DocumentSnapshot> documentSnapshotFuture = documentReference.get();
            DocumentSnapshot documentSnapshot = documentSnapshotFuture.get();
            if (!documentSnapshot.exists()) {
                System.out.println("Document does not exist. Update failed.");
                return false;
            }
            Map<String, Object> fieldUpdates = new HashMap<>();
            for (Field field : data.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(data);
                fieldUpdates.put(fieldName, value);
            }

            // Update the document with the new data
            ApiFuture<WriteResult> updateFuture = documentReference.update(fieldUpdates);
            updateFuture.get(); // Block and wait for the update to complete

            return true;
        } catch (InterruptedException | ExecutionException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

}