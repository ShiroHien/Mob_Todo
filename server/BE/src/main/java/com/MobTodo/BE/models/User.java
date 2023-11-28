package com.MobTodo.BE.models;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String ava = "";
}
