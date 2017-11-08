package com.zhero.babasport.controller.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhero.babasport.pojo.test.BbsTest;
import com.zhero.babasport.service.test.BbsTestService;

@Controller
public class BbsTestController {

	@Resource
	private BbsTestService bbsTestService;
	
	@RequestMapping("/test.do")
	public String test() throws Exception {
		List<BbsTest> list = bbsTestService.queryBbsTests();
		System.out.println("list size: " +list.size());
		return "test";
	}
	
	
}
