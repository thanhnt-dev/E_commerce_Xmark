package com.thanhnt.storeservice.domain.entity.shopinfoasset;

import com.thanhnt.storeservice.domain.entity.common.BaseEntity;
import com.thanhnt.storeservice.domain.entity.shop.Shop;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shop_info_assets")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopInfoAssets extends BaseEntity implements Serializable {
  @Column(name = "media_key", nullable = false)
  private String mediaKey;

  @Column(name = "public_id", nullable = false)
  private String publicId;

  @ManyToOne
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;
}
