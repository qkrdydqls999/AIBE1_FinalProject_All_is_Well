package org.example.bookmarket.usedbook.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;
import org.example.bookmarket.usedbook.dto.UsedBookResponse;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.usedbook.service.UsedBookPostService;
import org.springframework.stereotype.Controller; // @RestController에서 변경
import org.springframework.web.bind.annotation.*;

@Controller

@RequestMapping("/used-books")
@RequiredArgsConstructor
public class UsedBookPostController {

    @PostMapping
    public String postUsedBook(@ModelAttribute UsedBookPostRequest request) {

        System.out.println("판매 등록 요청 받음: " + request);

        return "redirect:/";
    }
}