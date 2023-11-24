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
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private boolean completed = false;
    private boolean myDay = false;
    private boolean important = false;
}