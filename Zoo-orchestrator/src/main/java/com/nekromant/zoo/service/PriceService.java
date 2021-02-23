package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.PriceDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Price;
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


    private int getAnimalTypePrice(AnimalRequest animalRequest,Price price) {
        LocalDate begin = animalRequest.getBeginDate();
        LocalDate end = animalRequest.getEndDate();
        int difference = daysBetween(begin, end);
        int sum = 0;
        sum += difference * getAnimalTypePriceMap(price).get(animalRequest.getAnimalType());
        return sum;
    }


    private int getRoomTypePrice(AnimalRequest animalRequest,Price price) {
        LocalDate begin = animalRequest.getBeginDate();
        LocalDate end = animalRequest.getEndDate();
        int difference = daysBetween(begin, end);
        int sum = 0;
        sum += difference * getRoomTypePriceMap(price).get(animalRequest.getRoomType());
        return sum;
    }

    /**
     * Просчитать стоимость заявки, учитывая вид животного и тип комнаты
     *
     * @param animalRequest заявка
     * @return Полная стоимость заявки
     */
    public int calculateTotalPrice(AnimalRequest animalRequest) {
        Price actualPrice = getActualPrice();
        LocalDate begin = animalRequest.getBeginDate();
        LocalDate end = animalRequest.getEndDate();
        int difference = daysBetween(begin, end);
        int sum = getAnimalTypePrice(animalRequest,actualPrice) + getRoomTypePrice(animalRequest,actualPrice);
        if (animalRequest.getVideoNeeded()) {
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

