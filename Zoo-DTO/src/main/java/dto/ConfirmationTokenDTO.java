package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationTokenDTO {
    private long id;
    private String token;
    private String email;
    private LocalDate expiredDate;
}
