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
    private String taskGroupId;
    private String description;
    //0 là chưa hoàn thành, 1 là đã hoàn thành
    private int isCompleted = 0;
    private String startTime;
    private String endTime;
    // 0 là bình thường, 1 là MyDay, 2 là Important
    private int type = 0;
}