package ua.mainacademy.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Integer id;
    private Integer itemId;
    private Integer cartId;
    private Integer amount;
}
