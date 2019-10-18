package com.helloworld;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Student {
    private String id;
    private String name;
    private String email;

    @Override
    public String toString() {
        return "{\"id\":\"" + id +
                "\", \"name\":\"" + name + "\", \"email\":\"" + email + "\"}";
    }
}
