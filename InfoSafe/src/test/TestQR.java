package test;

import hznu.grade15x.QRCode.QRCodeUtil;

public class TestQR {
	public static void main(String[] args) throws Exception {
		String text = "http://www.baidu.com";
	    String myBasePath="C://Users//泽林//Desktop//Github//InfoSafe//InfoSafe//WebContent//QRCode";
	    String myLogoPath=myBasePath+"//qq.png";
	    QRCodeUtil.id="55";
	    QRCodeUtil.encode(text, myLogoPath, myBasePath, true);
	}
}
