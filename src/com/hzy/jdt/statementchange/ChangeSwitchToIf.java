package com.hzy.jdt.statementchange;

import java.util.List;
import java.util.ArrayList;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import com.hzy.antlr.visitor.*;
public class ChangeSwitchToIf extends JavaBaseVisitor<Integer> {
	TokenStream tokens;
	TokenStreamRewriter rewriter;
	List<Integer> parameter1; //判断当前语句是否能重构
	List<Integer> parameter2; //判断当前case是第几个case
	List<String> parameter3;  //记录switch涉及的变量名

	public ChangeSwitchToIf(TokenStream tokens) {
		this.tokens = tokens;
		rewriter = new TokenStreamRewriter(tokens);
		parameter1 = new ArrayList<Integer> ();
		parameter2 = new ArrayList<Integer> ();
		parameter3 = new ArrayList<String> ();
		parameter1.add(0);
		parameter2.add(0);
		parameter3.add("");
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
	void push2(int x) {
		parameter2.add(x);
	}
	void pop2() {
		parameter2.remove(parameter2.size()-1);
	}
	int top2() {
		return parameter2.get(parameter2.size()-1);
	}
	void push3(String s) {
		parameter3.add(s);
	}
	void pop3() {
		parameter3.remove(parameter3.size()-1);
	}
	String top3() {
		return parameter3.get(parameter3.size()-1);
	}

	//去除break
	String removeBreak(String s) {
		String suffix = new String();
		int tail = s.length() - 1;
		for (; tail >= 0; tail--) {
			if (s.charAt(tail) == '}') suffix += "}";
			if (s.charAt(tail) == ';') break;
		}
		if (tail < 0) return s;
		tail--;
		for (; tail >= 0; tail--) {
			if (!Character.isWhitespace(s.charAt(tail))) break;
		}
		if (tail - 4 < 0) return s;
		if (s.charAt(tail) == 'k' && s.charAt(tail-1) == 'a' && s.charAt(tail-2) == 'e'
				&& s.charAt(tail-3) == 'r' && s.charAt(tail-4) == 'b') return s.substring(0, tail-4) + suffix;
		else return s;
	}
	
	@Override
	public Integer visitStatement(JavaParser.StatementContext ctx) {
		//忽略非switch的情况
		if (ctx.SWITCH() == null) {
			visitChildren(ctx);
			return 0;
		}
		int count = ctx.switchBlockStatementGroup().size();
		//只有当case、default的数量小于等于三的时候才能重构
		if (count <= 3) {
			push1(1); push2(0); push3(ctx.parExpression().expression().getText());
			visitChildren(ctx);
			pop1(); pop2(); pop3();
			
			rewriter.delete(ctx.SWITCH().getSymbol());
			rewriter.replace(ctx.parExpression().start, ctx.parExpression().stop, "");
			rewriter.delete(ctx.LBRACE().getSymbol());
			rewriter.delete(ctx.RBRACE().getSymbol());
			return 0;
		}
		//否则结束当前switch结构的重构，继续向下遍历新的switch结构进行重构
		else {
			push1(0); push2(0); push3("");
			visitChildren(ctx);
			pop1(); pop2(); pop3();
			return 0;
		}
		
	}

	@Override public Integer visitSwitchBlockStatementGroup(@NotNull JavaParser.SwitchBlockStatementGroupContext ctx) {
		int flag = top1();
		int count = top2();
		String name = top3();
		
		//继续向下递归重构
		pop2();
		push2(count + 1);
		visitChildren(ctx);
		
		//flag为0表示当前语句不满足重构条件
		if (flag == 0) return 0;
		
		//case
		if (ctx.switchLabel().CASE() != null) {
			if (count == 0) {
				rewriter.replace(ctx.switchLabel().CASE().getSymbol(), "if (" + name + " ==");
			}
			else {
				rewriter.replace(ctx.switchLabel().CASE().getSymbol(), "else if (" + name + " ==");
			}
			rewriter.replace(ctx.switchLabel().COLON().getSymbol(), ") {");
			rewriter.insertAfter(ctx.stop, "}");
			int end = ctx.blockStatement().size()-1;
			if (end >= 0) {
				Interval interval = new Interval(ctx.blockStatement(end).start.getTokenIndex(), ctx.blockStatement(end).stop.getTokenIndex());
				rewriter.replace(ctx.blockStatement(end).start, ctx.blockStatement(end).stop, removeBreak(rewriter.getText(interval)));
			}
		}
		//default
		else if (ctx.switchLabel().DEFAULT() != null) {
			rewriter.replace(ctx.switchLabel().DEFAULT().getSymbol(), "else");
			rewriter.replace(ctx.switchLabel().COLON().getSymbol(), "{");
			rewriter.insertAfter(ctx.stop, "}");
			int end = ctx.blockStatement().size()-1;
			if (end >= 0) {
				Interval interval = new Interval(ctx.blockStatement(end).start.getTokenIndex(), ctx.blockStatement(end).stop.getTokenIndex());
				rewriter.replace(ctx.blockStatement(end).start, ctx.blockStatement(end).stop, removeBreak(rewriter.getText(interval)));
			}
		}
		return 0;
	}
}
