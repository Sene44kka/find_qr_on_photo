package ru.skripov

import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.Result
import com.google.zxing.ResultPoint
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.DetectorResult
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.detector.Detector

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.charset.StandardCharsets

static void main(String[] args) {

    List<String> urlList = [
            "https://goods-photos.static1-sima-land.com/items/4292528/0/700.jpg?v=1661777599",
            "https://goods-photos.static1-sima-land.com/items/448821/3/1600.jpg?v=1665737046",
            "https://goods-photos.static1-sima-land.com/items/3904631/0/1600.jpg?v=1657021724",
            "https://amegaprint.ru/upload/medialibrary/ac0/ac05a6353ac7599f3787c34aa9bff668.jpg"
    ]

    for (url in urlList) {
        URL imageURL = new URL(url)
        BufferedImage originalImage = ImageIO.read(imageURL)
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ImageIO.write(originalImage, "jpg", baos)


        LuminanceSource source = new BufferedImageLuminanceSource(originalImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        MultiFormatReader multiFormatReader = new MultiFormatReader();
        Map<DecodeHintType, Object> hints = new HashMap();
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.QR_CODE));
        hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        multiFormatReader.setHints(hints);

        try {
            Result result = multiFormatReader.decode(bitmap, hints)
            println "url $url"
        } catch (Exception e) {
            println "Ничего нет на картинке"
        }
    }
}
