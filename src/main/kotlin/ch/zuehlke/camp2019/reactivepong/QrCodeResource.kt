package ch.zuehlke.camp2019.reactivepong

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.net.InetSocketAddress
import java.net.Socket


@RestController
class QrCodeResource {


    @GetMapping("qrcode/{side}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun generate(@PathVariable("side") side: Side): ByteArray? {
        return generateQRCodeImage(350, 350, side)
    }

    private fun generateQRCodeImage(width: Int, height: Int, side: Side): ByteArray? {

        val socket = Socket()
        socket.connect(InetSocketAddress("google.com", 80))
        val localAddress = socket.localAddress


        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode("http:/$localAddress:8080/mobile/index.html?side=$side", BarcodeFormat.QR_CODE, width, height)
        val output = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", output)
        return output.toByteArray()
    }


}