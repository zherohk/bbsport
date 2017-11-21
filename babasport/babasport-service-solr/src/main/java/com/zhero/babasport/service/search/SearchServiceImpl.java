package com.zhero.babasport.service.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import com.zhero.babasport.mapper.product.ProductMapper;
import com.zhero.babasport.mapper.product.SkuMapper;
import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Brand;
import com.zhero.babasport.pojo.product.Product;
import com.zhero.babasport.pojo.product.ProductQuery;
import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.pojo.product.SkuQuery;

import redis.clients.jedis.Jedis;

/**
 * @description 前台检索的业务层实现类
 * @author zhero
 * @date 2017年11月13日
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Resource
	private SolrServer solrServer;

	@Resource
	private Jedis jedis;
	
	@Resource
	private ProductMapper productMapper;
	
	@Resource
	private SkuMapper skuMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @description 根据关键字检索商品
	 */
	@Override
	public Pagination selectProductsForPortal(String keyword, Long brandId, String price, Integer pageNo)
			throws Exception {
		// 创建solrQuery对象,封装检索条件,封装分页参数
		SolrQuery solrQuery = new SolrQuery();
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(8);
		StringBuilder params = new StringBuilder();
		// 1、根据关键字检索；
		if (null != keyword && !"".equals(keyword)) {
			solrQuery.setQuery("name_ik:" + keyword);
			params.append("keyword=").append(keyword);
		}
		// 2、关键字高亮；
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("name_ik");
		solrQuery.setHighlightSimplePre("<font color='red'>");
		solrQuery.setHighlightSimplePost("</font>");
		// 3、根据价格排序；
		solrQuery.setSort("price", ORDER.asc);
		// 4、结果分页
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
		// 设置过滤条件
		if (null != brandId) {
			solrQuery.addFilterQuery("brandId:" + brandId);
			params.append("&brandId=").append(brandId);
		}
		if (null != price && !"".equals(price)) {
			String[] split = price.split("-");
			if (split.length == 2) {
				solrQuery.addFilterQuery("price:[" + split[0] + " TO " + split[1] + "]");
			} else {
				solrQuery.addFilterQuery("price:[" + price + " TO *");
			}
			params.append("&price=").append(price);
		}

		// 查询
		QueryResponse response = solrServer.query(solrQuery);
		SolrDocumentList results = response.getResults();
		// 总条数
		int totalCount = (int) results.getNumFound();
		// 获得高亮结果集
		Map<String, Map<String, List<String>>> hhh = response.getHighlighting();
		// 处理结果
		List<Product> list = new ArrayList<Product>();
		for (SolrDocument solrDocument : results) {
			Product product = new Product();
			String id = solrDocument.get("id").toString();
			String imgUrl = solrDocument.get("url").toString();
			String pri = solrDocument.get("price").toString();
			String bId = solrDocument.get("brandId").toString();
			List<String> names = hhh.get(id).get("name_ik");
			if (null != names && names.size() > 0) {
				product.setName(names.get(0));
			} else {
				product.setName(solrDocument.get("name_ik").toString());
			}
			product.setId(Long.parseLong(id));
			product.setPrice(pri);
			product.setBrandId(Long.parseLong(bId));
			product.setImgUrl(imgUrl);
			list.add(product);
		}
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), totalCount, list);
		pagination.pageView("/Search", params.toString());
		return pagination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @description 检索页面获取品牌信息
	 */
	@Override
	public List<Brand> selectBrandsFromRedis() {
		Map<String, String> map = jedis.hgetAll("brand");
		Set<Entry<String, String>> entrySet = map.entrySet();
		List<Brand> list = new ArrayList<Brand>();
		for (Entry<String, String> entry : entrySet) {
			Brand brand = new Brand();
			brand.setId(Long.parseLong(entry.getKey()));
			brand.setName(entry.getValue());
			list.add(brand);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @description 从Redis中查询品牌名称
	 */
	@Override
	public String selectBrandNameByIdFromRedis(Long brandId) {
		return jedis.hget("brand", String.valueOf(brandId));
	}

	/*
	 * (non-Javadoc)
	 * @description 将商品信息保存到索引库
	 */
	@Override
	public void insertProductToSolr(Long id) throws Exception {
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		Product nPro = productMapper.selectByPrimaryKey(id);
		solrInputDocument.addField("id", id);//商品id
		solrInputDocument.addField("name_ik", nPro.getName());//商品名称
		solrInputDocument.addField("url", nPro.getImgUrl());//商品图片
		solrInputDocument.addField("brandId", nPro.getBrandId());//商品品牌
		//商品价格需要展示最低价:select price from bbs_sku where product_id=? order by price asc limit 0,1
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.setFields("price");//设置查询的字段
		skuQuery.createCriteria().andProductIdEqualTo(id).andStockGreaterThan(0);
		skuQuery.setOrderByClause("price asc");
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);
		List<Sku> skus = skuMapper.selectByExample(skuQuery);
		solrInputDocument.addField("price", skus.get(0).getPrice());
		solrServer.add(solrInputDocument);
		solrServer.commit();
	}
}
