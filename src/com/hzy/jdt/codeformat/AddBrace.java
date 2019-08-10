package com.hzy.jdt.codeformat;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.NotNull;

import com.hzy.antlr.listener.JavaBaseListener;
import com.hzy.antlr.listener.JavaParser;

//利用listener模式遍历语法树
public class AddBrace extends JavaBaseListener {
	TokenStreamRewriter rewriter;
	public AddBrace(TokenStream tokens) {
		rewriter = new TokenStreamRewriter(tokens);
	}
	@Override public void enterIfstatement(@NotNull JavaParser.IfstatementContext ctx) {
		if (ctx.statement().block() == null || ctx.statement().block().LBRACE() == null) {
			rewriter.insertBefore(ctx.start, "{");
			rewriter.insertAfter(ctx.stop, "}");
		}
	}
	@Override public void enterElsestatement(@NotNull JavaParser.ElsestatementContext ctx) {
		if (ctx.statement().IF() != null) return;
		if (ctx.statement().block() == null || ctx.statement().block().LBRACE() == null) {
			rewriter.insertBefore(ctx.start, "{");
			rewriter.insertAfter(ctx.stop, "}");
		}
	}
	@Override public void enterForstatement(@NotNull JavaParser.ForstatementContext ctx) {
		if (ctx.statement().block() == null || ctx.statement().block().LBRACE() == null) {
			rewriter.insertBefore(ctx.start, "{");
			rewriter.insertAfter(ctx.stop, "}");
		}
	}
	@Override public void enterWhilestatement(@NotNull JavaParser.WhilestatementContext ctx) {
		if (ctx.statement().block() == null || ctx.statement().block().LBRACE() == null) {
			rewriter.insertBefore(ctx.start, "{");
			rewriter.insertAfter(ctx.stop, "}");
		}
	}
	@Override public void enterDostatement(@NotNull JavaParser.DostatementContext ctx) {
		if (ctx.statement().block() == null || ctx.statement().block().LBRACE() == null) {
			rewriter.insertBefore(ctx.start, "{");
			rewriter.insertAfter(ctx.stop, "}");
		}
	}
}