package com.zhero.babasport.service.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Product;
import com.zhero.babasport.pojo.product.ProductQuery;
/**
 * @description 前台检索的业务层实现类
 * @author zhero
 * @date 2017年11月13日
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {
	
	@Resource
	private SolrServer solrServer;

	/*
	 * (non-Javadoc)
	 * @description 根据关键字检索商品
	 */
	@Override
	public Pagination selectProductsForPortal(String keyword, Integer pageNo) throws Exception {
		//创建solrQuery对象,封装检索条件,封装分页参数
		SolrQuery solrQuery = new SolrQuery();
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(8);
		StringBuilder params = new StringBuilder();
		//1、根据关键字检索；
		if (null != keyword && !"".equals(keyword)) {
			solrQuery.setQuery("name_ik:" + keyword);
			params.append("keyword=").append(keyword);
		}
		//2、关键字高亮；
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("name_ik");
		solrQuery.setHighlightSimplePre("<font color='red'>");
		solrQuery.setHighlightSimplePost("</font>");
		//3、根据价格排序；
		solrQuery.setSort("price", ORDER.asc);
		//4、结果分页
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
		//查询
		QueryResponse response = solrServer.query(solrQuery);
		SolrDocumentList results = response.getResults();
		//总条数
		int totalCount = (int) results.getNumFound();
		//获得高亮结果集
		Map<String, Map<String, List<String>>> hhh = response.getHighlighting();
		//处理结果
		List<Product> list = new ArrayList<Product>();
		for (SolrDocument solrDocument : results) {
			Product product = new Product();
			String id = solrDocument.get("id").toString();
			String imgUrl = solrDocument.get("url").toString();
			String price= solrDocument.get("price").toString();
			String brandId = solrDocument.get("brandId").toString();
			List<String> names = hhh.get(id).get("name_ik");
			if (null != names && names.size() > 0) {
				product.setName(names.get(0));
			} else {
				product.setName(solrDocument.get("name_ik").toString());
			}
			product.setId(Long.parseLong(id));
			product.setPrice(price);
			product.setBrandId(Long.parseLong(brandId));
			product.setImgUrl(imgUrl);
			list.add(product);
		}
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), totalCount, list);
		pagination.pageView("/Search", params.toString());
		return pagination;
	}

}
