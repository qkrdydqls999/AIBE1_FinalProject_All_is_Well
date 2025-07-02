package org.example.bookmarket.wishlist.service;

import org.example.bookmarket.wishlist.dto.WishlistItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WishlistService {

    private final List<WishlistItem> items = new ArrayList<>();

    public List<WishlistItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(WishlistItem item) {
        if (item != null) {
            items.add(item);
        }
    }

    public void removeItem(Long usedBookId) {
        items.removeIf(i -> i.usedBookId().equals(usedBookId));
    }
}