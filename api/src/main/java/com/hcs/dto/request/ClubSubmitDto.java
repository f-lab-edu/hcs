package com.hcs.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubSubmitDto {

    @NotBlank
    @Length(min = 3, max = 30)
    private String title;
    @Length(max = 50)
    private String description;
    @NotBlank
    @Length(max = 20)
    private String location;
    @NotBlank
    private String category;

    public void setTitle(String title) {
        this.title = title.trim();
    }
}
