package com.example.slshopping_ut_answer.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.slshopping_ut_answer.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * カテゴリー情報検索クエリ
     *
     * @param name カテゴリー名
     * @return カテゴリー情報
     */
    public Category findByName(String name);

    /**
     * カテゴリー情報検索クエリ
     *
     * @param keyword 検索キーワード
     * @return カテゴリー情報のリスト
     */
    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
    public List<Category> search(String keyword);

}
