package com.zhero.babasport.service.buyer;

import com.zhero.babasport.pojo.cart.BuyerCart;
import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.pojo.user.Buyer;

/**
 * @description 用户管理
 * @author zhero
 * @date 2017年11月19日
 */
public interface BuyerService {

	/**
	 * 通过用户名获取用户信息
	 * @param username
	 * @return
	 */
	public Buyer selectBuyerByUserName(String username);
	
	/**
	 * 根据主键获取库存信息
	 * @param id
	 * @return
	 */
	public Sku selectSkuById(Long id);
	
	/**
	 * 将购物车保存到Redis中
	 * @param username
	 * @param buyerCart
	 */
	public void insertBuyerCartToRedis(String username, BuyerCart buyerCart);
	
	/**
	 * 从Redis中取出购物车
	 * @param username
	 * @return
	 */
	public BuyerCart getBuyerCartFromRedis(String username);
}
