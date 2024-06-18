package com.example.slshopping_ut_answer.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.slshopping_ut_answer.brand.BrandService;
import com.example.slshopping_ut_answer.category.CategoryService;
import com.example.slshopping_ut_answer.entity.Brand;
import com.example.slshopping_ut_answer.entity.Category;
import com.example.slshopping_ut_answer.entity.Product;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    /**
     * ブランドリストをmodelに追加する処理
     * 複数のハンドラメソッドでlistBrandsをmodelに追加する必要があるため、共通化する
     *
     * @return
     */
    @ModelAttribute(name = "listBrands")
    public List<Brand> setBrandsToModel() {
        return brandService.listAll();
    }

    /**
     * カテゴリーリストをmodelに追加する処理
     * 複数のハンドラメソッドでlistCategoriesをmodelに追加する必要があるため、共通化する
     *
     * @return
     */
    @ModelAttribute(name = "listCategories")
    public List<Category> setCategoriesToModel() {
        return categoryService.listAll();
    }

    /**
     * 商品一覧画面表示
     *
     * @param model
     * @return 商品一覧画面
     */
    @GetMapping
    public String listProducts(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        // 全商品情報の取得
        List<Product> listProducts = productService.listAll(keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("keyword", keyword);
        return "products/products";
    }

    /**
     * 商品新規登録画面表示
     *
     * @param model
     * @return 商品新規登録画面
     */
    @GetMapping("/new")
    public String newProductForm(Model model) {
        // 新規登録用に、空の商品情報作成
        Product product = new Product();
        model.addAttribute("product", product);
        return "products/product_form";
    }

    /**
     * 商品登録
     *
     * @param product 商品情報
     * @param result
     * @param file 商品画像
     * @param model
     * @param ra
     * @return 商品一覧画面 or 商品登録画面
     * @throws Exception
     */
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute Product product, BindingResult result,
        MultipartFile file, Model model, RedirectAttributes ra) throws IOException {

        // 入力値のチェック
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "products/product_form";
        }

        // 重複チェック
        if (!productService.checkUnique(product)) {
            model.addAttribute("error_message", "重複しています");
            return "products/product_form";
        }

        // 画像ファイルのチェック
        if (!productImageService.isValid(file)) {
            model.addAttribute("file_error_message", "画像は2MB以内かつpng・jpg・jpeg形式で添付してください");
            return "products/product_form";
        }

        // 商品画像のファイル名を取得し、商品情報に格納する
        productImageService.setMainImageName(file, product);
        // 商品情報の登録
        Product savedProduct = productService.save(product);
        // 商品画像のファイルを保存する
        productImageService.saveUploadedImages(file, savedProduct);
        ra.addFlashAttribute("success_message", "登録に成功しました");
        return "redirect:/products";
    }

    /**
     * 商品詳細画面表示
     *
     * @param id 商品ID
     * @param model
     * @param ra
     * @return 商品詳細画面
     */
    @GetMapping("/detail/{id}")
    public String detailUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes ra) {
        try {
            // 商品IDに紐づく商品情報取得
            Product product = productService.get(id);
            model.addAttribute("product", product);
            return "products/product_detail";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error_message", "対象のデータが見つかりませんでした");
            return "redirect:/products";
        }
    }

    /**
     * 商品編集画面表示
     *
     * @param id 商品ID
     * @param model
     * @param ra
     * @return 商品編集画面
     */
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try {
            // 商品IDに紐づく商品情報取得
            Product product = productService.get(id);
            model.addAttribute("product", product);
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error_message", "対象のデータが見つかりませんでした");
            return "redirect:/products";
        }

        return "products/product_edit";
    }

    /**
     * 商品更新
     *
     * @param product 商品情報
     * @param result
     * @param file 商品画像
     * @param model
     * @param ra
     * @return 商品一覧画面 or 商品更新画面
     * @throws Exception
     */
    @PostMapping("/edit/{id}")
    public String editProduct(@Valid @ModelAttribute Product product, BindingResult result,
        MultipartFile file, Model model, RedirectAttributes ra) throws IOException {

        // 入力値のチェック
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "products/product_edit";
        }

        // 重複チェック
        if (!productService.checkUnique(product)) {
            model.addAttribute("error_message", "重複しています");
            return "products/product_edit";
        }

        // 画像ファイルのチェック
        if (!productImageService.isValid(file)) {
            model.addAttribute("file_error_message", "画像は2MB以内かつpng・jpg・jpeg形式で添付してください");
            return "products/product_edit";
        }

        // 商品画像のファイル名を取得し、商品情報に格納する
        productImageService.setMainImageName(file, product);
        // 商品情報の更新
        Product savedProduct = productService.save(product);
        // 商品画像のファイルを保存する
        productImageService.saveUploadedImages(file, savedProduct);
        ra.addFlashAttribute("success_message", "更新に成功しました");
        return "redirect:/products";
    }

    /**
     * 商品削除処理
     *
     * @param id 商品ID
     * @param model
     * @param ra
     * @return 商品一覧画面
     */
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id, Model model, RedirectAttributes ra) {
        try {
            // 商品情報削除
            productService.delete(id);
            productImageService.delete(id);
            ra.addFlashAttribute("success_message", "削除に成功しました");
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error_message", "対象のデータが見つかりませんでした");
        }
        return "redirect:/products";
    }

}
