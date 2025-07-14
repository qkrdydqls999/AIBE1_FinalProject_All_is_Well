package org.example.bookmarket.admin.banner.repository;

import org.example.bookmarket.admin.banner.entity.Banner;
import org.example.bookmarket.admin.banner.entity.BannerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByStatusOrderBySortOrderAsc(BannerStatus status);
    List<Banner> findAllByOrderBySortOrderAsc();
}