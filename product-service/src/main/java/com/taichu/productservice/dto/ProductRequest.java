package com.taichu.productservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;

}
