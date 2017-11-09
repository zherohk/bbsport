package com.zhero.babasport.controller.center;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description 控制台中心页面的相互跳转
 * @author zhero
 * @date 2017年11月8日
 */
@Controller
@RequestMapping("/center")
public class CenterController {

	/**
	 * 跳转后台主页
	 * @return
	 */
	@RequestMapping("/index.do")
	public String index() {
		return "index";
	}
	
	/**
	 * 跳转后台top页
	 * @return
	 */
	@RequestMapping("/top.do")
	public String top() {
		return "top";
	}
	
	/**
	 * 跳转后台main页
	 * @return
	 */
	@RequestMapping("/main.do")
	public String main() {
		return "main";
	}
	
	/**
	 * 跳转后台left页
	 * @return
	 */
	@RequestMapping("/left.do")
	public String left() {
		return "left";
	}
	
	/**
	 * 跳转后台right页
	 * @return
	 */
	@RequestMapping("/right.do")
	public String right() {
		return "right";
	}
}
