package com.MobTodo.BE.models;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class Timetable {
    private String id;
    private String userId;
    private String dayTime;
}
