package com.zhero.babasport.controller.frame;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description 商品tab下页面的跳转
 * @author zhero
 * @date 2017年11月8日
 */
@Controller
@RequestMapping("/frame")
public class FrameController {

	/**
	 * 跳转商品tab下的页面
	 * @return
	 */
	@RequestMapping("/product_main.do")
	public String product_main() {
		return "frame/product_main";
	}
	
	/**
	 * 跳转商品tab下的product_left页面
	 * @return
	 */
	@RequestMapping("/product_left.do")
	public String product_left() {
		return "frame/product_left";
	}
}
