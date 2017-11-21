package com.zhero.babasport.service.brand;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhero.babasport.mapper.product.BrandMapper;
import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Brand;
import com.zhero.babasport.pojo.product.BrandQuery;

import redis.clients.jedis.Jedis;
/**
 * @description 品牌管理的service实现类
 * @author zhero
 * @date 2017年11月8日
 */
@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Resource
	private BrandMapper brandMapper;
	
	@Resource
	private Jedis jedis;
	
	/*
	 * (non-Javadoc)
	 * @description 品牌列表查询(不分页)
	 */
	@Override
	public List<Brand> selectBrandListNoPage(String name, Integer isDisplay) throws Exception {
		BrandQuery brandQuery = new BrandQuery();
		if (null != name && !"".equals(name)) {
			brandQuery.setName(name);
		}
		if (null != isDisplay) {
			brandQuery.setIsDisplay(isDisplay);
		}
		return brandMapper.selectBrandListNoPage(brandQuery);
	}

	/*
	 * (non-Javadoc)
	 * @description 查询列表分页
	 */
	@Override
	public Pagination selectBrandListByPage(String name, Integer isDisplay, Integer pageNo) throws Exception {
		BrandQuery brandQuery = new BrandQuery();
		//拼接分页工具栏需要的参数
		StringBuilder params = new StringBuilder();
		if (null != name && !"".equals(name)) {
			brandQuery.setName(name);
			//url?name=xx
			params.append("name=").append(name);
		}
		if (null != isDisplay) {
			brandQuery.setIsDisplay(isDisplay);
			//url?&isDisplay=xxx
			params.append("&isDisplay=").append(isDisplay);
		}
		//设置当前页,cpn(pageNo):检查页码
		brandQuery.setPageNo(Pagination.cpn(pageNo));
		brandQuery.setPageSize(5);//硬编码
		//查询当前页数据,及总条数,并创建分页对象
		List<Brand> list = brandMapper.selectBrandListByPage(brandQuery);
		int totalCount = brandMapper.selectBrandCount(brandQuery);
		Pagination pagination = new Pagination(brandQuery.getPageNo(), brandQuery.getPageSize(), totalCount, list);
		//构建分页工具栏
		String url = "/brand/list.do";
		pagination.pageView(url, params.toString());
		return pagination;
	}

	/*
	 * (non-Javadoc)
	 * @description 根据id查询指定品牌信息
	 */
	@Override
	public Brand selectBrandById(Long id) throws Exception {
		return brandMapper.selectBrandById(id);
	}

	/*
	 * (non-Javadoc)
	 * @description 更新品牌信息
	 */
	@Override
	public void updateBrand(Brand brand) throws Exception {
		brandMapper.updateBrand(brand);
		//将品牌信息保存到Redis中
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
	}

	/*
	 * (non-Javadoc)
	 * @description 添加品牌信息
	 */
	@Override
	public void saveBrand(Brand brand) throws Exception {
		brandMapper.saveBrand(brand);
	}

	/*
	 * (non-Javadoc)
	 * @description 批量删除品牌信息
	 */
	@Override
	public void deleteBatchBrand(Long[] ids) throws Exception {
		brandMapper.deleteBatchBrand(ids);
	}

	/*
	 * (non-Javadoc)
	 * @description 删除单个品牌信息
	 */
	@Override
	public void deleteById(Long id) throws Exception {
		brandMapper.deleteById(id);
	}

}
