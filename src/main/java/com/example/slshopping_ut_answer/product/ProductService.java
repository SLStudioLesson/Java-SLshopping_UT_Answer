package com.example.slshopping_ut_answer.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.slshopping_ut_answer.entity.Product;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 商品情報全件取得
     *
     * @return 商品情報のリスト
     */
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    /**
     * 商品情報検索処理
     *
     * @param keyword 検索キーワード
     * @return 商品情報のリスト
     */
    public List<Product> listAll(String keyword) {
        // 検索キーワードがあった場合
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.search(keyword);
        }
        // それ以外の場合
        else {
            return productRepository.findAll();
        }
    }

    /**
     * IDに紐づく商品情報取得処理
     *
     * @param id 商品ID
     * @return 商品情報
     * @throws NotFoundException
     */
    public Product get(Long id) throws NotFoundException {
        return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    /**
     * 商品情報登録処理
     *
     * @param product 保存したい商品情報
     * @return 保存した商品情報
     */
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * 商品名の重複チェック
     *
     * @param name 重複確認したい商品情報
     * @return true:重複なし false:重複あり
     */
    public boolean checkUnique(Product product) {
        boolean isCreatingNew = (product.getId() == null || product.getId() == 0);
        Product productByName = productRepository.findByName(product.getName());

        if (isCreatingNew) {
            if (productByName != null) {
                return false;
            }
        } else {
            if (productByName != null && productByName.getId() != product.getId()) {
                return false;
            }
        }
        return true;
    }

    /**
     * IDに紐づく商品情報削除処理
     *
     * @param id 商品ID
     * @throws NotFoundException
     */
    public void delete(Long id) throws NotFoundException {
        // IDに紐づく商品情報が存在するか確認するため、getメソッドを呼び出す
        Product product = get(id);
        productRepository.deleteById(product.getId());
    }
}
