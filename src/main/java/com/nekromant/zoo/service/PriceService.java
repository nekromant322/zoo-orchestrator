package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.PriceDAO;
import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

import static com.nekromant.zoo.enums.AnimalType.*;
import static com.nekromant.zoo.enums.RoomType.*;
import static java.lang.Math.abs;

@Service
public class PriceService {

    @Autowired
    private PriceDAO priceDAO;

    @Autowired
    private PriceService priceService;

    /**
     * Получить актуальную цену
     *
     * @return {@link Price}
     */
    public Price getActualPrice() {
        return priceDAO.findTopByOrderByLastUpdatedDesc();
    }

    /**
     * Просчитать стоимость заявки
     *
     * @param animalRequest заявка
     * @return Полная стоимость заявки
     */
    public int getAnimalTypePrice(AnimalRequest animalRequest) {
        LocalDate begin = animalRequest.getBeginDate();
        LocalDate end = animalRequest.getEndDate();
        int difference = daysBetween(begin, end);
        int price = 0;
        Map<AnimalType, Integer> animalTypePrice = new HashMap<>();
        animalTypePrice.put(CAT, priceService.getActualPrice().getCatPrice());
        animalTypePrice.put(DOG, priceService.getActualPrice().getDogPrice());
        animalTypePrice.put(REPTILE, priceService.getActualPrice().getReptilePrice());
        animalTypePrice.put(RAT, priceService.getActualPrice().getRatPrice());
        animalTypePrice.put(BIRD, priceService.getActualPrice().getBirdPrice());
        animalTypePrice.put(OTHER, priceService.getActualPrice().getOtherPrice());

        price += difference * animalTypePrice.get(animalRequest.getAnimalType());
        return price;
    }


    public int getRoomTypePrice(AnimalRequest animalRequest) {
        LocalDate begin = animalRequest.getBeginDate();
        LocalDate end = animalRequest.getEndDate();
        int difference = daysBetween(begin, end);
        int price = 0;

        Map<RoomType, Integer> roomTypePrice = new HashMap<>();
        roomTypePrice.put(LARGE, priceService.getActualPrice().getLargeRoomPrice());
        roomTypePrice.put(VIP, priceService.getActualPrice().getVipRoomPrice());
        roomTypePrice.put(COMMON, priceService.getActualPrice().getCommonRoomPrice());
        price += difference * roomTypePrice.get(animalRequest.getRoomType());
        return price;
    }

    public int calculateTotalPrice(AnimalRequest animalRequest) {
        int price = getAnimalTypePrice(animalRequest)+getRoomTypePrice(animalRequest);
        if (animalRequest.getVideoNeeded()) {
            price += priceService.getActualPrice().getVideoPrice();
        }
        return price;
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


