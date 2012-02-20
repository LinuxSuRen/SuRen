package org.suren.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Properties;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Test
{

	private static final String[]	keys		= { "Product.name", "Product.version",
			"License.type", "License.expiry", "Server.macaddress", "Server.sno", "signature.data" };

	private static final String		file		= "c://users//suren//desktop//license.properties";
	private static final String		privateKey	= "c://users//suren//desktop//privateKey";
	private static final String		publicKey	= "c://users//suren//desktop//publicKey";

	private static BASE64Encoder	encoder		= new sun.misc.BASE64Encoder();
	private static BASE64Decoder	decoder		= new sun.misc.BASE64Decoder();

	public static void main(String[] args) throws Exception
	{
		keys();

		ObjectInputStream oisPrivate = new ObjectInputStream(new FileInputStream(new File(
				privateKey)));
		ObjectInputStream oisPublic = new ObjectInputStream(
				new FileInputStream(new File(publicKey)));

		sig(new File(file), (PrivateKey) oisPrivate.readObject());
		System.out.println(valid(new File(file), (PublicKey) oisPublic.readObject()));

		oisPrivate.close();
		oisPublic.close();
	}

	public static void keys() throws Exception
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair key = keyGen.generateKeyPair();

		ObjectOutputStream oosPrivate = new ObjectOutputStream(new FileOutputStream(new File(
				privateKey)));
		ObjectOutputStream oosPublic = new ObjectOutputStream(new FileOutputStream(new File(
				publicKey)));

		oosPrivate.writeObject(key.getPrivate());
		oosPublic.writeObject(key.getPublic());

		oosPrivate.close();
		oosPublic.close();
	}

	public static boolean valid(File file, PublicKey publicKey) throws Exception
	{
		Properties pro = new Properties();
		pro.load(new FileInputStream(file));

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < keys.length - 1; i++)
		{
			String tmp = pro.getProperty(keys[i]);

			sb.append(tmp);
		}

		Signature sig = Signature.getInstance("SHA1WithRSA");

		sig.initVerify(publicKey);
		sig.update(sb.toString().getBytes());

		String a = pro.getProperty(keys[keys.length - 1]);

		return sig.verify(decoder.decodeBuffer(a));
	}

	public static void sig(File file, PrivateKey privateKey) throws Exception
	{
		Properties pro = new Properties();
		pro.load(new FileInputStream(file));

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < keys.length - 1; i++)
		{
			String tmp = pro.getProperty(keys[i]);

			sb.append(tmp);
		}

		Signature sig = Signature.getInstance("SHA1WithRSA");

		sig.initSign(privateKey);
		sig.update(sb.toString().getBytes());

		byte[] sign = sig.sign();

		pro.setProperty(keys[keys.length - 1], encoder.encode(sign));

		pro.store(new FileOutputStream(file), null);
	}
}