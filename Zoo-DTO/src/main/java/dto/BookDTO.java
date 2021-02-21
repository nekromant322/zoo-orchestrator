package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private long id;

    private long animalRequestId;

    private long roomId;

    private LocalDate beginDate;

    private LocalDate endDate;
}
