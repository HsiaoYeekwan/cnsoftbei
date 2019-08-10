package com.hzy.jdt.statementchange;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.NotNull;
import com.hzy.antlr.visitor.*;

public class ChangeForToWhile extends JavaBaseVisitor<Integer> {
	TokenStream tokens;
	TokenStreamRewriter rewriter;

	public ChangeForToWhile(TokenStream tokens) {
		this.tokens = tokens;
		rewriter = new TokenStreamRewriter(tokens);
	}
	@Override
    public Integer visitStatement(@NotNull JavaParser.StatementContext ctx) {
		//不重构增强for
		if (ctx.FOR() != null && ctx.forControl().enhancedForControl() == null) {
			//当第一控制部分和第三控制部分缺失时可重构
			if (ctx.forControl().forInit() == null && ctx.forControl().forUpdate == null) {
			    rewriter.replace(ctx.FOR().getSymbol(), "while");
				rewriter.replace(ctx.forControl().SEMI(0).getSymbol(), "");
				rewriter.replace(ctx.forControl().SEMI(1).getSymbol(), "");
			}
			//当只有第一控制部分缺失可重构
			else if (ctx.forControl().forInit() == null && ctx.forControl().expression() != null
					&& ctx.forControl().forUpdate != null) {
				if (ctx.forstatement().statement().block() == null) {
					rewriter.insertBefore(ctx.forstatement().start, "{");
				}
				String s = ctx.forControl().forUpdate.getText();
				rewriter.replace(ctx.FOR().getSymbol(), "while");
				rewriter.replace(ctx.forControl().SEMI(0).getSymbol(), "");
				rewriter.replace(ctx.forControl().SEMI(1).getSymbol(), "");
				rewriter.replace(ctx.forControl().forUpdate.start, ctx.forControl().forUpdate.stop, "");
				if (ctx.forstatement().statement().block() == null) {
					rewriter.insertAfter(ctx.forstatement().stop, s + ";}");
				}
				else rewriter.insertBefore(ctx.forstatement().stop, s + ";");
			}
		}
		return visitChildren(ctx);
	}

}
