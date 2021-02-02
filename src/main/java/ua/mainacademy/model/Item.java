package ua.mainacademy.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Integer id;
    private String itemCode;
    private String name;
    private Integer price;
    private Integer initPrice;
}
