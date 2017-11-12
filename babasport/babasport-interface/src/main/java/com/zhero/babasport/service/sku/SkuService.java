package com.zhero.babasport.service.sku;

import java.util.List;

import com.zhero.babasport.pojo.product.Sku;
/**
 * @description 库存管理的业务层接口
 * @author zhero
 * @date 2017年11月13日
 */
public interface SkuService {

	/**
	 * 查询此商品对应的库存信息
	 * @param productId
	 * @return
	 */
	public List<Sku> selectSkusByProductId(Long productId);
	
	/**
	 * 更新库存信息
	 * @param sku
	 * @throws Exception
	 */
	public void updateSku(Sku sku) throws Exception;
}
