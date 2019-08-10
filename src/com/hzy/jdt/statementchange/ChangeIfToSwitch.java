package com.hzy.jdt.statementchange;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;

import com.hzy.antlr.visitor.*;

public class ChangeIfToSwitch extends JavaBaseVisitor<Integer> {
	TokenStream tokens;
	TokenStreamRewriter rewriter;
	List<Integer> parameter1;//遍历的if-else结构中的if、else if、else的总数量
	List<String> parameter2; //遍历的if-else结构涉及到的变量名（要唯一）
	String name;             //与judge函数配合使用，提取条件表达式涉及到的变量名
	String value;            //与judge函数配合使用，提取条件表达式涉及到的变量值
	
	public ChangeIfToSwitch(TokenStream tokens) {
		this.tokens = tokens;
		rewriter = new TokenStreamRewriter(tokens);
		parameter1 = new ArrayList<Integer> ();
		parameter2 = new ArrayList<String> ();
		parameter1.add(0);
		parameter2.add("");
	}
	
	//模拟栈的操作
	void push1(int x) {
		parameter1.add(x);
	}
	void pop1() {
		parameter1.remove(parameter1.size()-1);
	}
	int top1() {
		return parameter1.get(parameter1.size()-1);
	}
	void push2(String x) {
		parameter2.add(x);
	}
	void pop2() {
		parameter2.remove(parameter2.size()-1);
	}
	String top2() {
		return parameter2.get(parameter2.size()-1);
	}
	
	//判断当前条件表达式是否满足if转switch的条件，即只涉及一个char或int类型的变量，且这个变量唯一
	boolean judge(String s, int count, String variable) {
		int p = 0;
		if (s.charAt(p) != '(') return false;
		p++;
		for (; p < s.length(); p++) {
			if (!Character.isWhitespace(s.charAt(p))) break;
		}
		if (p == s.length()) return false;
		if (!Character.isLowerCase(s.charAt(p)) && !Character.isUpperCase(s.charAt(p))) return false;
		name = "";
		for (; p < s.length(); p++) {
			if (!Character.isLowerCase(s.charAt(p)) && !Character.isUpperCase(s.charAt(p))
				&& !Character.isDigit(s.charAt(p)) && s.charAt(p) != '_') break;
			name += s.charAt(p);
		}
		if (count != 0) {
			if (!name.equals(variable)) return false;
		}
		for (; p < s.length(); p++) {
			if (!Character.isWhitespace(s.charAt(p))) break;
		}
		if (p == s.length()) return false;
		for (int i = 0; i < 2 && p < s.length(); i++, p++) {
			if (s.charAt(p) != '=') return false;
		}
		for (; p < s.length(); p++) {
			if (!Character.isWhitespace(s.charAt(p))) break;
		}
		if (p == s.length()) return false;
		if (!Character.isDigit(s.charAt(p)) && s.charAt(p) != '\'') return false;
		value = "";
		if (Character.isDigit(s.charAt(p))) {
			for (; p < s.length(); p++) {
				if (!Character.isDigit(s.charAt(p))) break;
				value += s.charAt(p);
			}
		}
		else {
			p++;
			value += "\'";
			value += s.charAt(p);
			value += "\'";
			p++;
			if (s.charAt(p) != '\'') return false;
			p++;
		}
		for (; p < s.length(); p++) {
			if (!Character.isWhitespace(s.charAt(p))) break;
		}
		if (p != s.length() - 1 || s.charAt(p) != ')') return false;
		return true;
	}

	//去除大括号
	String removeBrace(String s) {
		int p1 = 0, p2 = s.length()-1;
		for (; p1 < s.length(); p1++) {
			if (!Character.isWhitespace(s.charAt(p1))) break;
		}
		if (p1 == s.length() || s.charAt(p1) != '{') return s;
		for (; p2 >= 0; p2--) {
			if (!Character.isWhitespace(s.charAt(p2))) break;
		}
		if (p2 < 0 || s.charAt(p2) != '}') return s;
		return s.substring(p1+1, p2);
	}
	
