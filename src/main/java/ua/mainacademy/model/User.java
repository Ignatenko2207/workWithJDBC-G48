package ua.mainacademy.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;

}
