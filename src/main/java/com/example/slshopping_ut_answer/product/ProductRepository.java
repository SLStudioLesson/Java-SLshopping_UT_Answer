package com.example.slshopping_ut_answer.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.slshopping_ut_answer.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 商品情報検索クエリ
     *
     * @param name 商品名
     * @return 商品情報
     */
    public Product findByName(String name);

    /**
     * 商品情報検索クエリ
     *
     * @param keyword 検索キーワード
     * @return 商品情報のリスト
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% "
            + "OR p.description LIKE %?1% "
            + "OR p.brand.name LIKE %?1% "
            + "OR p.category.name LIKE %?1%")
    public List<Product> search(String keyword);

}
