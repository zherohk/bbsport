package babasport;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhero.babasport.mapper.test.BbsTestMapper;
import com.zhero.babasport.pojo.test.BbsTest;
import com.zhero.babasport.service.test.BbsTestService;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSM {

	@Resource
	private BbsTestMapper bbsTestMapper;
	
	@Resource
	private BbsTestService bbsTestService;
	
	/**
	 * 测试mybatis
	 * @throws Exception 
	 */
	@Test
	public void testMybatis() throws Exception {
		BbsTest bbsTest = new BbsTest();
		bbsTest.setName("张小凡");
		bbsTest.setBirthday(new Date());
		bbsTestMapper.insertBbsTest(bbsTest);
	}
	
	@Test
	public void testSpring() throws Exception {
		BbsTest bbsTest = new BbsTest();
		bbsTest.setName("萧炎2");
		bbsTest.setBirthday(new Date());
		bbsTestService.insertBbsTest(bbsTest);
	}
}
