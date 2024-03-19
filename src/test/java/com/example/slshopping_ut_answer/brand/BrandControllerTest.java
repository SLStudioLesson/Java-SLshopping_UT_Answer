package com.example.slshopping_ut_answer.brand;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.slshopping_ut_answer.entity.Brand;

@ExtendWith(MockitoExtension.class) // JUnit5でMockito使うために書く
class BrandControllerTest {

    /** モック化したクラス */
    @Mock
    private BrandService mockBrandService;

    /** テスト対象クラスにモックを注入 */
    @InjectMocks
    private BrandController target;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // MockMvcの生成
        this.mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();
    }

    /**
     * 【概要】
     * ブランド一覧表示画面の検証<br>
     *
     * 【条件】
     * GET通信の/brandsにリクエストすること<br>
     * クエリパラメーターkeywordにはnullを入力すること<br>
     * brandServiceのfindAllメソッドはブランドのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * brands/brands.htmlを表示すること<br>
     * キー名listBrandsにブランドのリストが格納されていること<br>
     * キー名keywordにnullが格納されていること
     */
    @Test
    void testListBrands() throws Exception {
        // 準備
        List<Brand> brands = new ArrayList<>();
        String keyword = null;

        // スタブを設定
        // doReturn(返り値の設定).when(対象のモック).対象のメソッド(引数)
        doReturn(brands).when(this.mockBrandService).listAll(keyword);

        // 検証
        /*
         * perform(get("path") httpメソッドとパスの指定
         * param("key", val) クエリストリングの指定
         * andExpect(検証したいこと)
         * 検証例：
         * status().isOk() ステータスコードの検証
         * view().name("テンプレートファイル名") テンプレートファイルの呼び出しがあっているか
         * model().attribute("key", val) modelに格納されているか
         */
        this.mockMvc.perform(get("/brands").param("keyword", keyword)) // リクエストの情報
                .andExpect(status().isOk()) // ステータスの検証
                .andExpect(view().name("brands/brands")) // テンプレートファイルの呼び出し検証
                .andExpect(model().attribute("listBrands", brands)) // modelに格納されている要素の検証
                .andExpect(model().attribute("keyword", keyword));

    }

    /**
     * 【概要】
     * ブランド新規登録画面の検証<br>
     *
     * 【条件】
     * GET通信の/brands/newにリクエストすること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * brands/brand_form.htmlを表示すること<br>
     * キー名brandにBrandのインスタンスが格納されていること
     */
    @Test
    void testNewBrand() throws Exception {
        // 検証
        /*
         * 検証例：
         * model().attribute("key", instanceOf(対象クラス名.class)) モデルに格納されているクラスが、対象インスタンスか
         */
        this.mockMvc.perform(get("/brands/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("brands/brand_form"))
                .andExpect(model().attribute("brand", instanceOf(Brand.class)));
    }

    /**
     * 【概要】
     * ブランド新規登録処理の検証<br>
     *
     * 【条件】
     * POST通信の/brands/saveにリクエストすること<br>
     * バリデーションを通過する値をパラメーターにすること<br>
     * brandServiceのcheckUniqueメソッドはtrueを返却するようスタブ化すること<br>
     * brandServiceのsaveメソッドはBrandのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /brandsにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「登録に成功しました」という文字列が格納されていること
     */
    @Test
    void testSaveBrand() throws Exception {
        // 準備
        Brand brand = new Brand();
        brand.setName("brandA");

        // スタブを設定
        doReturn(true).when(this.mockBrandService).checkUnique(brand);
        // doNothing()は返り値がないとき
        doReturn(brand).when(this.mockBrandService).save(brand);

        // 検証
        /*
         * 検証例：
         * redirectedUrl("path") リダイレクト先の検証
         * flash().attribute("key", val) // リダイレクト時の引継ぎ情報の検証
         */
        this.mockMvc.perform(post("/brands/save").flashAttr("brand", brand))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/brands"))
                .andExpect(flash().attribute("success_message", "登録に成功しました"));

    }

    /**
     * 【概要】
     * ブランド詳細画面の検証<br>
     *
     * 【条件】
     * GET通信の/brands/detail/1にリクエストすること<br>
     * brandServiceのgetメソッドはID1LのBrandを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * brands/brand_detail.htmlを表示すること<br>
     * キー名brandにID1LのBrandが格納されていること
     */
    @Test
    void testDetailBrand() throws Exception {
        // 準備
        Long id = 1L;
        Brand brand = new Brand();

        // スタブを設定
        doReturn(brand).when(this.mockBrandService).get(id);

        // 検証
        this.mockMvc.perform(get("/brands/detail/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("brands/brand_detail"))
                .andExpect(model().attribute("brand", brand));
    }

    /**
     * 【概要】
     * ブランド編集画面の検証<br>
     *
     * 【条件】
     * GET通信の/brands/edit/1にリクエストすること<br>
     * brandServiceのgetメソッドがID1LのBrandを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * brands/brand_edit.htmlを表示すること<br>
     * キー名brandにID1LのBrandが格納されていること
     */
    @Test
    void testEditBrandForm() throws Exception {
        // 準備
        Long id = 1L;
        Brand brand = new Brand();

        // スタブを設定
        when(this.mockBrandService.get(id)).thenReturn(brand);

        // 検証
        this.mockMvc.perform(get("/brands/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("brands/brand_edit"))
                .andExpect(model().attribute("brand", brand));

    }

    /**
     * 【概要】
     * ブランド更新処理の検証<br>
     *
     * 【条件】
     * POST通信の/brands/edit/1にリクエストすること<br>
     * バリデーションを通過する値をパラメーターにすること<br>
     * brandServiceのcheckUniqueメソッドはtrueを返却するようスタブ化すること<br>
     * brandServiceのsaveメソッドはBrandのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /brandsにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「更新に成功しました」という文字列が格納されていること
     */
    @Test
    void testEditBrand() throws Exception {
        // 準備
        Long id = 1L;
        Brand brand = new Brand(1L, "brandA");

        // スタブを設定
        doReturn(true).when(this.mockBrandService).checkUnique(brand);
        doReturn(brand).when(this.mockBrandService).save(brand);

        // 検証
        this.mockMvc.perform(post("/brands/edit/{id}", id).flashAttr("brand", brand))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/brands"))
                .andExpect(flash().attribute("success_message", "更新に成功しました"));
    }

    /**
     * 【概要】
     * ブランド削除処理の検証<br>
     *
     * 【条件】
     * GET通信の/brands/delete/1にリクエストすること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /brandsにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「削除に成功しました」という文字列が格納されていること
     */
    @Test
    void testDeleteBrand() throws Exception {
        // 準備
        Long id = 1L;

        // スタブの設定
        doNothing().when(this.mockBrandService).delete(id);

        // 検証
        this.mockMvc.perform(get("/brands/delete/{id}", id))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/brands"))
                .andExpect(flash().attribute("success_message", "削除に成功しました"));
    }
}
