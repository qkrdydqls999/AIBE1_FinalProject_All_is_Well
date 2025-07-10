package org.example.bookmarket.banner.repository;

import org.example.bookmarket.banner.entity.Banner;
import org.example.bookmarket.banner.entity.BannerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByStatusOrderBySortOrderAsc(BannerStatus status);
    List<Banner> findAllByOrderBySortOrderAsc();
}