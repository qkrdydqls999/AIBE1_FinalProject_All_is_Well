package org.example.bookmarket.usedbook.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;
import org.example.bookmarket.usedbook.service.UsedBookPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/used-books")
@RequiredArgsConstructor
public class UsedBookPostController {

//    private final UsedBookPostService usedBookPostService;

    @PostMapping
    public ResponseEntity<Void> postUsedBook(@ModelAttribute UsedBookPostRequest request) {
//        usedBookPostService.registerUsedBook(request);
        return ResponseEntity.ok().build();
    }
}
