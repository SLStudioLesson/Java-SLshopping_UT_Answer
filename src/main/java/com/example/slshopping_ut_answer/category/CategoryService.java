package com.example.slshopping_ut_answer.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.slshopping_ut_answer.entity.Category;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * カテゴリー情報全件取得
     *
     * @return カテゴリー情報のリスト
     */
    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    /**
     * カテゴリー情報検索処理
     *
     * @param keyword 検索キーワード
     * @return カテゴリー情報のリスト
     */
    public List<Category> listAll(String keyword) {
        // 検索キーワードがあった場合
        if (keyword != null && !keyword.isEmpty()) {
            return categoryRepository.search(keyword);
        }
        // それ以外の場合
        else {
            return categoryRepository.findAll();
        }
    }

    /**
     * IDに紐づくカテゴリー情報取得処理
     *
     * @param id カテゴリーID
     * @return カテゴリー情報
     * @throws NotFoundException
     */
    public Category get(Long id) throws NotFoundException {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    /**
     * カテゴリー情報登録処理
     *
     * @param category 保存したいカテゴリー情報
     * @return 保存したカテゴリー情報
     */
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * カテゴリー名の重複チェック
     *
     * @param name 重複確認したいブランド情報
     * @return true:重複なし false:重複あり
     */
    public boolean checkUnique(Category category) {
        boolean isCreatingNew = (category.getId() == null || category.getId() == 0);
        Category categoryByName = categoryRepository.findByName(category.getName());

        if (isCreatingNew) {
            if (categoryByName != null) {
                return false;
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != category.getId()) {
                return false;
            }
        }
        return true;
    }

    /**
     * IDに紐づくカテゴリー情報削除処理
     *
     * @param id カテゴリーID
     * @throws NotFoundException
     */
    public void delete(Long id) throws NotFoundException {
        // IDに紐づくブランド情報が存在するか確認するため、getメソッドを呼び出す
        Category category = get(id);
        categoryRepository.deleteById(category.getId());
    }

}
