package com.zhero.babasport.service.product;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhero.babasport.mapper.product.ProductMapper;
import com.zhero.babasport.mapper.product.SkuMapper;
import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Product;
import com.zhero.babasport.pojo.product.ProductQuery;
import com.zhero.babasport.pojo.product.ProductQuery.Criteria;
import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.utils.fdfs.FastDFSUtils;

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
	
	@Resource
	private SolrServer solrServer;
	
	@Resource
	private JmsTemplate jmsTemplate;
	
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

	/*
	 * (non-Javadoc)
	 * @description 商品上架
	 */
	@Override
	public void isShow(Long[] ids) throws Exception {
		//1.设置isShow的状态为true,上架
		Product product = new Product();
		product.setIsShow(true);
		if (null != ids && ids.length > 0) {
			for (final Long id : ids) {
				product.setId(id);
				productMapper.updateByPrimaryKeySelective(product);
				//2.保存商品信息到索引库
				//将商品的ID发送到消息队列中.
				jmsTemplate.send(new MessageCreator() {
					
					@Override
					public Message createMessage(Session session) throws JMSException {
						//将商品的ID封装成消息体进行发送
						TextMessage msg = session.createTextMessage(String.valueOf(id));
						return msg;
					}
				});
//				SolrInputDocument solrInputDocument = new SolrInputDocument();
//				Product nPro = productMapper.selectByPrimaryKey(id);
//				solrInputDocument.addField("id", id);//商品id
//				solrInputDocument.addField("name_ik", nPro.getName());//商品名称
//				solrInputDocument.addField("url", nPro.getImgUrl());//商品图片
//				solrInputDocument.addField("brandId", nPro.getBrandId());//商品品牌
//				//商品价格需要展示最低价:select price from bbs_sku where product_id=? order by price asc limit 0,1
//				SkuQuery skuQuery = new SkuQuery();
//				skuQuery.setFields("price");//设置查询的字段
//				skuQuery.createCriteria().andProductIdEqualTo(id);
//				skuQuery.setOrderByClause("price asc");
//				skuQuery.setPageNo(1);
//				skuQuery.setPageSize(1);
//				List<Sku> skus = skuMapper.selectByExample(skuQuery);
//				solrInputDocument.addField("price", skus.get(0).getPrice());
//				solrServer.add(solrInputDocument);
//				solrServer.commit();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @description 批量删除商品
	 */
	@SuppressWarnings("unused")
	@Transactional
	@Override
	public void deleteBatchProduct(Long[] ids) throws Exception {
		if (null != ids && ids.length > 0) {
			for (Long id : ids) {
				//需要删除附件信息
				Product product = productMapper.selectByPrimaryKey(id);
				String description = product.getDescription();
				//TODO:描述里面的图片没删.
				FastDFSUtils.delPic(product.getImgUrl());
				//删除
				productMapper.deleteByPrimaryKey(id);
			}
		}
	}
}
