package org.example.bookmarket.banner.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.banner.entity.Banner;
import org.example.bookmarket.banner.entity.BannerStatus;
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
    public String list(@RequestParam(name = "all", defaultValue = "false") boolean all, Model model) {
        model.addAttribute("banners", bannerService.getBanners(all));
        model.addAttribute("bannerForm", Banner.builder().build());
        model.addAttribute("showAll", all);
        return "banner/manager";
    }

    @GetMapping("/{id}")
    public String edit(@PathVariable Long id, @RequestParam(name = "all", defaultValue = "false") boolean all, Model model) {
        model.addAttribute("banners", bannerService.getBanners(all));
        model.addAttribute("bannerForm", bannerService.getBanner(id));
        model.addAttribute("showAll", all);
        return "banner/manager";
    }

    @PostMapping
    public String create(@ModelAttribute Banner banner, @RequestParam(name = "all", defaultValue = "false") boolean all) {
        bannerService.saveBanner(banner);
        return "redirect:/admin/banners" + (all ? "?all=true" : "");
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam(name = "all", defaultValue = "false") boolean all) {
        bannerService.deleteBanner(id);
        return "redirect:/admin/banners" + (all ? "?all=true" : "");
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, @RequestParam(name = "all", defaultValue = "false") boolean all) {
        bannerService.toggleBannerStatus(id);
        return "redirect:/admin/banners" + (all ? "?all=true" : "");
    }
}