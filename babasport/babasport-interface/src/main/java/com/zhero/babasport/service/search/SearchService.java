package com.zhero.babasport.service.search;

import com.zhero.babasport.pojo.page.Pagination;

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
	public Pagination selectProductsForPortal(String keyword, Integer pageNo) throws Exception;
}
