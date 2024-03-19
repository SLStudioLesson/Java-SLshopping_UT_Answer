package com.example.slshopping_ut_answer.product;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.slshopping_ut_answer.FileUploadUtil;
import com.example.slshopping_ut_answer.entity.Product;

/**
 * 商品画像関連の処理を行うサービスクラス
 * ProductServiceと役割を分けるために、別クラスに処理を切り出しています
 */
@Service
public class ProductImageService {

    private static final String UPLOADED_DIR = "product-images/";

    /**
     * 画像ファイルアップロード処理
     *
     * @param mainImageMultipart 商品画像
     * @param savedProduct 商品情報
     * @throws IOException
     */
    public void saveUploadedImages(MultipartFile mainImageMultipart, Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = mainImageMultipart.getOriginalFilename();
            String uploadDir = UPLOADED_DIR + savedProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }
    }

    /**
     * 商品画像名をProductエンティティにセットする
     *
     * @param mainImageMultipart 商品画像
     * @param product 商品情報
     */
    public void setMainImageName(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = mainImageMultipart.getOriginalFilename();
            product.setImage(fileName);
        }
    }

    /**
     * 商品画像のサイズと種類をチェックする
     *
     * @param mainImageMultipart 商品画像
     * @return
     */
    public boolean isValid(MultipartFile mainImageMultipart) {
        // ファイルが添付されていない場合
        if (mainImageMultipart.isEmpty()) {
            return true;
        }
        // ファイルサイズが2MBより大きい場合
        if (mainImageMultipart.getSize() > 2 * 1024 * 1024) {
            return false;
        }

        String originalFileName = mainImageMultipart.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
        // ファイルの種類が画像じゃない場合
        if (!Arrays.asList("png", "jpg", "jpeg").contains(fileExtension)) {
            return false;
        }
        return true;
    }

    /**
     * 商品画像を削除する
     *
     * @param id
     */
    public void delete(Long id) {
        String deleteDir= UPLOADED_DIR + id;
        FileUploadUtil.removeDir(deleteDir);
    }
}
