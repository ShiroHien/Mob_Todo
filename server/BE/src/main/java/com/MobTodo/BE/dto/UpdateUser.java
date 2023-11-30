package com.MobTodo.BE.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class UpdateUser {
    private String username;
    private String name;
}
