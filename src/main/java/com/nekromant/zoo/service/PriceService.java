package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.PriceDAO;
import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

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
    public int calculateTotalPrice(AnimalRequest animalRequest) {

        LocalDate begin = animalRequest.getBeginDate();
        LocalDate end = animalRequest.getEndDate();
        int difference = daysBetween(begin, end);
        int price = 0;

        if (animalRequest.getAnimalType() == CAT){
            price = difference * priceService.getActualPrice().getCatPrice();
        }

        if (animalRequest.getAnimalType() == DOG){
            price = difference * priceService.getActualPrice().getDogPrice();
        }

        if (animalRequest.getAnimalType() == REPTILE){
            price = difference * priceService.getActualPrice().getReptilePrice();
        }

        if (animalRequest.getAnimalType() == RAT){
            price = difference * priceService.getActualPrice().getRatPrice();
        }

        if (animalRequest.getAnimalType() == BIRD){
            price = difference * priceService.getActualPrice().getBirdPrice();
        }

        if (animalRequest.getAnimalType() == OTHER){
            price = difference * priceService.getActualPrice().getOtherPrice();
        }

        if (animalRequest.getRoomType() == LARGE) {
            price+= difference * priceService.getActualPrice().getLargeRoomPrice();
        }
        if (animalRequest.getRoomType() == VIP) {
            price += difference * priceService.getActualPrice().getVipRoomPrice();
        }
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


