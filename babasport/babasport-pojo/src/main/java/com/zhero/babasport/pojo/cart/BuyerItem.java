package com.zhero.babasport.pojo.cart;

import java.io.Serializable;

import com.zhero.babasport.pojo.product.Sku;

/**
 * @description 购物项
 * @author zhero
 * @date 2017年11月22日
 */
@SuppressWarnings("serial")
public class BuyerItem implements Serializable {

	private Sku sku; // 商品信息
	private Boolean isHave = true; // 是否有货
	private Integer amount = 1; // 购买数量

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Boolean getIsHave() {
		return isHave;
	}

	public void setIsHave(Boolean isHave) {
		this.isHave = isHave;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyerItem other = (BuyerItem) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.getId().equals(other.sku.getId()))//判断是否是同款商品,判断ID
			return false;
		return true;
	}
	
}
