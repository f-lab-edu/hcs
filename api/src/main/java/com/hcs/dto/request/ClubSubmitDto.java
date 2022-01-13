package com.hcs.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubSubmitDto {

    @NotBlank
    @Length(min = 3, max = 50)
    private String title;
    @Length(max = 50)
    private String description;
    @NotNull
    private LocalDateTime createdAt;
    @NotBlank
    private String location;
    @NotBlank
    private String category;

    public void setTitle(String title) {
        this.title = title.trim();
    }
}
