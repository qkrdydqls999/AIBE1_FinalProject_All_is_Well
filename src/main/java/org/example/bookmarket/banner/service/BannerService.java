package org.example.bookmarket.banner.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.banner.entity.Banner;
import org.example.bookmarket.banner.entity.BannerStatus;
import org.example.bookmarket.banner.repository.BannerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;

    @Transactional(readOnly = true)
    public List<Banner> getBanners(boolean all) {
        if (all) {
            return bannerRepository.findAllByOrderBySortOrderAsc();
        }
        return bannerRepository.findAllByStatusOrderBySortOrderAsc(BannerStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Banner> getBanners() {
        return getBanners(false);
    }

    @Transactional
    public void saveBanner(Banner banner) {
        bannerRepository.save(banner);
    }

    @Transactional
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    @Transactional
    public void toggleBannerStatus(Long id) {
        Banner banner = getBanner(id);
        if (banner.getStatus() == BannerStatus.ACTIVE) {
            banner.changeStatus(BannerStatus.INACTIVE);
        } else {
            banner.changeStatus(BannerStatus.ACTIVE);
        }
    }

    @Transactional(readOnly = true)
    public Banner getBanner(Long id) {
        return bannerRepository.findById(id).orElseThrow();
    }
}