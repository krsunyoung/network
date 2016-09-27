package com.bit2016.network.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NSLookup {

	public static void main(String[] args) {
		String hostname ="www.naver.com";
		
		try {
			//도메인의 IP주소를 찾아줌. 
			InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
			for(InetAddress inetAddress : inetAddresses){
				System.out.println(inetAddress.getHostAddress());
			}
		} catch (UnknownHostException e) {
		
			e.printStackTrace();
		}
	}

}
