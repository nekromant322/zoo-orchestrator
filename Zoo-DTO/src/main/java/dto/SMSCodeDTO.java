package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SMSCodeDTO {

    private long id;
    private Integer code;
    private String phoneNumber;
    private LocalDateTime expiredDate;

    public SMSCodeDTO(Integer code, String phoneNumber) {
        this.code = code;
        this.phoneNumber = phoneNumber;
    }
}
