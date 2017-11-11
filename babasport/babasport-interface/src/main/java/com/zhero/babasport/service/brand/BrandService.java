package com.zhero.babasport.service.brand;

import java.util.List;

import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Brand;

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
	
	/**
	 * 根据id查询品牌信息
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
	 * 单个删除品牌信息
	 * @param id
	 * @throws Exception
	 */
	public void deleteById(Long id) throws Exception;
}
