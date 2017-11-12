package com.zhero.babasport.service.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhero.babasport.mapper.test.BbsTestMapper;
import com.zhero.babasport.pojo.test.BbsTest;
import com.zhero.babasport.service.test.BbsTestService;
@Service("bbsTestService")
public class BbsTestServiceImpl implements BbsTestService {

	@Resource
	private BbsTestMapper bbsTestMapper;
	
	@Transactional
	@Override
	public void insertBbsTest(BbsTest bbsTest) throws Exception {
		bbsTestMapper.insertBbsTest(bbsTest);
		//System.out.println(1/0);
	}

	@Override
	public List<BbsTest> queryBbsTests() throws Exception {
		return bbsTestMapper.queryBbsTests();
	}

}
