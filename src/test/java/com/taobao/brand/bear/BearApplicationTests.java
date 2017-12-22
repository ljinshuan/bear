package com.taobao.brand.bear;

import com.taobao.brand.bear.service.DogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BearApplicationTests {


	@Resource
	private DogService dogService;

	@Test
	public void contextLoads() {
	}

}
