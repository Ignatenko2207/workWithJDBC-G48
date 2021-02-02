package ua.mainacademy.service;

import ua.mainacademy.dao.ItemDAO;
import ua.mainacademy.model.Item;

import java.util.List;

public class ItemService {

    List<Item> findPayedItemsByUserAndPeriod(Integer userId, Long dateFrom, Long dateTo) {
        return ItemDAO.findPayedItemsByUserAndPeriod(userId, dateFrom, dateTo);
    }

}
