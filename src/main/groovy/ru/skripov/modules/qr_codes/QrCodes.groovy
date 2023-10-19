package ru.skripov.modules.qr_codes

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

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.charset.StandardCharsets

class QrCodes {
    MultiFormatReader multiFormatReader = new MultiFormatReader()
    ByteArrayOutputStream baos
    Map<DecodeHintType, Object> hints = new HashMap()

    QrCodes() {
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE)
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.QR_CODE))
        hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name())
        multiFormatReader.setHints(hints)
    }

    String findQrCode(String url) {
        URL imageURL
        try {
            imageURL = new URL(url)
        } catch (Exception e) {
            println "error url: $url"
        }
        BufferedImage originalImage = ImageIO.read(imageURL)
        baos = new ByteArrayOutputStream()
        ImageIO.write(originalImage, "jpg", baos)

        LuminanceSource source = new BufferedImageLuminanceSource(originalImage)
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source))

        try {
            Result result = multiFormatReader.decode(bitmap, hints)
        } catch (NotFoundException ignored) {
            url = "NO_CODE"
        } catch (ReaderException ignored) {
            url = "UNKNOWN"
        }

        return url
    }

    void close() {
        baos.close()
    }
}
