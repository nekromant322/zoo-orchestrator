package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceDAO extends CrudRepository<Price, Long> {
    /**
     * Найти запись о цене с самым большим lastUpdated
     * @return {@link Price} актуальная цена
     */
    Price findTopByOrderByLastUpdatedDesc();
}
