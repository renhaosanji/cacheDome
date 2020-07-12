package com.example.demo.restfulController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.cache.Cache;
import com.example.demo.cache.CacheManager;
import com.example.demo.dao.CategoryDAO;
import com.example.demo.service.ProductService;
import com.example.demo.vo.CategoryVO;
import com.example.demo.vo.ProductVO;

@RestController
@RequestMapping(value = "/api")
public class RestfulController {
	
	private static final Logger LOG = LoggerFactory.getLogger(RestfulController.class);
	
	@Autowired
	private ProductService productService;
	@Value("${cache.config.expiredTime}")
	private int expiredTime;
	

	/**  
	 * @Title: getAllCategory
	 * @Description: TODO(설명) 모든 카테고리 조회
	 * @return
	 * @author renhs
	 * @date 2020-07-12 03:37:00 
	 */  
	@GetMapping("/getAllCategory")
	public List<CategoryVO> getAllCategory() {
		
		try {
			LOG.info("cache로 접근 시도");  
			return (List<CategoryVO>) CacheManager.getCacheInfo("cg").getValue();
		} catch (NullPointerException e) {
			// TODO: handle exception
			LOG.info("cache 없음, db 접근 시작");
			List<CategoryVO> categoryVOs = productService.selectCategory();			
			if (categoryVOs.size()>0) {
//				Cache c = new Cache();
//			    c.setValue(categoryVOs);
			    CacheManager.putCacheInfo("cg", categoryVOs,expiredTime*1000, false);								
			}
			return categoryVOs;
		}
	}
	
	/**  
	 * @Title: getProductNo
	 * @Description: TODO(설명) 선택된 카태고리를 소속된 상품번호 조회
	 * @param categoryNo
	 * @return
	 * @author renhs
	 * @date 2020-07-12 03:37:30 
	 */  
	@GetMapping("/getProductNo")
	public List<ProductVO> getProductNo(@RequestParam(value = "categoryNo",required = true)String categoryNo) {
		
		try {
			LOG.info("cache로 접근 시도");  
			return (List<ProductVO>) CacheManager.getCacheInfo("c_"+categoryNo).getValue();
		} catch (NullPointerException e) {
			// TODO: handle exception
			LOG.info("cache 없음, db 접근 시작");
			ProductVO p = new ProductVO();
			p.setCategoryNo(Integer.parseInt(categoryNo));
			List<ProductVO> productVOs = productService.selectProductNo(p);
            if (productVOs.size()>0) {	
            	LOG.info("db 접근 성공, cache에 로딩");
//            	Cache prodctVOCache = new Cache();			
//    			prodctVOCache.setValue(productVOs);
    			CacheManager.putCacheInfo("c_"+categoryNo, productVOs,expiredTime*1000, false);            	           	
			} else {
				LOG.info("db에서도 데이터 없음, null 값 반환");
			}
           return productVOs;
		}

	}
	
	/**  
	 * @Title: getProductInfo
	 * @Description: TODO(설명) 상폼번호를 통해 상품상제 정보 조회
	 * @param productNo 
	 * @return
	 * @author renhs
	 * @date 2020-07-12 03:39:38 
	 */  
	@GetMapping("/getProductInfo")
	public ProductVO getProductInfo(@RequestParam(value = "productNo",required = true)String productNo) {

		try {
			LOG.info("cache로 접근 시도");  
			return (ProductVO) CacheManager.getCacheInfo("p_"+productNo).getValue();
		} catch (NullPointerException e) {
			// TODO: handle exception
			LOG.info("cache 없음, db 접근 시작");
			ProductVO p = new ProductVO();
			p.setProductNo(Integer.parseInt(productNo));
			ProductVO productInfo = productService.selectProductInfo(p);			
			if (productInfo!=null) {
//				Cache prodctVOCache = new Cache();
//		    	prodctVOCache.setValue(productInfo);
		    	CacheManager.putCacheInfo("p_"+productNo, productInfo,expiredTime*1000, false);
			}else {
				LOG.info("db에서도 데이터 없음, null 값 반환");
			}
			return productInfo;
		}
		
	}	
	
	/**  
	 * @Title: addCategory
	 * @Description: TODO(설명) 카테고리 추가
	 * @param categoryVO
	 * @return
	 * @author renhs
	 * @date 2020-07-12 03:40:25 
	 */  
	@PostMapping("/category")
	public int addCategory(@RequestBody CategoryVO categoryVO) {
		return productService.insertCategory(categoryVO);
	}
	
	
	/**  
	 * @Title: updateCategory
	 * @Description: TODO(설명) 카테고리 업데이터
	 * @param categoryVO
	 * @return
	 * @author renhs
	 * @date 2020-07-12 03:40:54 
	 */  
	@PutMapping("/category")
	public int updateCategory(@RequestBody CategoryVO categoryVO) {
		return productService.updateCategory(categoryVO);
	}
	

	/**  
	 * @Title: addProduct
	 * @Description: TODO(설명) 상품추가
	 * @param productVO
	 * @return
	 * @author renhs
	 * @date 2020-07-12 03:41:15 
	 */  
	@PostMapping("/product")
	public int addProduct(@RequestBody ProductVO productVO) {
		return productService.insertProduct(productVO);
	}
	
	
	/**  
	 * @Title: updateProduct
	 * @Description: TODO(설명) 상품정보 업데이트(상품명, 가격)
	 * @param productVO
	 * @return
	 * @author renhs
	 * @date 2020-07-12 03:41:23 
	 */  
	@PutMapping("/product")
	public int updateProduct(@RequestBody ProductVO productVO) {
		return productService.updateProduct(productVO);
	}	
	
	
	
	
	
}
