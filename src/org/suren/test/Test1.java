package org.suren.test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Properties;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class Test1
{
	private static BASE64Encoder encoder = new sun.misc.BASE64Encoder();   
    private static BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    
	private static final String[] keys = {"Product.name", "Product.version", "License.type",
		"License.expiry", "Server.macaddress", "Server.sno", "signature.data"};
	
	
//	private static final String data = "洪卫综合安全网关";
	private static final String file = "c://license//license.properties";
	private static final String privateKey = "c://license//ca.key";
	private static final String publicCert = "c://license//ca.crt";
	
	public static void main(String[] args) throws Exception
	{

//		String data = sig();
//		storeSignData2pro(data);
		
		System.out.println(valid(getVailData()));
		
	}
	
	

	
	public static String sig() throws Exception
	{
		
		PrivateKey privateKey = getCaKey();
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(privateKey);
		sig.update(getSignData().getBytes());
		byte[] sign = sig.sign();
		return encoder.encode(sign);
	}
	
	public static boolean valid(String signData) throws Exception
	{
		
		System.out.println(signData);
		PublicKey publicKey = getCaCert().getPublicKey();
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initVerify(publicKey);
		sig.update(getSignData().getBytes());
		return sig.verify(decoder.decodeBuffer(signData));
	}
	
	public static PrivateKey getCaKey()
	{
		
		try
		{
			FileInputStream is = new FileInputStream(privateKey);
			byte[] buf = new byte[is.available()];
			is.read(buf);
			is.close();
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(buf);
			KeyFactory kfac = KeyFactory.getInstance("RSA");
			PrivateKey pk = kfac.generatePrivate(spec);
			return pk;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static X509Certificate getCaCert()
	{
		try
		{
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new FileInputStream(publicCert));
			return x509Cert;
		} catch (CertificateException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getSignData() throws FileNotFoundException, IOException
	{
		Properties pro = new Properties();
		pro.load(new FileInputStream(file));
		
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < keys.length - 1; i++)
		{
			String tmp = pro.getProperty(keys[i]);
			
			sb.append(tmp);
		}
		
		return sb.toString();
	}
	
	
	public static void storeSignData2pro(String signData) throws FileNotFoundException, IOException
	{
		Properties pro = new Properties();
		pro.load(new FileInputStream(file));
		pro.setProperty(keys[keys.length - 1], signData);
		pro.store(new FileOutputStream(file), null);
	}
	
	
	public static String getVailData() throws FileNotFoundException, IOException
	{
		Properties pro = new Properties();
		pro.load(new FileInputStream(file));
		return pro.getProperty(keys[keys.length - 1]);
	}
	
	
	
}