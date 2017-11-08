package com.zhero.babasport.mapper.test;

import java.util.List;

import com.zhero.babasport.pojo.test.BbsTest;

public interface BbsTestMapper {

	public void insertBbsTest(BbsTest bbsTest) throws Exception;
	
	public List<BbsTest> queryBbsTests() throws Exception;
}
