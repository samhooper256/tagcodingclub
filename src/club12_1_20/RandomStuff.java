package club12_1_20;

import java.util.Arrays;

public class RandomStuff {
	public static void main(String[] args) {
		
		boolean b = true;
		boolean p = true;
		dog:
		while(b) {
			while(p) {
				break dog;
			}
			System.out.println("This will not get printed");
		}
		System.out.println("done");
		
		if(true) {
			label:
				if(true) {
					break label;
				}
		}
		
		block:
		{
			System.out.println();
			break block;
			//
			//
			//
		}
		
		
		String[] arr = {"zefokef","aefeaf", "ceaf", "eaeff"};
		Arrays.sort(arr, (str1, str2) -> str1.length() - str2.length());
		
		System.out.println(Arrays.toString(arr));
		
	}
}
