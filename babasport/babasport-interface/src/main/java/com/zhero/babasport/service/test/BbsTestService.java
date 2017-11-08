package com.zhero.babasport.service.test;

import java.util.List;

import com.zhero.babasport.pojo.test.BbsTest;

public interface BbsTestService {

	public void insertBbsTest(BbsTest bbsTest) throws Exception;
	
	public List<BbsTest> queryBbsTests() throws Exception;
}
