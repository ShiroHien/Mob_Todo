package com.MobTodo.BE.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class Login {
    private String email;
    private String password;
}
