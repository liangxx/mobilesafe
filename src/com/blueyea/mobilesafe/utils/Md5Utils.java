package com.blueyea.mobilesafe.utils;

import java.security.MessageDigest;

public class Md5Utils {
	/**
	 * md5�����ַ�
	 * @param password
	 * @return  ���ܺ������
	 */
	public static String encode(String password){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			//���ܺ����Ϣ������result���档
			byte[] result = digest.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for(byte b : result){
				int number = b & 0xff ;//���� - 3 ;
				String str = Integer.toHexString(number);
				if(str.length()==1){
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			//�����ܷ���
			//can't reach
			return "";
		}
	}
}
