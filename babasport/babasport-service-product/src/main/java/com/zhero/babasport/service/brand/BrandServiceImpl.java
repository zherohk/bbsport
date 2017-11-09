package com.zhero.babasport.service.brand;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhero.babasport.mapper.product.BrandMapper;
import com.zhero.babasport.pojo.product.Brand;
import com.zhero.babasport.pojo.product.BrandQuery;
import com.zhero.babasport.pojo.product.Pagination;
import com.zhero.babasport.service.brand.BrandService;
/**
 * @description 品牌管理的service实现类
 * @author zhero
 * @date 2017年11月8日
 */
@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Resource
	private BrandMapper brandMapper;
	
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
		//查询当前页数据,及总条数,并创建分页对象
		List<Brand> list = brandMapper.selectBrandListByPage(brandQuery);
		int totalCount = brandMapper.selectBrandCount();
		Pagination pagination = new Pagination(Pagination.cpn(pageNo), brandQuery.getPageSize(), totalCount, list);
		//构建分页工具栏
		String url = "/brand/list.do";
		pagination.pageView(url, params.toString());
		return pagination;
	}

}
