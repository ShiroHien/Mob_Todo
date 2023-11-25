package com.MobTodo.BE.models;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class TaskDay {
    private String id;
    private String timetableId;
    private String title;
    private String description;
    private String startTime;
    private String endTime;
}
