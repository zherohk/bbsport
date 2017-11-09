package com.zhero.babasport.service.brand;

import java.util.List;

import com.zhero.babasport.pojo.product.Brand;
import com.zhero.babasport.pojo.product.Pagination;

/**
 * @description 品牌管理的service接口
 * @author zhero
 * @date 2017年11月8日
 */
public interface BrandService {

	/**
	 * 品牌列表查询(不分页)
	 * @param brandQuery
	 * @return
	 * @throws Exception
	 */
	public List<Brand> selectBrandListNoPage(String name, Integer isDisplay) throws Exception;
	
	/**
	 * 查询列表分页
	 * @param name
	 * @param isDisplay
	 * @param pageNo
	 * @return
	 * @throws Exception
	 */
	public Pagination selectBrandListByPage(String name, Integer isDisplay, Integer pageNo) throws Exception;
	
}
