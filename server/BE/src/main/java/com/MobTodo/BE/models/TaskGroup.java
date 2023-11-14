package com.MobTodo.BE.models;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class TaskGroup {
    private String id;
    private String title;
    private String userId;
}