	@Override public Integer visitStatement(@NotNull JavaParser.StatementContext ctx) {
		//忽略非if的情况
		if (ctx.IF() == null) {
			visitChildren(ctx);
			return 0;
		}
		//若为单if语句，则视为当前if-else结构的结尾
		if (ctx.ELSE() == null) {
			int count = top1();
			String variable = top2();
			
			//继续向下递归查找新的if-else结构进行重构
			push1(0); push2("");
			visitChildren(ctx);
			pop1(); pop2();
			
			//if、else if、else语句总数大于3才能重构
			if (count + 1 > 3) {
				String s = ctx.parExpression().getText();
				//判断条件表达式是否满足重构条件
				if (judge(s, count, variable)) {
					rewriter.replace(ctx.IF().getSymbol(), "case ");
					rewriter.replace(ctx.parExpression().start, ctx.parExpression().stop, value + ":");
					Interval interval = new Interval(ctx.ifstatement().start.getTokenIndex(), ctx.ifstatement().stop.getTokenIndex());
					rewriter.replace(ctx.ifstatement().start, ctx.ifstatement().stop, removeBrace(rewriter.getText(interval)) + "break;");
					return 1;
				}
			}
			return 0;
		}
		//若为if else，则还需要进一步判断
		else {
			int count = top1();
			String variable = top2();
			
			push1(0); push2("");
			visitChildren(ctx);
			pop1(); pop2();
			
			String s = ctx.parExpression().getText();
			//判断条件表达式是否满足重构条件
			if (judge(s, count, variable)) {
				//若else后直接跟if，则说明构成了else if的情况，应继续向下递归重构
				if (ctx.elsestatement().statement().IF() != null) {
					String curname = name;
					String curvalue = value;
					
					//应继续向下递归重构
					push1(count+1); push2(name);
					int flag = visit(ctx.elsestatement());
					pop1(); pop2();
					
					//若回溯回来的值不为0，则说明当前语句可以重构
					if (flag != 0) {
						rewriter.replace(ctx.IF().getSymbol(), "case ");
						rewriter.replace(ctx.parExpression().start, ctx.parExpression().stop, curvalue + ":");
						Interval interval = new Interval(ctx.ifstatement().start.getTokenIndex(), ctx.ifstatement().stop.getTokenIndex());
						rewriter.replace(ctx.ifstatement().start, ctx.ifstatement().stop, removeBrace(rewriter.getText(interval)) + "break;");
						rewriter.replace(ctx.ELSE().getSymbol(), "");
						if (count == 0) {
							rewriter.insertBefore(ctx.start, "switch (" + curname + "){");
							rewriter.insertAfter(ctx.stop, "}");
						}
						return 1;
					}
					return 0;
				}
				//若else后不直接跟if，则视为当前if-else结构的结尾
				else {
					String curvalue = value;
					
					//继续向下递归查找新的if-else结构进行重构
					push1(0); push2("");
					visit(ctx.elsestatement());
					pop1(); pop2();
					
					//if、else if、else语句总数大于3才能重构
					if (count + 2 > 3) {
						rewriter.replace(ctx.IF().getSymbol(), "case ");
						rewriter.replace(ctx.parExpression().start, ctx.parExpression().stop, curvalue + ":");
						Interval interval = new Interval(ctx.ifstatement().start.getTokenIndex(), ctx.ifstatement().stop.getTokenIndex());
						rewriter.replace(ctx.ifstatement().start, ctx.ifstatement().stop, removeBrace(rewriter.getText(interval)) + "break;");
						rewriter.replace(ctx.ELSE().getSymbol(), "default:");
						interval = new Interval(ctx.elsestatement().start.getTokenIndex(), ctx.elsestatement().stop.getTokenIndex());
						rewriter.replace(ctx.elsestatement().start, ctx.elsestatement().stop, removeBrace(rewriter.getText(interval)) + "break;");
						return 1;
					}
					return 0;
				}
			}
			//当前条件表达式不满足重构条件则终止当前if-else结构的重构，继续向下递归查找新的if-else结构进行重构
			else {
				push1(0); push2("");
				visit(ctx.elsestatement());
				pop1(); pop2();
				return 0;
			}
		}
	}
}
