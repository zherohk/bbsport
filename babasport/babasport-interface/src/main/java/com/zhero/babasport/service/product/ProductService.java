package com.zhero.babasport.service.product;

import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Product;

/**
 * @description 商品管理的业务层接口
 * @author zhero
 * @date 2017年11月12日
 */
public interface ProductService {

	/**
	 * 商品列表查询(分页)
	 * @param name
	 * @param brandId
	 * @param isShow
	 * @param pageNo
	 * @return
	 * @throws Exception
	 */
	public Pagination selectProductListByPage(String name, Long brandId, Boolean isShow, Integer pageNo) throws Exception;

	/**
	 * 添加商品信息
	 * @param product
	 * @throws Exception
	 */
	public void insertProduct(Product product) throws Exception;
}
