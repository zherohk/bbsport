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
	public int selectBrandCount(BrandQuery brandQuery) throws Exception;
	
	/**
	 * 根据id查询品牌对象
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Brand selectBrandById(Long id) throws Exception;

	/**
	 * 更新品牌信息
	 * @param brand
	 * @throws Exception
	 */
	public void updateBrand(Brand brand) throws Exception;

	/**
	 * 添加品牌信息
	 * @param brand
	 * @throws Exception
	 */
	public void saveBrand(Brand brand) throws Exception;

	/**
	 * 批量删除品牌信息
	 * @param ids
	 * @throws Exception
	 */
	public void deleteBatchBrand(Long[] ids) throws Exception;

	/**
	 * 删除单个品牌
	 * @param id
	 * @throws Exception
	 */
	public void deleteById(Long id) throws Exception;
}
