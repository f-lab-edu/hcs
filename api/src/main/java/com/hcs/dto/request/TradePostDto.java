package com.hcs.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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

    @Length(min = 5, max = 100)
    private String locationName;

    @Min(-180)
    @Max(180)
    private double lng;

    @Min(-90)
    @Max(90)
    private double lat;

    @NotBlank
    @Range(min = 1_000, max = 1_000_000, message = "1,000원 이상 1,000,000원 이하의 범위까지 가능합니다.")
    private Integer price;
}
