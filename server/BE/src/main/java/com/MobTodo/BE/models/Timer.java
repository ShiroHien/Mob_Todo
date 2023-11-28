package com.MobTodo.BE.models;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class Timer {
    private String id;
    private String userId;
    private double duringTime;
    private String day;
}
