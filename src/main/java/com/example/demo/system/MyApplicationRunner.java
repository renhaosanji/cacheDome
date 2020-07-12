package com.example.demo.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.demo.cache.Cache;
import com.example.demo.cache.CacheManager;
import com.example.demo.dao.CategoryDAO;
import com.example.demo.restfulController.RestfulController;
import com.example.demo.service.ProductService;
import com.example.demo.vo.CategoryVO;
import com.example.demo.vo.ProductVO;
@Component
public class MyApplicationRunner implements ApplicationRunner {

	@Autowired
	private ProductService productService;
	
	@Value("${cache.config.expiredTime}")
	private int expiredTime;
	
	private static final Logger LOG = LoggerFactory.getLogger(RestfulController.class);
	
	/**
	 * 시스템 시동할 때 일차적으로 db에서 있는 정보를 가져와서 캐시에 로딩
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		
		LOG.info("category  정보 cache 로딩");
		List<CategoryVO> categoryVOList = productService.selectCategory();
		//Cache c = new Cache();
		//c.setValue(categoryVOList);
	    //CacheManager.putCache("cg", c);
	   CacheManager.putCacheInfo("cg", categoryVOList,expiredTime*1000, false);
	    
	    
	    LOG.info(" Product category 속소 정보 cache 로딩");
	    for (CategoryVO categoryVO : categoryVOList) {
			
	    	ProductVO parmPro = new ProductVO();
		    parmPro.setCategoryNo(categoryVO.getCategoryNo());
			List<ProductVO> productVOList =  productService.selectProductNo(parmPro);
			
//			Cache prodctVOCache = new Cache();			
//			prodctVOCache.setValue(productVOList);
			//CacheManager.putCache("c_"+categoryVO.getCategoryNo(), prodctVOCache);
			CacheManager.putCacheInfo("c_"+categoryVO.getCategoryNo(), productVOList, expiredTime*1000, false);

	    }

	    LOG.info("product  정보 cache 로딩");
	    List<ProductVO> allProductVOList = productService.selectAllProductInfo();
	    
	    for (ProductVO productVO : allProductVOList) {
//			
//	    	Cache allProdctVOCache = new Cache();
//	    	allProdctVOCache.setValue(productVO);
	    	CacheManager.putCacheInfo("p_"+productVO.getProductNo(), productVO,expiredTime*1000, false);
	    }
	      
	      
	      
	}

}
