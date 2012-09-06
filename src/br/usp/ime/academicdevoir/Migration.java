package br.usp.ime.academicdevoir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Migration {

	public static void main(String[] args) throws IOException  {
		BufferedReader io;
		String str = "";
		try {
			io = new BufferedReader(new FileReader("/Users/laeciofreitas/Desktop/data.txt"));
			BufferedWriter in = new BufferedWriter(new FileWriter("/Users/laeciofreitas/Desktop/migration.sql"));
			while (io.ready()) {
				str = io.readLine();
				str = str.replaceAll("&quot;", "\"");
				str = str.replaceAll("<br>", "");
				str = str.replaceAll("<BR>", "");
				str = str.replaceAll("&#39;", "'");
				str = str.replaceAll("&#63;", "?");
				str = str.replaceAll("&minus;", "-");
				str += "\n";
				System.out.println(str);
				in.write(str);
			}
			in.close();
			io.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
