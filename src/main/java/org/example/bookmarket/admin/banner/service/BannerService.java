package org.example.bookmarket.admin.banner.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.admin.banner.dto.BannerRequest;
import org.example.bookmarket.admin.banner.entity.Banner;
import org.example.bookmarket.admin.banner.entity.BannerStatus;
import org.example.bookmarket.admin.banner.repository.BannerRepository;
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
        public void saveBanner(BannerRequest request) {
            Banner banner;
            if (request.id() != null) {
                banner = getBanner(request.id());
            } else {
                banner = Banner.builder().build();
            }

            banner.setImageUrl(request.imageUrl());
            banner.setLink(request.link());
            banner.setTitle(request.title());
            banner.setSortOrder(request.sortOrder());
            banner.setStatus(request.status() != null ? request.status() : BannerStatus.ACTIVE);

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