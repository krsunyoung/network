package com.bit2016.network.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			//it 주소정보가 들어있음.  local-나자신 호스트, remote-나랑 통신하고 있는 호스트  / 호스트는 장비 
			String hostName = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			
			System.out.println( hostName + ": "+hostAddress);
			
			byte[] addresses = inetAddress.getAddress(); //	192 / 168 / 1 / 2 이렇게 저장
			
			for(byte address : addresses){
				System.out.print( address & 0x000000ff);   
//				System.out.print((int)address); 
				//1byte가 그대로 가는것이 아니고 부호 비트는 맨앞으로 이동해서 음수가 변화가 없다.
				//위와 같이 변경해줘야 제대로된 IP가 나온다. 
				
				System.out.print(".");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
