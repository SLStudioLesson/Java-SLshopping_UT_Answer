package com.example.slshopping_ut_answer.product;

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

import com.example.slshopping_ut_answer.brand.BrandService;
import com.example.slshopping_ut_answer.category.CategoryService;
import com.example.slshopping_ut_answer.entity.Brand;
import com.example.slshopping_ut_answer.entity.Category;
import com.example.slshopping_ut_answer.entity.Product;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    /** モック化したクラス */
    @Mock
    private ProductService mockProductService;

    @Mock
    private BrandService mockBrandService;

    @Mock
    private CategoryService mockCategoryService;

    @Mock
    private ProductImageService mockProductImageService;

    /** テスト対象クラスにモックを注入 */
    @InjectMocks
    private ProductController target;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // MockMvcの生成
        this.mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();
    }

    /**
     * 【概要】
     * 商品一覧表示画面の検証<br>
     *
     * 【条件】
     * GET通信の/productsにリクエストすること<br>
     * クエリパラメーターkeywordにはnullを入力すること<br>
     * productServiceのfindAllメソッドは商品のリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * products/products.htmlを表示すること<br>
     * キー名listProductsに商品のリストが格納されていること<br>
     * キー名keywordにnullが格納されていること
     */
    @Test
    void testListProducts() throws Exception {
        List<Product> products = new ArrayList<>();
        String keyword = null;

        doReturn(products).when(this.mockProductService).listAll(keyword);

        this.mockMvc.perform(get("/products").param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(view().name("products/products"))
                .andExpect(model().attribute("listProducts", products))
                .andExpect(model().attribute("keyword", keyword));
    }

    /**
     * 【概要】
     * 商品新規登録画面の検証<br>
     *
     * 【条件】
     * GET通信の/products/newにリクエストすること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * products/product_form.htmlを表示すること<br>
     * キー名productにProductのインスタンスが格納されていること
     */
    @Test
    void testNewProduct() throws Exception {
        this.mockMvc.perform(get("/products/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/product_form"))
                .andExpect(model().attribute("product", instanceOf(Product.class)));
    }

    /**
     * 【概要】
     * 商品新規登録処理の検証
     *
     * 【条件】
     * POST通信の/products/saveにリクエストすること<br>
     * バリデーションを通過する値をパラメーターにすること<br>
     * productImageServiceのisValidメソッドはtrueを返却するようスタブ化すること<br>
     * productServiceのcheckUniqueメソッドはtrueを返却するようスタブ化すること<br>
     * productsServiceのsaveメソッドはProductのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /productsにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「登録に成功しました」という文字列が格納されていること
     */
    @Test
    void testSaveProduct() throws Exception {
        Brand brand = new Brand(1L, "brandA");
        Category category = new Category(1L, "categoryA");
        Product product = new Product(1L, "productA", "description", 1, "image",
            1.0, 1.0, 1.0, 1.0, category, brand);

        doReturn(true).when(this.mockProductImageService).isValid(null);
        doReturn(true).when(this.mockProductService).checkUnique(product);
        doReturn(product).when(this.mockProductService).save(product);

        this.mockMvc.perform(post("/products/save")
                .flashAttr("product", product))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attribute("success_message", "登録に成功しました"));
    }

    /**
     * 【概要】
     * 商品詳細画面の検証<br>
     *
     * 【条件】
     * GET通信の/products/detail/1にリクエストすること<br>
     * productServiceのgetメソッドはID1LのProductを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * products/product_detail.htmlを表示すること<br>
     * キー名productにID1LのProductが格納されていること
     */
    @Test
    void testDetailProduct() throws Exception {
        Long id = 1L;
        Product product = new Product();

        doReturn(product).when(this.mockProductService).get(id);

        this.mockMvc.perform(get("/products/detail/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("products/product_detail"))
                .andExpect(model().attribute("product", product));
    }

    /**
     * 【概要】
     * 商品編集画面の検証<br>
     *
     * 【条件】
     * GET通信の/products/edit/1にリクエストすること<br>
     * productServiceのgetメソッドがID1LのProductを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * products/product_edit.htmlを表示すること<br>
     * キー名productにID1LのProductが格納されていること
     */
    @Test
    void testEditProductForm() throws Exception {
        Long id = 1L;
        Product product = new Product();

        when(this.mockProductService.get(id)).thenReturn(product);

        this.mockMvc.perform(get("/products/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("products/product_edit"))
                .andExpect(model().attribute("product", product));
    }

    /**
     * 【概要】
     * 商品更新処理の検証
     *
     * 【条件】
     * POST通信の/products/edit/1にリクエストすること<br>
     * バリデーションを通過する値をパラメーターにすること<br>
     * productImageServiceのisValidメソッドはtrueを返却するようスタブ化すること<br>
     * productServiceのcheckUniqueメソッドはtrueを返却するようスタブ化すること<br>
     * productsServiceのsaveメソッドはProductのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /productsにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「更新に成功しました」という文字列が格納されていること
     */
    @Test
    void testEditProduct() throws Exception {
        Long id = 1L;
        Brand brand = new Brand(1L, "brandA");
        Category category = new Category(1L, "categoryA");
        Product product = new Product(id, "productA", "description", 1, "image",
            1.0, 1.0, 1.0, 1.0, category, brand);

        doReturn(true).when(this.mockProductImageService).isValid(null);
        doReturn(true).when(this.mockProductService).checkUnique(product);
        doReturn(product).when(this.mockProductService).save(product);

        this.mockMvc.perform(post("/products/edit/{id}", id)
                .flashAttr("product", product))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attribute("success_message", "更新に成功しました"));
    }

    /**
     * 【概要】
     * 商品削除処理の検証<br>
     *
     * 【条件】
     * GET通信の/products/delete/1にリクエストすること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /productsにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「削除に成功しました」という文字列が格納されていること
     */
    @Test
    void testDeleteProduct() throws Exception {
        Long id = 1L;

        doNothing().when(this.mockProductService).delete(id);

        this.mockMvc.perform(get("/products/delete/{id}", id))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attribute("success_message", "削除に成功しました"));
    }
}
