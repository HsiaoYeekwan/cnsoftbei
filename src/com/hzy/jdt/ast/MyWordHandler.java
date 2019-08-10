package com.hzy.jdt.ast;

import java.util.List;

public class MyWordHandler {
	MyWordSegmenter myWordSegmenter;            //分词器
	MyWordHandler() {
		myWordSegmenter = new MyWordSegmenter();//加载分词器
	}
	//判断是否为字母
	boolean isLetter(char c) {
		if ('a' <= c && c <= 'z') return true;
		if ('A' <= c && c <= 'Z') return true;
		return false;
	}
	//判断是否为数字
	boolean isDigit(char c) {
		if ('0' <= c && c <= '9') return true;
		return false;
	}
	boolean isUnderLine(char c) {
		if (c == '_') return true;
		return false;
	}
	//首字母大写
	String firstUpper(String s) {
		StringBuilder myBuilder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				if (Character.isLowerCase(s.charAt(i))) {
					myBuilder.append(Character.toUpperCase(s.charAt(i)));
				}
				else {
					myBuilder.append(s.charAt(i));
				}
			}
			else {
				if (Character.isUpperCase(s.charAt(i))) {
					myBuilder.append(Character.toLowerCase(s.charAt(i)));
				}
				else {
					myBuilder.append(s.charAt(i));
				}
			}
		}
		return myBuilder.toString();
	}
	//全部字母大写
	String allUpper(String s) {
		StringBuilder myBuilder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (Character.isLowerCase(s.charAt(i))) {
				myBuilder.append(Character.toUpperCase(s.charAt(i)));
			}
			else myBuilder.append(s.charAt(i));
		}
		return myBuilder.toString();
	}
	//全部字母小写
	String allLower(String s) {
		StringBuilder myBuilder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (Character.isUpperCase(s.charAt(i))) {
				myBuilder.append(Character.toLowerCase(s.charAt(i)));
			}
			else myBuilder.append(s.charAt(i));
		}
		return myBuilder.toString();
	}
	//初步检测命名是否合乎规范，若符合规范则返回"ok"，若不符合规范则返回错误信息
	String check(String s) {
		//检测命名是否为空
		if (s.length() == 0) {
			return "名字不能为空";
		}
		//检测命名中是否存在字母、数字、下划线以外的字符
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isLetter(c) || isDigit(c) || isUnderLine(c)) continue;
			return "名字中只能包含字母、数字、下划线";
		}
		//检测命名中是否存在字母
		boolean ok = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isLetter(c)) {
				ok = true;
				break;
			}
		}
		if (ok == false) {
			return "名字中不能没有字母";
		}
		//检查是否有前导下划线
		if (isUnderLine(s.charAt(0))) {
			return "名字中不推荐有前导下划线";
		}
		//检测是否有数字出现在名字中间
		int start = 0, end = s.length()-1;
		while (!isLetter(s.charAt(start))) {
			start++;
		}
		while (!isLetter(s.charAt(end))) {
			end--;
		}
		for (int i = start; i <= end; i++) {
			if (isDigit(s.charAt(i))) {
				return "名字中间不推荐有数字";
			}
		}
		//检测是否有后导下划线
		if (isUnderLine(s.charAt(s.length()-1))) {
			return "名字中不推荐有后导下划线";
		}
		return "ok";
	}
	//包名重构
	String [] renamePackage(String s) {
		//重命名成功后新名字保存在第一个字符串中，第二个字符串为空
		//重命名失败后第一个字符串为原名，第二个字符串保存错误信息
		//第三个字符串是让前端显示的颜色
		String [] ans = new String [] {s, "包名中的字母全部重构为小写", "default"};
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isLetter(c) || c == '.') continue;
			ans[1] = "包名不能包含字母和'.'以外的字符";
			ans[2] = "blue";
			return ans;
		}
		StringBuilder myBuilder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (Character.isUpperCase(s.charAt(i))) {
				myBuilder.append(Character.toLowerCase(s.charAt(i)));
			}
			else {
				myBuilder.append(s.charAt(i));
			}
		}
		ans[0] = myBuilder.toString();
		if (!ans[0].equals(s)) {
			ans[2] = "purple";
		}
		return ans;
	}
	//源文件名重构
	String [] renameCompilationUnit(String s) {
		//重命名成功后新名字保存在第一个字符串中，第二个字符串为空
		//重命名失败后第一个字符串为原名，第二个字符串保存错误信息
		//第三个字符串是让前端显示的颜色
		String [] ans = new String [] {s, "源文件名按照 UpperCamelCase规则命名", "default"};
		String checkAns = check(s);
		if (!checkAns.equals("ok")) {
			ans[1] = checkAns;
			ans[2] = "blue";
			return ans;
		}
		
		StringBuilder myBuilder = new StringBuilder();
		List<String> stringList = myWordSegmenter.split(s);
		for (int i = 0; i < stringList.size(); i++) {
			String t = stringList.get(i);
			if (t.length() == 0) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (isDigit(t.charAt(0)) || isUnderLine(t.charAt(0))) {
				myBuilder.append(t);
				continue;
			}
			if (t.length() == 1 && s.length() != 1) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			myBuilder.append(firstUpper(t));
		}
		ans[0] = myBuilder.toString();
		if (!ans[0].equals(s)) {
			ans[2] = "purple";
		}
		return ans;
	}
	
	//类型名重构
	//普通类型名重构
	String [] renameNormalType(String s) {
		//重命名成功后新名字保存在第一个字符串中，第二个字符串为空
		//重命名失败后第一个字符串为原名，第二个字符串保存错误信息
		//第三个字符串是让前端显示的颜色
		String [] ans = new String [] {s, "普通的类、接口、枚举命按照UpperCamelCase规则命名", "default"};
		String checkAns = check(s);
		if (!checkAns.equals("ok")) {
			ans[1] = checkAns;
			ans[2] = "blue";
			return ans;
		}
		
		StringBuilder myBuilder = new StringBuilder();
		List<String> stringList = myWordSegmenter.split(s);
		for (int i = 0; i < stringList.size(); i++) {
			String t = stringList.get(i);
			if (t.length() == 0) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (isDigit(t.charAt(0)) || isUnderLine(t.charAt(0))) {
				myBuilder.append(t);
				continue;
			}
			if (t.length() == 1 && s.length() != 1) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			myBuilder.append(firstUpper(t));
		}
		ans[0] = myBuilder.toString();
		if (!ans[0].equals(s)) {
			ans[2] = "purple";
		}
		return ans;
	}
	//泛型重构
	String [] renameSpecialType(String s) {
		//重命名成功后新名字保存在第一个字符串中，第二个字符串为空
		//重命名失败后第一个字符串为原名，第二个字符串保存错误信息
		//第三个字符串是让前端显示的颜色
		String [] ans = new String [] {s, "泛型类名按照UpperCamelCase+T规则命名", "default"};
		if (s.length() != 0 && s.charAt(s.length()-1) == 'T') return ans;
		String checkAns = check(s);
		if (!checkAns.equals("ok")) {
			ans[1] = checkAns;
			ans[2] = "blue";
			return ans;
		}
		
		StringBuilder myBuilder = new StringBuilder();
		List<String> stringList = myWordSegmenter.split(s);
		for (int i = 0; i < stringList.size(); i++) {
			String t = stringList.get(i);
			if (t.length() == 0) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (isDigit(t.charAt(0)) || isUnderLine(t.charAt(0))) {
				myBuilder.append(t);
				continue;
			}
			if (t.length() == 1 && s.length() != 1) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			myBuilder.append(firstUpper(t));
		}
		ans[0] = myBuilder.toString();
		if (ans[0].length() != 1) ans[0] += "T";
		if (!ans[0].equals(s)) {
			ans[2] = "purple";
		}
		return ans;
	}
	//方法名重构
	String [] renameMethod(String s) {
		//重命名成功后新名字保存在第一个字符串中，第二个字符串为空
		//重命名失败后第一个字符串为原名，第二个字符串保存错误信息
		//第三个字符串是让前端显示的颜色
		String [] ans = new String [] {s, "方法名按照lowerCamelCase规则命名", "default"};
		String checkAns = check(s);
		if (!checkAns.equals("ok")) {
			ans[1] = checkAns;
			ans[2] = "blue";
			return ans;
		}
		
		StringBuilder myBuilder = new StringBuilder();
		List<String> stringList = myWordSegmenter.split(s);
		int flag = 0;
		for (int i = 0; i < stringList.size(); i++) {
			String t = stringList.get(i);
			if (t.length() == 0) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (isDigit(t.charAt(0)) || isUnderLine(t.charAt(0))) {
				myBuilder.append(t);
				flag = 0;
				continue;
			}
			if (t.length() == 1 && s.length() != 1) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (flag == 0) myBuilder.append(allLower(t));
			else myBuilder.append(firstUpper(t));
			flag++;
		}
		ans[0] = myBuilder.toString();
		if (!ans[0].equals(s)) {
			ans[2] = "purple";
		}
		return ans;
	}
	//常量名重构
	String [] renameConstance(String s) {
		//重命名成功后新名字保存在第一个字符串中，第二个字符串为空
		//重命名失败后第一个字符串为原名，第二个字符串保存错误信息
		//第三个字符串是让前端显示的颜色
		String [] ans = new String [] {s, "常量名中的字母全部重构为大写", "default"};
		String checkAns = check(s);
		if (!checkAns.equals("ok")) {
			ans[1] = checkAns;
			ans[2] = "blue";
			return ans;
		}
		
		StringBuilder myBuilder = new StringBuilder();
		List<String> stringList = myWordSegmenter.split(s);
		for (int i = 0; i < stringList.size(); i++) {
			String t = stringList.get(i);
			if (t.length() == 0) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (isDigit(t.charAt(0)) || isUnderLine(t.charAt(0))) {
				myBuilder.append(t);
				continue;
			}
			myBuilder.append(allUpper(t));
		}
		ans[0] = myBuilder.toString();
		if (!ans[0].equals(s)) {
			ans[2] = "purple";
		}
		return ans;
	}
	//成员变量名重构
	String [] renameField(String s) {
		//重命名成功后新名字保存在第一个字符串中，第二个字符串为空
		//重命名失败后第一个字符串为原名，第二个字符串保存错误信息
		//第三个字符串是让前端显示的颜色
		String [] ans = new String [] {s, "成员变量名按照lowerCamelCase规则命名", "default"};
		String checkAns = check(s);
		if (!checkAns.equals("ok")) {
			ans[1] = checkAns;
			ans[2] = "blue";
			return ans;
		}
		
		StringBuilder myBuilder = new StringBuilder();
		List<String> stringList = myWordSegmenter.split(s);
		int flag = 0;
		for (int i = 0; i < stringList.size(); i++) {
			String t = stringList.get(i);
			if (t.length() == 0) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (isDigit(t.charAt(0)) || isUnderLine(t.charAt(0))) {
				myBuilder.append(t);
				flag = 0;
				continue;
			}
			if (t.length() == 1 && s.length() != 1) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (flag == 0) myBuilder.append(allLower(t));
			else myBuilder.append(firstUpper(t));
			flag++;
		}
		ans[0] = myBuilder.toString();
		if (!ans[0].equals(s)) {
			ans[2] = "purple";
		}
		return ans;
	}
	//参数名重构
	String [] renameParameter(String s) {
		//重命名成功后新名字保存在第一个字符串中，第二个字符串为空
		//重命名失败后第一个字符串为原名，第二个字符串保存错误信息
		//第三个字符串是让前端显示的颜色
		String [] ans = new String [] {s, "参数名按照lowerCamelCase规则命名", "default"};
		String checkAns = check(s);
		if (!checkAns.equals("ok")) {
			ans[1] = checkAns;
			ans[2] = "blue";
			return ans;
		}
		
		StringBuilder myBuilder = new StringBuilder();
		List<String> stringList = myWordSegmenter.split(s);
		int flag = 0;
		for (int i = 0; i < stringList.size(); i++) {
			String t = stringList.get(i);
			if (t.length() == 0) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (isDigit(t.charAt(0)) || isUnderLine(t.charAt(0))) {
				myBuilder.append(t);
				flag = 0;
				continue;
			}
			if (t.length() == 1 && s.length() != 1) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (flag == 0) myBuilder.append(allLower(t));
			else myBuilder.append(firstUpper(t));
			flag++;
		}
		ans[0] = myBuilder.toString();
		if (!ans[0].equals(s)) {
			ans[2] = "purple";
		}
		return ans;
	}
	//局部变量名重构
	String [] renameLocalVariable(String s) {
		//重命名成功后新名字保存在第一个字符串中，第二个字符串为空
		//重命名失败后第一个字符串为原名，第二个字符串保存错误信息
		//第三个字符串是让前端显示的颜色
		String [] ans = new String [] {s, "局部变量名按照lowerCamelCase规则命名", "default"};
		String checkAns = check(s);
		if (!checkAns.equals("ok")) {
			ans[1] = checkAns;
			ans[2] = "blue";
			return ans;
		}
		
		StringBuilder myBuilder = new StringBuilder();
		List<String> stringList = myWordSegmenter.split(s);
		int flag = 0;
		for (int i = 0; i < stringList.size(); i++) {
			String t = stringList.get(i);
			if (t.length() == 0) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (isDigit(t.charAt(0)) || isUnderLine(t.charAt(0))) {
				myBuilder.append(t);
				flag = 0;
				continue;
			}
			if (t.length() == 1 && s.length() != 1) {
				ans[1] = "名字中的单词拼写可能存在问题，也可能是因为我们的词库不全无法处理";
				ans[2] = "blue";
				return ans;
			}
			if (flag == 0) myBuilder.append(allLower(t));
			else myBuilder.append(firstUpper(t));
			flag++;
		}
		ans[0] = myBuilder.toString();
		if (!ans[0].equals(s)) {
			ans[2] = "purple";
		}
		return ans;
	}
}
