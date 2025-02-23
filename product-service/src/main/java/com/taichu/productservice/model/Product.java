package com.taichu.productservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true, length = 500)
    private String name;
    @Column(length = 500)
    private String description;
    private BigDecimal price;

}
