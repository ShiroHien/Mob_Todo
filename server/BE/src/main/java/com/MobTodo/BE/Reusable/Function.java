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
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Function {
    // Đối tượng đại diện cho csdl
    private static Firestore dbFirestore = FirestoreClient.getFirestore();

    public static boolean checkExist(String collectionName, String fieldName, String value) {
        try {
            ApiFuture<QuerySnapshot> future = dbFirestore
                    .collection(collectionName)
                    .whereEqualTo(fieldName, value)
                    .get();

            QuerySnapshot querySnapshot = future.get();
            return !querySnapshot.isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkExist(String collectionName, String fieldName1, String value1, String fieldName2, String value2) {
        try {
            ApiFuture<QuerySnapshot> future = dbFirestore
                    .collection(collectionName)
                    .whereEqualTo(fieldName1, value1)
                    .whereEqualTo(fieldName2, value2)
                    .get();

            QuerySnapshot querySnapshot = future.get();
            return !querySnapshot.isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Long distanceDateTime(String start, String end) {
        LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
        long durationInSeconds = Duration.between(startTime, endTime).getSeconds();
        return durationInSeconds;
    }
    public static Long distanceTime(String start, String end) {
        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm:ss"));
        long durationInSeconds = Duration.between(startTime, endTime).getSeconds();
        return durationInSeconds;
    }

    public static boolean checkDateFormat(String input) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        try {
            LocalDate parsedDate = LocalDate.parse(input, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean checkTimeFormat(String input) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            LocalTime parsedTime = LocalTime.parse(input.trim(), timeFormatter);
            return true; // If parsing is successful, the time is valid
        } catch (DateTimeParseException e) {
            System.out.println(e);
            return false; // If parsing fails, the time is not valid
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

    public static <T> T getDetailByFieldName(String collectionName, String fieldName, String value, Class<T> valueType) {
        try {
            // Thực hiện truy vấn
            ApiFuture<QuerySnapshot> future = dbFirestore.collection(collectionName).whereEqualTo(fieldName, value).get();
            QuerySnapshot querySnapshot = future.get();

            // Kiểm tra và chuyển đổi tài liệu đầu tiên
            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot firstDocument = querySnapshot.getDocuments().get(0);
                return firstDocument.toObject(valueType);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getDetailByFieldName(String collectionName, String fieldName1, String value1, String fieldName2, String value2, Class<T> valueType) {
        try {
            // Thực hiện truy vấn
            ApiFuture<QuerySnapshot> future = dbFirestore.collection(collectionName).whereEqualTo(fieldName1, value1).whereEqualTo(fieldName2, value2).get();
            QuerySnapshot querySnapshot = future.get();

            // Kiểm tra và chuyển đổi tài liệu đầu tiên
            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot firstDocument = querySnapshot.getDocuments().get(0);
                return firstDocument.toObject(valueType);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
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
    public static String generateRandomId(String COLLECTION_NAME) {
        DocumentReference documentReference = FirestoreClient.getFirestore().collection(COLLECTION_NAME).document();
        return documentReference.getId();
    }
}