package ua.mainacademy.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Integer id;
    private Integer userId;
    private Long creationTime;
    private Status status;

    public enum Status {
        OPEN,
        CLOSED
    }
}
