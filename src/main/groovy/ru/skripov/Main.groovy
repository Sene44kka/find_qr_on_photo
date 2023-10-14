package ru.skripov

import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.ReaderException
import com.google.zxing.Result
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import ru.skripov.modules.excel.ExcelReader

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.charset.StandardCharsets

static void main(String[] args) {

    List<String> urlList = []
    ExcelReader excelReader = new ExcelReader("/home/skripov/git/myProjects/find_qr_on_photo/urls.xls")

    excelReader.eachLine {
        int cellSize = (it.cells - null).size()
        for (int i = 0; i < cellSize; i++) {
            urlList.add(excelReader.cell(i).toString())
        }
    }

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
        } catch (NotFoundException ignored) {
            println "Ничего нет на картинке"
        } catch (ReaderException ignored) {
            println "Не смог понять есть ли qr код для данной картинки - $url"
        }
    }
}
