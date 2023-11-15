package com.MobTodo.BE.models;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class Task {
    private String id;
    private String TaskGroupId;
    private String description;
    // 0 là bình thường, 1 là MyDay, 2 là Important
    private int type;
    private boolean isCompleted;
    private String startTime;
    private String endTime;
}