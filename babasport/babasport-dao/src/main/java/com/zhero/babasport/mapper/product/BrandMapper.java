package com.zhero.babasport.mapper.product;

import java.util.List;

import com.zhero.babasport.pojo.product.Brand;
import com.zhero.babasport.pojo.product.BrandQuery;

/**
 * @description 品牌管理的mapper接口
 * @author zhero
 * @date 2017年11月8日
 */
public interface BrandMapper {

	/**
	 * 品牌列表查询不分页
	 * @param brandQuery
	 * @return
	 * @throws Exception
	 */
	public List<Brand> selectBrandListNoPage(BrandQuery brandQuery) throws Exception;
	
	/**
	 * 查询列表分页数据
	 * @param brandQuery
	 * @return
	 * @throws Exception
	 */
	public List<Brand> selectBrandListByPage(BrandQuery brandQuery) throws Exception;
	
	/**
	 * 查询总条数
	 * @return
	 * @throws Exception
	 */
	public int selectBrandCount() throws Exception;
}
