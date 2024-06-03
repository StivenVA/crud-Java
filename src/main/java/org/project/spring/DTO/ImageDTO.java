package org.project.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImageDTO {

    private LocalDate creationDate;
    private byte[] image;
    private LocalTime creationTime;
}
