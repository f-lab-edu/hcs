package com.hcs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubDto {

    @NotBlank
    @Length(max = 50)
    @Pattern(regexp = "/(^\\s*)|(\\s*$)/gi, \"\"")
    private String title;

    @Length(max = 50)
    private String description;

    @NotNull
    private LocalDateTime createdAt;

    @NotBlank
    private String location;

    @NotBlank
    private String category;

}
