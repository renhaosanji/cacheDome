package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.cache.CacheManager;
import com.example.demo.dao.CategoryDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.vo.CategoryVO;
import com.example.demo.vo.ProductVO;

@Service
public class ProductServiceImpl implements ProductService {

	
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private ProductService productService;
	
	@Override
	public List<ProductVO> selectProductNo(ProductVO productVO) {
		// TODO Auto-generated method stub
		return productDAO.selectProductNo(productVO);
	}

	@Override
	public List<CategoryVO> selectCategory() {
		// TODO Auto-generated method stub
		return categoryDAO.selectCategory();
	}

	@Override
	public List<ProductVO> selectAllProductInfo() {
		// TODO Auto-generated method stub
		return productDAO.selectAllProductInfo();
	}

	@Override
	public ProductVO selectProductInfo(ProductVO productVO) {
		// TODO Auto-generated method stub
		return productDAO.selectProductInfo(productVO);
	}

	@Override
	public int insertCategory(CategoryVO categoryVO) {
		// TODO Auto-generated method stub
		int count = categoryDAO.insertCategory(categoryVO);
		CacheManager.clearOnly("cg"); // insert 성공 경우에 캐시 지우기
		return count;
	}

	@Override
	public int updateCategory(CategoryVO categoryVO) {
		// TODO Auto-generated method stub
		int count = categoryDAO.updateCategory(categoryVO);
		CacheManager.clearOnly("cg");// update 성공 경우에 캐시 지우기
		return count;
	}

	@Override
	public int insertProduct(ProductVO productVO) {
		// TODO Auto-generated method stub		
		int count = productDAO.insertProduct(productVO);
		// 상품 정보 추가할때 상품 카테고리 소속정보 지우기
		CacheManager.clearOnly("c_"+productVO.getCategoryNo());
		return count;
	}

	@Override
	public int updateProduct(ProductVO productVO) {
		// TODO Auto-generated method stub
		int count = productDAO.updateProduct(productVO);
		// 상품 정보 업데이트시 해당된 상품 캐이시 지우기 
		CacheManager.clearOnly("p_"+productVO.getProductNo());
		return count;
	}

}
