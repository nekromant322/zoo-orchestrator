package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDTO {

    private Long id;

    private Integer commonRoomPrice;

    private Integer largeRoomPrice;

    private Integer vipRoomPrice;

    private Integer dogPrice;

    private Integer catPrice;

    private Integer reptilePrice;

    private Integer ratPrice;

    private Integer birdPrice;

    private Integer otherPrice;

    private Integer videoPrice;

    private LocalDateTime lastUpdated;
}
