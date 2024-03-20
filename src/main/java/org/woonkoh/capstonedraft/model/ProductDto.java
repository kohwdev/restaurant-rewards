package org.woonkoh.capstonedraft.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;


@Getter
@Setter
public class ProductDto {
    @NotEmpty(message = "Product name cannot be empty")
    private String name;
    @NotEmpty(message = "Category cannot be empty")
    private String category;
    @Min(0)
    private double price;

    @Size(min = 10, message = "The description should be at least 10 characters long")
    @Size(max = 2000, message = "The description should be less than 2000 characters long")
    private String description;

    private MultipartFile imageFile;

}
