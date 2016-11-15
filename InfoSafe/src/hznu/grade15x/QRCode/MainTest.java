package com.yihaomen.barcode;

public class MainTest {
	public static void main(String[] args) throws Exception {
        String text = "http://www.baidu.com";
        String basePath="C://Users//泽林//Desktop//barcodesample//WebContent//QRCode";
        String logoPath=basePath+"//qq.png";
        QRCodeUtil.id="5";
        QRCodeUtil.encode(text, logoPath, basePath, true);
    }
}
