package com.HopprBackend.backend.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String userId;
    private String password;

    private String name;
    private String avatar;
}
