package org.example.bookmarket.usedbook.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.usedbook.service.UsedBookPostService;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/used-books")
@RequiredArgsConstructor
public class UsedBookPostController {

    private final UsedBookPostService usedBookPostService;

    @PostMapping
    public String postUsedBook(@ModelAttribute UsedBookPostRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        User user = (User) authentication.getPrincipal();
        UsedBookPostRequest withSeller = new UsedBookPostRequest(
                request.isbn(),
                request.title(),
                request.author(),
                request.publisher(),
                request.publicationYear(),
                request.conditionGrade(),
                request.hasWriting(),
                request.hasStains(),
                request.hasTears(),
                request.hasWaterDamage(),
                request.likeNew(),
                request.detailedCondition(),
                request.sellingPrice(),
                request.categoryId(),
                user.getId(),
                request.images()
        );

        UsedBookSummary summary = usedBookPostService.registerUsedBook(withSeller);
        return "redirect:/used-books/" + summary.usedBookId();
    }
}