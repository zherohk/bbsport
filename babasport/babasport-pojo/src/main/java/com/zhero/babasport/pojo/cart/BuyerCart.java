package com.zhero.babasport.pojo.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * @description 购物车
 * @author zhero
 * @date 2017年11月22日
 */
@SuppressWarnings("serial")
public class BuyerCart implements Serializable {

	private List<BuyerItem> buyerItems = new ArrayList<>();		//购物项集

	public List<BuyerItem> getBuyerItems() {
		return buyerItems;
	}

	public void setBuyerItems(List<BuyerItem> buyerItems) {
		this.buyerItems = buyerItems;
	}
	
	/**
	 * 购物项装车
	 * @param buyerItem
	 */
	public void addBuyerItem(BuyerItem buyerItem) {
		if (buyerItems.contains(buyerItem)) {
			for (BuyerItem item : buyerItems) {
				if (item.equals(buyerItem)) {
					item.setAmount(item.getAmount() + buyerItem.getAmount());
				}
			}
		} else {
			buyerItems.add(buyerItem);
		}
	}
	
	//小计部分通过购物项集计算,回显只需要提供get方法.
	//商品金额
	@JsonIgnore
	public Float getProductPrice() {
		Float  productPrice = 0f;
		for (BuyerItem buyerItem : buyerItems) {
			productPrice += buyerItem.getAmount() * buyerItem.getSku().getPrice();
		}
		return productPrice;
	}
	//商品数量
	@JsonIgnore
	public Integer getProductAmount() {
		Integer productAmount = 0;
		for (BuyerItem buyerItem : buyerItems) {
			productAmount += buyerItem.getAmount();
		}
		return productAmount;
	}
	//运费
	@JsonIgnore
	public Float getFee() {
		Float fee = 0f;
		if (getProductPrice() < 99f) {
			fee = 9.9f;
		}
		return fee;
	}
	//总价格
	@JsonIgnore
	public Float getTotalPrice() {
		return getProductPrice() + getFee();
	}
}
