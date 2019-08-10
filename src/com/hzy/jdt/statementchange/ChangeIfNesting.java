package com.hzy.jdt.statementchange;

import java.util.List;
import java.util.ArrayList;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import com.hzy.antlr.visitor.*;

public class ChangeIfNesting extends JavaBaseVisitor<Integer> {
	TokenStream tokens;
	TokenStreamRewriter rewriter;
	List<String> parameter; //记录合并后的条件表达式

	public ChangeIfNesting(TokenStream tokens) {
		this.tokens = tokens;
		rewriter = new TokenStreamRewriter(tokens);
		parameter = new ArrayList<String> ();
		parameter.add("");
	}
	
	//模拟栈的操作
	void push(String s) {
		parameter.add(s);
	}
	void pop() {
		parameter.remove(parameter.size()-1);
	}
	String top() {
		return parameter.get(parameter.size()-1);
	}

	@Override public Integer visitStatement(@NotNull JavaParser.StatementContext ctx) {
		//不是单if的情况全部忽略
		if (ctx.IF() == null || ctx.ELSE() != null) {
			visitChildren(ctx);
			return 0;
		}
		String s = top();
		//若后面没有大括号
		if (ctx.ifstatement().statement().block() == null) {
			//若后面跟着单if，则继续递归重构
			if (ctx.ifstatement().statement().IF() != null && ctx.ifstatement().statement().ELSE() == null) {
				Interval interval = new Interval(ctx.parExpression().start.getTokenIndex(), ctx.parExpression().stop.getTokenIndex());
				String t = rewriter.getText(interval);
				push(s + t + "&&");
				visitChildren(ctx);
				pop();
				rewriter.delete(ctx.IF().getSymbol());
				rewriter.replace(ctx.parExpression().start, ctx.parExpression().stop, "");
				return 0;
			}
			//否则视为当前if嵌套结构的尾部
			else {
				Interval interval = new Interval(ctx.parExpression().start.getTokenIndex(), ctx.parExpression().stop.getTokenIndex());
				String t = rewriter.getText(interval);
				push("");
				visitChildren(ctx);
				pop();
				if (s.length() != 0) {
					rewriter.replace(ctx.parExpression().start, ctx.parExpression().stop, "("+s+t+")");
				}
				return 0;
			}
		}
		//若后面有大括号
		else {
			//除去大括号，后面跟着单if，则继续递归重构
			if (ctx.ifstatement().statement().block().getChildCount()-2 == 1
					&& ctx.ifstatement().statement().block().blockStatement(0).statement().IF() != null
					&& ctx.ifstatement().statement().block().blockStatement(0).statement().ELSE() == null) {
				Interval interval = new Interval(ctx.parExpression().start.getTokenIndex(), ctx.parExpression().stop.getTokenIndex());
				String t = rewriter.getText(interval);
				push(s + t + "&&");
				visitChildren(ctx);
				pop();
				rewriter.delete(ctx.IF().getSymbol());
				rewriter.replace(ctx.parExpression().start, ctx.parExpression().stop, "");
				rewriter.delete(ctx.ifstatement().statement().block().LBRACE().getSymbol());
				rewriter.delete(ctx.ifstatement().statement().block().RBRACE().getSymbol());
				return 0;
			}
			//否则视为当前if嵌套结构的尾部
			else {
				Interval interval = new Interval(ctx.parExpression().start.getTokenIndex(), ctx.parExpression().stop.getTokenIndex());
				String t = rewriter.getText(interval);
				push("");
				visitChildren(ctx);
				pop();
				if (s.length() != 0) {
					rewriter.replace(ctx.parExpression().start, ctx.parExpression().stop, "("+s+t+")");					
				}
				return 0;
			}
		}
	}

}
