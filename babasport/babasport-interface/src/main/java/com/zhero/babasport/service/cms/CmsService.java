package com.zhero.babasport.service.cms;

import java.util.List;

import com.zhero.babasport.pojo.product.Product;
import com.zhero.babasport.pojo.product.Sku;

public interface CmsService {

	/**
	 * 获取商品信息
	 * @param id
	 * @return
	 */
	public Product selectProductById(Long id);
	
	/**
	 * 获取商品对应的库存信息
	 * @param productId
	 * @return
	 */
	public List<Sku> selectSkusByProductIdAndStockMoreThanZero(Long productId);
}
