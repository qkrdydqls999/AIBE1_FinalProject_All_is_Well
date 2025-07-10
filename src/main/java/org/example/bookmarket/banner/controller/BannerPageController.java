package org.example.bookmarket.banner.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.banner.entity.Banner;
import org.example.bookmarket.banner.service.BannerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/banners")
public class BannerPageController {
    private final BannerService bannerService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("banners", bannerService.getBanners());
        model.addAttribute("bannerForm", Banner.builder().build());
        return "banner/manager";
    }

    @PostMapping
    public String create(@ModelAttribute Banner banner) {
        bannerService.saveBanner(banner);
        return "redirect:/admin/banners";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return "redirect:/admin/banners";
    }
}