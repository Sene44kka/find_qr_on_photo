package ru.skripov.modules.qr_codes

import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.EncodeHintType
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.ReaderException
import com.google.zxing.Result
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

import javax.imageio.ImageIO
import java.nio.charset.StandardCharsets

class QrCodes {
    MultiFormatReader multiFormatReader = new MultiFormatReader()
    Map<DecodeHintType, Object> hints = new HashMap()

    QrCodes() {
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE)
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(
                BarcodeFormat.QR_CODE,
                BarcodeFormat.AZTEC,
                BarcodeFormat.DATA_MATRIX,
                BarcodeFormat.MAXICODE,
                BarcodeFormat.PDF_417
        ))
        hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name())
        hints.put(DecodeHintType.ALSO_INVERTED, Boolean.TRUE)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L)
        multiFormatReader.setHints(hints)
    }

    String findQrCode(String url) {
        URL imageURL
        try {
            imageURL = new URL(url)
        } catch (Exception e) {
            println "error url: $url"
            return
        }

        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(imageURL))));

        try {
            Result result = multiFormatReader.decode(bitmap, hints)
        } catch (NotFoundException ignored) {
            url = "NO_CODE"
        } catch (ReaderException ignored) {
            url = "UNKNOWN"
        }

        return url
    }
}
