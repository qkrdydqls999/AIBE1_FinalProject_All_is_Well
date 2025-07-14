package org.example.bookmarket.admin.banner.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.admin.banner.entity.Banner;
import org.example.bookmarket.admin.banner.dto.BannerRequest;
import org.example.bookmarket.admin.banner.service.BannerService;
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
        model.addAttribute("bannerForm", new BannerRequest());
        model.addAttribute("showAll", all);
        return "admin/banner";
    }

    @GetMapping("/{id}")
    public String edit(@PathVariable Long id, @RequestParam(name = "all", defaultValue = "false") boolean all, Model model) {
        model.addAttribute("banners", bannerService.getBanners(all));
        Banner banner = bannerService.getBanner(id);
        BannerRequest req = new BannerRequest(
                banner.getId(),
                banner.getImageUrl(),
                banner.getLink(),
                banner.getTitle(),
                banner.getSortOrder(),
                banner.getStatus()
        );
        model.addAttribute("bannerForm", req);
        model.addAttribute("showAll", all);
        return "admin/banner";
    }

    @PostMapping
        public String create(@ModelAttribute BannerRequest banner, @RequestParam(name = "all", defaultValue = "false") boolean all) {
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