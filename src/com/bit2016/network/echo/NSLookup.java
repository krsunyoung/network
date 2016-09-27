package com.bit2016.network.echo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.print(">");
		String inputValue = scan.nextLine();
		
		try{
		InetAddress[] inetaddresses = InetAddress.getAllByName(inputValue);
		
		for (InetAddress inetaddress : inetaddresses){
			System.out.println(inetaddress.getHostAddress());
		}
		
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		scan.close();
		
	}



}
