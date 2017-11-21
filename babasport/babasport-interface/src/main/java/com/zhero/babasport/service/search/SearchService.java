package com.zhero.babasport.service.search;

import java.util.List;

import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Brand;

/**
 * @description 前台检索业务层接口
 * @author zhero
 * @date 2017年11月13日
 */
public interface SearchService {

	/**
	 * 根据关键字进行检索
	 * @param keyword
	 * @param pageNo
	 * @return
	 */
	public Pagination selectProductsForPortal(String keyword, Long branId, String price, Integer pageNo) throws Exception;
	
	/**
	 * 检索页面获取品牌信息
	 * @return
	 */
	public List<Brand> selectBrandsFromRedis();
	
	/**
	 * 从Redis中获取品牌名称
	 * @param brandId
	 * @return
	 */
	public String selectBrandNameByIdFromRedis(Long brandId);
	
	/**
	 * 将商品信息保存到索引库
	 * @param id
	 */
	public void insertProductToSolr(Long id) throws Exception;
}
