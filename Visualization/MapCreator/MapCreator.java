package MapCreator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MapCreator {
	private HashMap<Integer, String> symbols;
	private HashMap<Integer, Integer> scales;
	private String file = "map1.txt";
	
	public MapCreator() {
		symbols = new HashMap<Integer, String>();
		scales = new HashMap<Integer, Integer>();
		buildMaps(symbols, scales);
	}
	
	private void buildMaps(HashMap<Integer, String> symbols, 
			HashMap<Integer, Integer> scales){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while(br.ready()){
				StringBuilder symbol = new StringBuilder();
				StringBuilder index = new StringBuilder();
				StringBuilder scale = new StringBuilder();
				String line = br.readLine();
				int i = 0;
				// get symbol
				while(line.charAt(i) != '|'){
					symbol.append(line.charAt(i));
					i+=1;
				}
				i+=1;
				// skip
				while(line.charAt(i) != '|') i+=1;
				i+=1;
				// index
				while(line.charAt(i) != '|'){
					index.append(line.charAt(i));
					i+=1;
				}
				i+=1;
				// skip
				while(line.charAt(i) != '|') i+=1;
				i+=1;
				// skip
				while(line.charAt(i) != '|') i+=1;
				i+=1;
				// skip
				while(line.charAt(i) != '|') i+=1;
				i+=1;
				// skip
				while(line.charAt(i) != '|') i+=1;
				i+=1;
				// scale
				while(line.charAt(i) != '|'){
					scale.append(line.charAt(i));
					i+=1;
				}
				int symbolIndex = Integer.parseInt(index.toString());
				int priceScale = Integer.parseInt(scale.toString());
				symbols.put(symbolIndex, symbol.toString());
				scales.put(symbolIndex, priceScale);
				
				if(!br.ready() && file == "map1.txt") {
					System.out.println("enter");
					br.close();
					file = "map2.txt";
					br = new BufferedReader(new FileReader(file));
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public HashMap<Integer, String> getSymbolMap(){
		return symbols;
	}
	
	public HashMap<Integer, Integer> getScaleMap(){
		return scales;
	}
}
