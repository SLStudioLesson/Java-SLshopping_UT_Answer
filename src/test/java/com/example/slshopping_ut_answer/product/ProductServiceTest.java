package com.example.slshopping_ut_answer.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.example.slshopping_ut_answer.entity.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    /** モック化したクラス */
    @Mock
    private ProductRepository mockProductRepository;

    /** テスト対象クラスにモックを注入 */
    @InjectMocks
    private ProductService target;

    /**
     * 【概要】
     * 商品のリストを取得<br>
     *
     * 【条件】
     * productRepositoryのfindAllメソッドはProductのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Productのリストを返却すること
     */
    @Test
    void testListAll() {
        List<Product> expected = Arrays.asList(
            new Product(),
            new Product()
        );

        doReturn(expected).when(this.mockProductRepository).findAll();

        assertThat(target.listAll()).isEqualTo(expected);
    }

    /**
     * 【概要】
     * 商品を検索<br>
     *
     * 【条件】
     * productServiceのlistAllメソッドにnullを渡すこと<br>
     * productRepositoryのfindAllメソッドはProductのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Productのリストを返却すること
     */
    @Test
    void testListAll_argumentIsNull() {
        List<Product> expected = Arrays.asList(
            new Product(),
            new Product()
        );

        doReturn(expected).when(this.mockProductRepository).findAll();

        assertThat(target.listAll(null)).isEqualTo(expected);
    }

    /**
     * 【概要】
     * 商品を検索<br>
     *
     * 【条件】
     * productServiceのlistAllメソッドに空文字を渡すこと<br>
     * productRepositoryのfindAllメソッドはProductのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Productのリストを返却すること
     */
    @Test
    void testListAll_argumentIsEmpty() {
        List<Product> expected = Arrays.asList(
            new Product(),
            new Product()
        );

        doReturn(expected).when(this.mockProductRepository).findAll();

        assertThat(target.listAll("")).isEqualTo(expected);
    }

    /**
     * 【概要】
     * 商品を検索<br>
     *
     * 【条件】
     * productServiceのlistAllメソッドにproductという文字列を渡すこと<br>
     * productRepositoryのsearchメソッドはProductのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Productのリストを返却すること
     */
    @Test
    void testListAll_argumentIsNotEmpty() {
        String keyword = "product";

        List<Product> expected = Arrays.asList(
            new Product(),
            new Product()
        );

        doReturn(expected).when(this.mockProductRepository).search(keyword);

        assertThat(target.listAll(keyword)).isEqualTo(expected);
    }

    /**
     * 【概要】
     * 商品名の重複チェック<br>
     *
     * 【条件】
     * productRepositoryのfindByNameメソッドはnullを返すようスタブ化すること<br>
     *
     * 【結果】
     * trueを返すこと
     */
    @Test
    void testCheckUnique_noDuplication() {
        Product newProduct = new Product();
        newProduct.setName("product");

        doReturn(null).when(this.mockProductRepository).findByName(anyString());

        assertThat(target.checkUnique(newProduct)).isTrue();
    }

    /**
     * 【概要】
     * 商品名の重複チェック<br>
     *
     * 【条件】
     * productRepositoryのfindByNameメソッドはProductのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * falseを返すこと
     */
    @Test
    void testCheckUnique_duplicate() {
        Product newProduct = new Product();
        newProduct.setName("product");

        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("product");

        doReturn(mockProduct).when(this.mockProductRepository).findByName(newProduct.getName());

        assertThat(target.checkUnique(newProduct)).isFalse();
    }

    /**
     * 【概要】
     * 商品情報の取得<br>
     *
     * 【条件】
     * productRepositoryのfindByIdメソッドはProductのインスタンスを格納したOptionalを返却するようスタブ化すること<br>
     *
     * 【結果】
     * 例外が発生しないこと
     */
    @Test
    void testGet_noThrowsException() {
        Long id = 1L;

        Optional<Product> product = Optional.of(new Product());
        doReturn(product).when(this.mockProductRepository).findById(id);

        assertThatCode(() -> {
            target.get(id);
        }).doesNotThrowAnyException();
    }

    /**
     * 【概要】
     * 商品情報の取得<br>
     *
     * 【条件】
     * productRepositoryのfindByIdメソッドはnullを格納したOptionalを返却するようスタブ化すること<br>
     *
     * 【結果】
     * 例外が発生しないこと
     */
    @Test
    void testGet_throwsException() {
        Long id = 1000L;

        Optional<Product> product = Optional.ofNullable(null);

        doReturn(product).when(this.mockProductRepository).findById(id);

        assertThatThrownBy(() -> {
            target.get(id);
        })
        .isInstanceOf(NotFoundException.class);
    }

    /**
     * 【概要】
     * 商品情報の取得処理の検証<br>
     *
     * 【条件】
     * productRepositoryのfindByIdはProductのインスタンスを格納したOptionalを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Productを返却すること
     */
    @Test
    void testGet() throws Exception {
        Long id = 1L;

        Optional<Product> product = Optional.of(new Product());
        doReturn(product).when(this.mockProductRepository).findById(id);

        assertThat(target.get(id)).isEqualTo(product.get());
    }
}
