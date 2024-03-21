package com.example.slshopping_ut_answer.user;

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

import com.example.slshopping_ut_answer.entity.User;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    /** モック化したクラス */
    @Mock
    private UserRepository mockUserRepository;

    /** テスト対象クラスにモックを注入 */
    @InjectMocks
    private  UserService target;

    /**
     * 【概要】
     * 管理者を検索<br>
     *
     * 【条件】
     * userServiceのlistAllメソッドにnullを渡すこと<br>
     * userRepositoryのfindAllメソッドはUserのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Userのリストを返却すること
     */
    @Test
    void testListAll_argumentIsNull() {
        List<User> expected = Arrays.asList(
            new User(),
            new User()
        );

        doReturn(expected).when(this.mockUserRepository).findAll();

        assertThat(target.listAll(null)).isEqualTo(expected);
    }

    /**
     * 【概要】
     * 管理者を検索<br>
     *
     * 【条件】
     * userServiceのlistAllメソッドに空文字を渡すこと<br>
     * userRepositoryのfindAllメソッドはUserのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Userのリストを返却すること
     */
    @Test
    void testListAll_argumentIsEmpty() {
        List<User> expected = Arrays.asList(
            new User(),
            new User()
        );

        doReturn(expected).when(this.mockUserRepository).findAll();

        assertThat(target.listAll("")).isEqualTo(expected);
    }

    /**
     * 【概要】
     * 管理者を検索<br>
     *
     * 【条件】
     * userServiceのlistAllメソッドにuserという文字列を渡すこと<br>
     * userRepositoryのsearchメソッドはUserのリストを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Userのリストを返却すること
     */
    @Test
    void testListAll_argumentIsNotEmpty() {
        String keyword = "user";

        List<User> expected = Arrays.asList(
            new User(),
            new User()
        );

        doReturn(expected).when(this.mockUserRepository).search(keyword);

        assertThat(target.listAll(keyword)).isEqualTo(expected);
    }

   /**
     * 【概要】
     * 管理メールアドレスの重複チェック<br>
     *
     * 【条件】
     * userRepositoryのfindByEmailメソッドはnullを返すようスタブ化すること<br>
     *
     * 【結果】
     * trueを返すこと
     */
    @Test
    void testCheckUnique_noDuplication() {
        User newUser= new User();
        newUser.setEmail("test@example.com");

        doReturn(null).when(this.mockUserRepository).findByEmail(anyString());

        assertThat(target.checkUnique(newUser)).isTrue();
    }

    /**
     * 【概要】
     * 管理メールアドレスの重複チェック<br>
     *
     * 【条件】
     * userRepositoryのfindByEmailメソッドはUserのインスタンスを返すようスタブ化すること<br>
     *
     * 【結果】
     * falseを返すこと
     */
    @Test
    void testCheckUnique_duplicate() {
        User newUser = new User();
        newUser.setEmail("test@example.com");

        User mockUser = new User();
        mockUser.setEmail("test@example.com");

        doReturn(mockUser).when(this.mockUserRepository).findByEmail(newUser.getEmail());

        assertThat(target.checkUnique(newUser)).isFalse();
    }

    /**
     * 【概要】
     * 管理者情報の取得<br>
     *
     * 【条件】
     * userRepositoryのfindByIdメソッドはUserのインスタンスを格納したOptionalを返却するようスタブ化すること<br>
     *
     * 【結果】
     * 例外が発生しないこと
     */
    @Test
    void testGet_noThrowsException() {
        Long id = 1L;

        Optional<User> user = Optional.of(new User());
        doReturn(user).when(this.mockUserRepository).findById(id);

        assertThatCode(() -> {
            target.get(id);
        }).doesNotThrowAnyException();
    }

    /**
     * 【概要】
     * 管理者情報の取得<br>
     *
     * 【条件】
     * userRepositoryのfindByIdメソッドはnullを格納したOptionalを返却するようスタブ化すること<br>
     *
     * 【結果】
     * 例外が発生すること
     */
    @Test
    void testGet_throwsException() {
        Long id = 1000L;

        Optional<User> user = Optional.ofNullable(null);

        doReturn(user).when(this.mockUserRepository).findById(id);

        assertThatThrownBy(() -> {
            target.get(id);
        })
        .isInstanceOf(NotFoundException.class);
    }

    /**
     * 【概要】
     * 管理者情報の取得処理の検証<br>
     *
     * 【条件】
     * userRepositoryのfindByIdはUserのインスタンスを格納したOptionalを返却するようスタブ化すること<br>
     *
     * 【結果】
     * Userを返却すること
     */
    @Test
    void testGet() throws Exception {
        Long id = 1L;
        Optional<User> user = Optional.of(new User());

        doReturn(user).when(this.mockUserRepository).findById(id);

        User actual = this.target.get(id);
        assertThat(actual).isEqualTo(user.get());
    }
}
