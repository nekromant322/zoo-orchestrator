package com.nekromant.zoo.service;

import com.cko.zoo.dto.AnimalRequestDTO;
import com.nekromant.zoo.dao.PriceDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.cko.zoo.enums.RoomType.*;
import static java.lang.Math.abs;

@Service
public class PriceService {

    @Autowired
    private PriceDAO priceDAO;

    @Autowired
    private PriceService priceService;

    /**
     * Получить актуальную цену
     * @return {@link Price}
     */
    public Price getActualPrice() {
        return priceDAO.findTopByOrderByLastUpdatedDesc();
    }

    /**
     * Просчитать стоимость заявки
     * @param animalRequestDTO заявка
     * @return Полная стоимость заявки
     */
    public int calculateTotalPrice(AnimalRequestDTO animalRequestDTO) {

        LocalDate begin = animalRequestDTO.getBeginDate();
        LocalDate end = animalRequestDTO.getEndDate();
        int difference = daysBetween(begin,end);
        int price = 0;
        if (animalRequestDTO.getRoomType() == COMMON) {
            price = difference * priceService.getActualPrice().getCommonRoomPrice();
        }
        if (animalRequestDTO.getRoomType() == LARGE) {
            price = difference * priceService.getActualPrice().getLargeRoomPrice();
        }
        if (animalRequestDTO.getRoomType() == VIP) {
            price = difference * priceService.getActualPrice().getVipRoomPrice();
        }
        if (animalRequestDTO.getVideoNeeded()) {
            price += priceService.getActualPrice().getVideoPrice();
        }
        return price;
    }

    /**
     * Добавить новый прайс
     * @param price - цены
     */
    public void insert(Price price) {
        price.setLastUpdated(LocalDateTime.now());
        priceDAO.save(price);
    }

    /**
     * Рассчитать количество дней между двумя датами
     * @param begin - первая дата
     * @param end - вторая дата
     */
    private int daysBetween(LocalDate begin, LocalDate end) {
        return (int) (abs(end.toEpochDay() - begin.toEpochDay()) + 1);
    }
}


