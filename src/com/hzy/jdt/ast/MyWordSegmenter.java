package com.hzy.jdt.ast;
import java.util.*;

import java.io.*;

class Result {
	double cost;
	int k;
	public Result() {
		super();
	}
	void setCost(double co) {
		this.cost = co;
	}
	void setK(int ko) {
		this.k = ko;
	}
	double getCost(){
		return this.cost;
	}
	int getK() {
		return this.k;
	}
}

public class MyWordSegmenter {
	List<String> words;
	Map<String, Double> map;
	int maxWord;
	public MyWordSegmenter() {
		this.words = new ArrayList<String> ();
		this.map = new HashMap<String, Double> ();
		maxWord = 0;
		getWord();
		getMap();
	}
	//初始化为小写
	public String init(String s) {
		StringBuilder ans = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (Character.isUpperCase(s.charAt(i))) {
				ans.append(Character.toLowerCase(s.charAt(i)));
			}
			else {
				ans.append(s.charAt(i));
			}
		}
		return ans.toString();
	}
	public void getWord() {
		//根据相对路径加载words.txt
		InputStream is = this.getClass().getResourceAsStream("words.txt");
		InputStreamReader read = null;
		try {
			read = new InputStreamReader(is, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader((read));
		String s = "";
		try {
			while((s = br.readLine())!= null) {
				words.add(s);
				maxWord = Math.max(maxWord, s.length());
			}
			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
	}
	public void getMap() {
		int N = this.words.size();
		double x = 0;
		for(int i = 0; i < N; i++) {
			String kword = this.words.get(i);
			double value = Math.log((i+1)*(Math.log(N)));
			map.put(kword, value);
			x = Math.max(x, value);
		}
	}
	public Result bestMatch(int idx, List<Double> cost, String s) {
		Result result = new Result();
		double ans = 100000000.0;
		result.setCost(ans);
		result.setK(1);
		int mi = Math.max(0, idx-this.maxWord);
		for(int i = idx-1, k = 0; i >= mi; i--, k++) { 
			String key = init(s.substring(idx-k-1, idx));
			double c = cost.get(i);
			if(map.get(key) != null) {
				double wcost = map.get(key); 		
				if( c+ wcost < ans) {
					ans = Math.min(ans, c + wcost);
					result.setCost(ans);
					result.setK(k+1);
				}	
			}
		}
		return result;
	}
	public List<String> di_split(String s) {
		List<Double> cost = new ArrayList<Double> ();
		cost.add(0.0);
		for(int i = 1; i <= s.length(); i++) {
			Result c = bestMatch(i, cost, s);
			cost.add(c.getCost());
		}
		List<String> out = new ArrayList<String> ();
		int i = s.length();
		while (i > 0) {
			Result result = bestMatch(i, cost, s);
			double c = result.getCost();
			int k = result.getK();
			assert c == cost.get(i);
			boolean newToken = true;
			String now = init(s.substring(i-k, i));
			int endi = out.size();
			if( now.compareTo("'") != 0 && endi > 0 ) {
				String outend = out.get(endi-1);
				if(outend.compareTo("'s") == 0 || (Character.isDigit(s.charAt(i-1)) && Character.isDigit(outend.charAt(0)))) {
					String x = now;
					for(int j = 0; j < outend.length(); j++) {
						x += outend.charAt(j);						
					}
					out.set(endi-1, x);
					newToken = false;
				}
			}
			if (newToken) {
				out.add(now);				
			}
			i -= k;
		}
		Collections.reverse(out);
		return out;
	}
	//look here
	public List<String> split(String str) {
		String s = new String();
		List<String> ans = new ArrayList<String> ();
		for(int i = 0; i < str.length(); i++) {
			if(Character.isAlphabetic(str.charAt(i)) || Character.isDigit(str.charAt(i)) || str.charAt(i) == '_') {
				s += str.charAt(i);				
			}
			else {
				if(s.length() > 0) {
					ans.addAll(di_split(s));
				}
				s = "";
			}
		}
		if(s.length() > 0) {
			ans.addAll(di_split(s));
		}
		s = "";
		return ans;
	}
}

