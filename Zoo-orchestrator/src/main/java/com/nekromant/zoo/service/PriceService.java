package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.PriceDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Price;
import dto.AnimalRequestDTO;
import enums.AnimalType;
import enums.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static enums.AnimalType.*;
import static enums.RoomType.*;
import static java.lang.Math.abs;

@Service
public class PriceService {

    @Autowired
    private PriceDAO priceDAO;


    /**
     * Получить актуальную цену
     *
     * @return {@link Price}
     */
    public Price getActualPrice() {
        return priceDAO.findTopByOrderByLastUpdatedDesc();
    }


    private Map<AnimalType, Integer> getAnimalTypePriceMap(Price price) {
        Map<AnimalType, Integer> animalTypePrice = new HashMap<>();

        animalTypePrice.put(CAT, price.getCatPrice());
        animalTypePrice.put(DOG, price.getDogPrice());
        animalTypePrice.put(REPTILE, price.getReptilePrice());
        animalTypePrice.put(RAT, price.getRatPrice());
        animalTypePrice.put(BIRD,price.getBirdPrice());
        animalTypePrice.put(OTHER, price.getOtherPrice());
        return animalTypePrice;
    }

    private Map<RoomType, Integer> getRoomTypePriceMap(Price price) {
        Map<RoomType, Integer> roomTypePrice = new HashMap<>();
        roomTypePrice.put(LARGE, price.getLargeRoomPrice());
        roomTypePrice.put(VIP, price.getVipRoomPrice());
        roomTypePrice.put(COMMON, price.getCommonRoomPrice());
        return roomTypePrice;
    }


    private int getAnimalTypePrice(AnimalRequestDTO animalRequestDTO, Price price) {
        LocalDate begin = animalRequestDTO.getBeginDate();
        LocalDate end = animalRequestDTO.getEndDate();
        int difference = daysBetween(begin, end);
        int sum = 0;
        sum += difference * getAnimalTypePriceMap(price).get(animalRequestDTO.getAnimalType());
        return sum;
    }


    private int getRoomTypePrice(AnimalRequestDTO animalRequestDTO,Price price) {
        LocalDate begin = animalRequestDTO.getBeginDate();
        LocalDate end = animalRequestDTO.getEndDate();
        int difference = daysBetween(begin, end);
        int sum = 0;
        sum += difference * getRoomTypePriceMap(price).get(animalRequestDTO.getRoomType());
        return sum;
    }

    /**
     * Просчитать стоимость заявки, учитывая вид животного и тип комнаты
     *
//     * @param animalRequestDTO заявка
     * @return Полная стоимость заявки
     */
    public int calculateTotalPrice(AnimalRequestDTO animalRequestDTO) {
        Price actualPrice = getActualPrice();
        LocalDate begin = animalRequestDTO.getBeginDate();
        LocalDate end = animalRequestDTO.getEndDate();
        int difference = daysBetween(begin, end);
        int sum = getAnimalTypePrice(animalRequestDTO,actualPrice) + getRoomTypePrice(animalRequestDTO,actualPrice);
        if (animalRequestDTO.getVideoNeeded()) {
            sum += difference *getActualPrice().getVideoPrice();
        }
        return sum;
    }

    /**
     * Добавить новый прайс
     *
     * @param price - цены
     */
    public void insert(Price price) {
        price.setLastUpdated(LocalDateTime.now());
        priceDAO.save(price);
    }

    /**
     * Рассчитать количество дней между двумя датами
     *
     * @param begin - первая дата
     * @param end   - вторая дата
     */
    private int daysBetween(LocalDate begin, LocalDate end) {
        return (int) (abs(end.toEpochDay() - begin.toEpochDay()) + 1);
    }
}


