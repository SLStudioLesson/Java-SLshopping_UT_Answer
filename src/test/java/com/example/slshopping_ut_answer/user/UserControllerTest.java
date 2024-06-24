package com.example.slshopping_ut_answer.user;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.slshopping_ut_answer.entity.Role;
import com.example.slshopping_ut_answer.entity.User;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    /** モック化したクラス */
    @Mock
    private UserService mockUserService;

    /** テスト対象クラスにモックを注入 */
    @InjectMocks
    private UserController target;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // MockMvcの生成
        this.mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();
    }

    /**
     * 【概要】
     * 管理者一覧表示画面の検証<br>
     *
     * 【条件】
     * GET通信の/usersにリクエストすること<br>
     * クエリパラメーターkeywordにはnullを入力すること<br>
     * userServiceのfindAllメソッドは管理者のリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * users/users.htmlを表示すること<br>
     * キー名listUsersに管理者のリストが格納されていること<br>
     * キー名keywordにnullが格納されていること
     */
    @Test
    void testListUsers() throws Exception {
        List<User> users = new ArrayList<>();
        String keyword = null;

        doReturn(users).when(this.mockUserService).listAll(keyword);

        this.mockMvc.perform(get("/users").param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(view().name("users/users"))
                .andExpect(model().attribute("listUsers", users))
                .andExpect(model().attribute("keyword", keyword));

    }

    /**
     * 【概要】
     * 管理者新規登録画面の検証<br>
     *
     * 【条件】
     * GET通信の/users/newにリクエストすること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * users/user_form.htmlを表示すること<br>
     * キー名userにUserのインスタンスが格納されていること
     */
    @Test
    void testNewUser() throws Exception {
        this.mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/user_form"))
                .andExpect(model().attribute("user", instanceOf(User.class)));
    }

    /**
     * 【概要】
     * 管理者新規登録処理の検証
     *
     * 【条件】
     * POST通信の/users/saveにリクエストすること<br>
     * バリデーションを通過する値をパラメーターにすること<br>
     * userServiceのcheckUniqueメソッドはtrueを返却するようスタブ化すること<br>
     * userServiceのsaveメソッドはUserのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /usersにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「登録に成功しました」という文字列が格納されていること
     */
    @Test
    void testNewUserForm() throws Exception {
        Set<Role> roles = Set.of(
            new Role(1L, "Admin", "管理者")
        );
        User user = new User(1L, "test@example.com", "password", "userA", false, roles);

        doReturn(true).when(this.mockUserService).checkUnique(user);
        doReturn(user).when(this.mockUserService).save(user);

        this.mockMvc.perform(post("/users/save").flashAttr("user", user))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("success_message", "登録に成功しました"));
    }

    /**
     * 【概要】
     * 管理者詳細画面の検証<br>
     *
     * 【条件】
     * GET通信の/users/detail/1にリクエストすること<br>
     * userServiceのgetメソッドはID1LのUserを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * users/user_detail.htmlを表示すること<br>
     * キー名userにID1LのUserが格納されていること
     */
    @Test
    void testDetailUser() throws Exception {
        Long id = 1L;
        User user = new User();

        doReturn(user).when(this.mockUserService).get(id);

        this.mockMvc.perform(get("/users/detail/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("users/user_detail"))
                .andExpect(model().attribute("user", user));
    }

    /**
     * 【概要】
     * 管理者編集画面の検証<br>
     *
     * 【条件】
     * GET通信の/users/edit/1にリクエストすること<br>
     * userServiceのgetメソッドがID1LのUserを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが200であること<br>
     * users/user_edit.htmlを表示すること<br>
     * キー名userにID1LのUserが格納されていること
     */
    @Test
    void testEditUserForm() throws Exception {
        Long id = 1L;
        User user = new User();

        when(this.mockUserService.get(id)).thenReturn(user);

        this.mockMvc.perform(get("/users/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("users/user_edit"))
                .andExpect(model().attribute("user", user));
    }

    /**
     * 【概要】
     * 管理者編集処理の検証
     *
     * 【条件】
     * POST通信の/users/edit/1にリクエストすること<br>
     * バリデーションを通過する値をパラメーターにすること<br>
     * userServiceのcheckUniqueメソッドはtrueを返却するようスタブ化すること<br>
     * userServiceのsaveメソッドはUserのインスタンスを返却するようスタブ化すること<br>
     *
     * 【結果】
     * ステータスが302であること<br>
     * /usersにリダイレクトしていること<br>
     * リダイレクト先にキー名success_messageに「更新に成功しました」という文字列が格納されていること
     */
    @Test
    void testEditProduct() throws Exception {
        Long id = 1L;

        Set<Role> roles = Set.of(
            new Role(1L, "Admin", "管理者")
        );
        User user = new User(id, "test@example.com", "password", "userA", false, roles);

        doReturn(true).when(this.mockUserService).checkUnique(user);
        doReturn(user).when(this.mockUserService).save(user);

        this.mockMvc.perform(post("/users/edit/{id}", id).flashAttr("user", user))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("success_message", "更新に成功しました"));
    }
}
