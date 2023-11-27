package com.MobTodo.BE.models;

import lombok.*;

import java.util.List;

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
    private List<Events> events;
}
