package com.hcs.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradePostDto {

    @NotBlank
    @Length(min = 5, max = 30)
    private String title;

    @NotBlank
    private String category;

    @NotBlank
    private String productStatus;

    @NotBlank
    private String description;

    @Lob
    private byte[] pictures;

    @NotBlank
    @Range(min = 1_000, max = 1_000_000, message = "1,000원 이상 1,000,000원 이하의 범위까지 가능합니다.")
    private Integer price;

    @NotBlank
    @Pattern(regexp = "^010[-](\\d{4})[-](\\d{4})$")
    private String phoneNumber;

}
