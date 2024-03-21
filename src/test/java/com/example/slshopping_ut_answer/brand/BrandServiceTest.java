package com.example.slshopping_ut_answer.brand;

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

import com.example.slshopping_ut_answer.entity.Brand;

/*
 * @SpringBootTest
 * 簡易版のテストはこのアノテーションを使う
 * @ExtendWith(MockitoExtension.class)と一緒には使えないためどちらか一方を利用する形となる
 */
@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    /** モック化したクラス */
    @Mock
    private BrandRepository mockBrandRepository;

    /** テスト対象クラスにモックを注入 */
    @InjectMocks
    private BrandService target;

    /**
     * 【概要】
     * ブランドのリストを取得<br>
     *
     * 【結果】
     * ブランドのリストを返却すること
     */
    @Test
    void testListAll() {
        List<Brand> expected = Arrays.asList(
            new Brand(1L, "brandA"),
            new Brand(2L, "brandB")
        );

        // スタブの設定
        doReturn(expected).when(this.mockBrandRepository).findAll();

        // 検証処理
        assertThat(target.listAll()).isEqualTo(expected);
    }

    /**
     * 【概要】
     * ブランドを検索<br>
     *
     * 【条件】
     * brandServiceのlistAllメソッドにnullを渡すこと<br>
     * brandRepositoryのfindAllメソッドはBrandのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Brandのリストを返却すること
     */
    @Test
    void testListAll_argumentIsNull() {
        List<Brand> expected = Arrays.asList(
            new Brand(1L, "brandA"),
            new Brand(2L, "brandB")
        );

        doReturn(expected).when(this.mockBrandRepository).findAll();

        assertThat(target.listAll(null)).isEqualTo(expected);
    }

    /**
     * 【概要】
     * 商品を検索<br>
     *
     * 【条件】
     * brandServiceのlistAllメソッドに空文字を渡すこと<br>
     * brandRepositoryのfindAllメソッドはBrandのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Brandのリストを返却すること
     */
    @Test
    void testListAll_argumentIsEmpty() {
        List<Brand> expected = Arrays.asList(
            new Brand(1L, "brandA"),
            new Brand(2L, "brandB")
        );

        doReturn(expected).when(this.mockBrandRepository).findAll();

        assertThat(target.listAll("")).isEqualTo(expected);
    }

    /**
     * 【概要】
     * ブランドを検索<br>
     *
     * 【条件】
     * brandServiceのlistAllメソッドにbrandという文字列を渡すこと<br>
     * brandRepositoryのsearchメソッドはBrandのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Brandのリストを返却すること
     */
    @Test
    void testListAll_argumentIsNotEmpty() {
        String keyword = "brand";

        List<Brand> expected = Arrays.asList(
            new Brand(1L, "brandA"),
            new Brand(2L, "brandB")
        );

        doReturn(expected).when(this.mockBrandRepository).search(keyword);

        assertThat(target.listAll(keyword)).isEqualTo(expected);
    }

    /**
     * 【概要】
     * ブランド名の重複チェック<br>
     *
     * 【条件】
     * brandRepositoryのfindByNameメソッドはnullを返すようスタブ化すること<br>
     *
     * 【結果】
     * trueを返すこと
     */
    @Test
    void testCheckUnique_noDuplication() {
        // ブランド名が重複していないブランド情報を作成
        Brand newBrand = new Brand(1L, "brandA");

        // スタブの設定
        doReturn(null).when(this.mockBrandRepository).findByName(anyString());

        // 検証処理
        assertThat(target.checkUnique(newBrand)).isTrue();
    }

    /**
     * 【概要】
     * ブランド名の重複チェック<br>
     *
     * 【条件】
     * brandRepositoryのfindByNameメソッドはBrandのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * falseを返すこと
     */
    @Test
    void testCheckUnique_duplicate() {
        // 準備 ブランド名が重複するブランド情報を作成
        Brand newBrand = new Brand(1L, "brandA");

        // スタブに設定するデータを作成
        Brand mockBrand = new Brand(1L, "brandA");

        //スタブの設定
        doReturn(mockBrand).when(this.mockBrandRepository).findByName(newBrand.getName());

        // 検証
        assertThat(target.checkUnique(newBrand)).isFalse();
    }

    /**
     * 【概要】
     * ブランド情報の取得<br>
     *
     * 【条件】
     * brandRepositoryのfindByIdメソッドはBrandのインスタンスを格納したOptionalを返却するようスタブ化すること<br>
     *
     * 【結果】
     * 例外が発生しないこと
     */
    @Test
    void testGet_noThrowsException() {
        // 準備 テストデータに存在するID
        Long id = 1L;

        // スタブに設定するデータを作成
        Optional<Brand> brand = Optional.of(new Brand());

        //スタブの設定
        doReturn(brand).when(this.mockBrandRepository).findById(id);

        // 検証
        assertThatCode(() -> {
            target.get(id);
        }).doesNotThrowAnyException();
    }

    /**
     * 【概要】
     * ブランド情報の取得<br>
     *
     * 【条件】
     * brandRepositoryのfindByIdメソッドはnullを格納したOptionalを返却するようスタブ化すること<br>
     *
     * 【結果】
     * 例外が発生すること
     */
    @Test
    void testGet_throwsException() {
        // 準備 テストデータに存在しないID
        Long id = 1000L;

        // スタブに設定するデータを作成
        Optional<Brand> brand = Optional.ofNullable(null);

        //スタブの設定
        doReturn(brand).when(this.mockBrandRepository).findById(id);

        // 検証処理
        assertThatThrownBy(() -> {
            target.get(id);
        })
        .isInstanceOf(NotFoundException.class);
    }

    /**
     * 【概要】
     * ブランド情報の取得処理の検証<br>
     *
     * 【条件】
     * brandRepositoryのfindByIdはBrandのインスタンスを格納したOptionalを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Brandを返却すること
     */
    @Test
    void testGet() throws Exception {
        // 準備 任意のID
        Long id = 1L;

        // スタブに設定するデータを作成
        Optional<Brand> brand = Optional.of(new Brand());

        // スタブの設定
        doReturn(brand).when(this.mockBrandRepository).findById(id);

        // 検証
        assertThat(target.get(id)).isEqualTo(brand.get());
    }
}
