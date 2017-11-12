package com.zhero.babasport.service.product;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhero.babasport.mapper.product.ProductMapper;
import com.zhero.babasport.mapper.product.SkuMapper;
import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Product;
import com.zhero.babasport.pojo.product.ProductQuery;
import com.zhero.babasport.pojo.product.ProductQuery.Criteria;
import com.zhero.babasport.pojo.product.Sku;

import redis.clients.jedis.Jedis;
/**
 * @description 商品管理的业务层实现类
 * @author zhero
 * @date 2017年11月12日
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Resource
	private ProductMapper productMapper;
	
	@Resource
	private SkuMapper skuMapper;
	
	@Resource
	private Jedis jedis;
	
	@Override
	public Pagination selectProductListByPage(String name, Long brandId, Boolean isShow, Integer pageNo)
			throws Exception {
		//封装查询条件到productQuery对象,并拼接分页工具需要的参数.
		ProductQuery productQuery = new ProductQuery();
		Criteria criteria = productQuery.createCriteria();
		StringBuilder ss = new StringBuilder();
		if (null != name && !"".equals(name)) {
			criteria.andNameLike("%" + name + "%");//不会带百分号,要手动添加.
			ss.append("name=").append(name);
		}
		if (null != brandId) {
			criteria.andBrandIdEqualTo(brandId);
			ss.append("&brandId=").append(brandId);
		}
		if (null != isShow) {
			criteria.andIsShowEqualTo(isShow);
			ss.append("&isShow=").append(isShow);
		} else {//默认查询下架
			criteria.andIsShowEqualTo(false);
			ss.append("&isShow=").append(false);
		}
		//设置页码,每页显示条数
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(5);
		//按照id降序
		productQuery.setOrderByClause("id desc");
		//根据条件查询
		List<Product> list = productMapper.selectByExample(productQuery);
		int totalCount = productMapper.countByExample(productQuery);
		//填充数据
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), totalCount, list);
		//url,构建分页工具栏
		String url = "/product/list.do";
		pagination.pageView(url, ss.toString());
		return pagination;
	}

	/*
	 * (non-Javadoc)
	 * @description 添加商品信息
	 */
	@Override
	public void insertProduct(Product product) throws Exception {
		//保存商品
		//redis生成商品id
		Long id = jedis.incr("pno");
		product.setId(id);
		product.setIsShow(false);//默认下架
		product.setCreateTime(new Date());
		productMapper.insertSelective(product);
		//初始化此商品对应的库存信息
		if (null != product.getColors() && null != product.getSizes()) {
			String[] colors = product.getColors().split(",");
			String[] sizes = product.getSizes().split(",");
			for (String size : sizes) {
				for (String color : colors) {
					Sku sku = new Sku();
					sku.setProductId(id);
					sku.setColorId(Long.parseLong(color));
					sku.setSize(size);
					sku.setMarketPrice(0f);//市场价
					sku.setPrice(0f);//售价
					sku.setDeliveFee(0f);//运费
					sku.setStock(0);//库存
					sku.setUpperLimit(0);//购买限制
					sku.setCreateTime(new Date());
					skuMapper.insertSelective(sku);
				}
			}
		}
	}
}
