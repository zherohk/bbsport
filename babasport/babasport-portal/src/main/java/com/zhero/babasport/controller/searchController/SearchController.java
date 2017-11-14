package com.zhero.babasport.controller.searchController;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.service.search.SearchService;

/**
 * @description 前台检索
 * @author zhero
 * @date 2017年11月13日
 */
@Controller
public class SearchController {

	@Resource
	private SearchService searchService;
	
	/**
	 * 系统首页
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	/**
	 * 根据关键字查询商品
	 * @param keyword
	 * @param pageNo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/Search")
	public String search(String keyword, Integer pageNo, Model model) throws Exception {
		Pagination pagination = searchService.selectProductsForPortal(keyword, pageNo);
		model.addAttribute("pagination", pagination);
		return "search";
	}
}
