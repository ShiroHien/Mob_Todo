package com.MobTodo.BE.Reusable;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public static Long distanceTime(String start, String end) {
        LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
        long durationInSeconds = Duration.between(startTime, endTime).getSeconds();
        return durationInSeconds;
    }
    public static boolean checkDateFormat(String input) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        try {
            LocalDate parsedDate = LocalDate.parse(input, dateFormatter);
            return true; // If parsing is successful, the date is valid
        } catch (DateTimeParseException e) {
            return false; // If parsing fails, the date is not valid
        }
    }
    public static boolean checkTimeFormat(String input) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            LocalTime parsedDate = LocalTime.parse(input, dateFormatter);
            return true; // If parsing is successful, the date is valid
        } catch (DateTimeParseException e) {
            return false; // If parsing fails, the date is not valid
        }
    }
    public static boolean checkDateTimeFormat(String input) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        try {
            LocalDateTime parsedDate = LocalDateTime.parse(input, dateFormatter);
            return true; // If parsing is successful, the date is valid
        } catch (DateTimeParseException e) {
            return false; // If parsing fails, the date is not valid
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
            updateFuture.get();

            return true;
        } catch (InterruptedException | ExecutionException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Boolean deleteData(String collectionName, String value, String fieldName) {
        try {
            Query query = dbFirestore.collection(collectionName).whereEqualTo(fieldName, value);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            if (documents.isEmpty()) {
                return true;
            }
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
            }
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

}