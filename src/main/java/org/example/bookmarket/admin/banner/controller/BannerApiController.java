package org.example.bookmarket.admin.banner.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.admin.banner.entity.Banner;
import org.example.bookmarket.admin.banner.service.BannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerApiController {
    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<List<Banner>> banners() {
        return ResponseEntity.ok(bannerService.getBanners());
    }
}