package com.bit2016.network.echo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

	
		try {
			while (true) {
				System.out.print(">");
				String inputValue = scan.nextLine();
				if (inputValue.equals("exit") == true) {
					
					 break;
				}
				InetAddress[] inetaddresses = InetAddress.getAllByName(inputValue);
				
				for (InetAddress inetaddress : inetaddresses) {
					System.out.println(inputValue + " : " + inetaddress.getHostAddress());

				}
				
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		scan.close();

	}

}
