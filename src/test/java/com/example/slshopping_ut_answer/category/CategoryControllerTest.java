package com.example.slshopping_ut_answer.category;

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

import com.example.slshopping_ut_answer.entity.Category;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    /** モック化したクラス */
    @Mock
    private CategoryService mockCategoryService;

    /** テスト対象クラスにモックを注入 */
    @InjectMocks
    private CategoryController target;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // MockMvcの生成
        this.mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();
    }

    /**
     * 【概要】
     * 概要 カテゴリー一覧表示画面の検証<br>
     *
     * 【条件】
     * GET通信の/categoriesにリクエストすること<br>
     * クエリパラメーターkeywordにはnullを入力すること<br>
     * categoryServiceのfindAllメソッドはカテゴリーのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * categories/categories.htmlを表示すること<br>
     * キー名listCategoriesにカテゴリーのリストが格納されていること<br>
     * キー名keywordにnullが格納されていること
     */
    @Test
    void testListCategories() throws Exception {
        List<Category> categories = new ArrayList<>();
        String keyword = null;

        doReturn(categories).when(this.mockCategoryService).listAll(keyword);

        this.mockMvc.perform(get("/categories").param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(view().name("categories/categories"))
                .andExpect(model().attribute("listCategories", categories))
                .andExpect(model().attribute("keyword", keyword));
    }

    /**
     * 【概要】
     * カテゴリー新規登録画面の検証<br>
     *
     * 【条件】
     * GET通信の/categories/newにリクエストすること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * categories/category_form.htmlを表示すること<br>
     * キー名categoryにCategoryのインスタンスが格納されていること
     */
    @Test
    void testNewCategory() throws Exception {
        this.mockMvc.perform(get("/categories/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories/category_form"))
                .andExpect(model().attribute("category", instanceOf(Category.class)));
    }

    /**
     * 【概要】
     * カテゴリー新規登録処理の検証<br>
     *
     * 【条件】
     * POST通信の/categories/saveにリクエストすること<br>
     * バリデーションを通過する値をパラメーターにすること<br>
     * categoryServiceのcheckUniqueメソッドはtrueを返却するようスタブ化すること<br>
     * categoryServiceのsaveメソッドはCategoryのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /categoriesにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「登録に成功しました」という文字列が格納されていること
     */
    @Test
    void testSaveCategory() throws Exception {
        Category category = new Category();

        doReturn(true).when(this.mockCategoryService).checkUnique(category);
        doReturn(category).when(this.mockCategoryService).save(category);

        this.mockMvc.perform(post("/categories/save").flashAttr("category", category))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/categories"))
                .andExpect(flash().attribute("success_message", "登録に成功しました"));

    }

    /**
     * 【概要】
     * カテゴリー詳細画面の検証<br>
     *
     * 【条件】
     * GET通信の/categories/detail/1にリクエストすること<br>
     * categoryServiceのgetメソッドはID1LのCategoryを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * categories/category_detail.htmlを表示すること<br>
     * キー名categoryにID1LのCategoryが格納されていること
     */
    @Test
    void testDetailCategory() throws Exception {
        Long id = 1L;
        Category category = new Category();

        doReturn(category).when(this.mockCategoryService).get(id);

        this.mockMvc.perform(get("/categories/detail/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("categories/category_detail"))
                .andExpect(model().attribute("category", category));
    }

    /**
     * 【概要】
     * カテゴリー編集画面の検証<br>
     *
     * 【条件】
     * GET通信の/categories/edit/1にリクエストすること<br>
     * categoryServiceのgetメソッドがID1LのCategoryを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * categories/category_edit.htmlを表示すること<br>
     * キー名categoryにID1LのCategoryが格納されていること
     */
    @Test
    void testEditCategoryForm() throws Exception {
        Long id = 1L;
        Category category = new Category();

        when(this.mockCategoryService.get(id)).thenReturn(category);
        this.mockMvc.perform(get("/categories/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("categories/category_edit"))
                .andExpect(model().attribute("category", category));

    }

    /**
     * 【概要】
     * カテゴリー更新処理の検証<br>
     *
     * 【条件】
     * POST通信の/categories/edit/1にリクエストすること<br>
     * バリデーションを通過する値をパラメーターにすること<br>
     * categoryServiceのcheckUniqueメソッドはtrueを返却するようスタブ化すること<br>
     * categoryServiceのsaveメソッドはCategoryのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /categoriesにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「更新に成功しました」という文字列が格納されていること
     */
    @Test
    void testEditCategory() throws Exception {
        // 準備
        Long id = 1L;
        Category category = new Category(1L, "categoryA");

        // スタブを設定
        doReturn(true).when(this.mockCategoryService).checkUnique(category);
        doReturn(category).when(this.mockCategoryService).save(category);

        // 検証
        this.mockMvc.perform(post("/categories/edit/{id}", id).flashAttr("category", category))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/categories"))
                .andExpect(flash().attribute("success_message", "更新に成功しました"));
    }

    /**
     * 【概要】
     * カテゴリー削除処理の検証<br>
     *
     * 【条件】
     * GET通信の/categories/delete/1にリクエストすること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /categoriesにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「削除に成功しました」という文字列が格納されていること
     */
    @Test
    void testDeleteCategory() throws Exception {
        Long id = 1L;

        doNothing().when(this.mockCategoryService).delete(id);

        this.mockMvc.perform(get("/categories/delete/{id}", id))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/categories"))
                .andExpect(flash().attribute("success_message", "削除に成功しました"));
    }
}
