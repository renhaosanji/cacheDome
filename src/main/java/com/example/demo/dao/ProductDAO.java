package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.vo.ProductVO;
@Mapper
public interface ProductDAO {
	
	
	public List<ProductVO> selectProductNo(ProductVO productVO);
	public List<ProductVO> selectAllProductInfo();
	public ProductVO selectProductInfo(ProductVO productVO);
	public int insertProduct(ProductVO productVO);
	public int updateProduct(ProductVO productVO);

}
