package dto;

import enums.MailingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MailingMessageDTO {
    private String topic;
    private String text;

    /**
     * С какой даты нужно искать пользователя по последней активности
     */
    private LocalDate dateFrom;

    private MailingType type;
}
