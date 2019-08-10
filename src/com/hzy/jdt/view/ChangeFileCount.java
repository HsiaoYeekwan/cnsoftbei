package com.hzy.jdt.view;

import java.util.List;

import com.hzy.jdt.ast.NewNode;

public class ChangeFileCount {
	private int fileNum;
	private int allNum;

	public ChangeFileCount(NewNode root) {
		fileNum = 0;
		allNum = 0;
		getDiff(root);
	}

	public void getDiff(NewNode root) {
		List<NewNode> childs = root.getChildren();
		if (childs == null || childs.size() == 0)
			return;
		for (NewNode child : childs) {
			if (child.getNewCode().length() != 0) {
				int cnt = getDifferent(child.getOldCode(), child.getNewCode());
				if (cnt != 0) {
					fileNum++;
					allNum += cnt;
				}
			}
			getDiff(child);
		}
	}

	public int getFileName() {
		return fileNum;
	}

	public int getAllNum() {
		return allNum;
	}

	private int getDifferent(String pre, String now) {
		int num = 0;
		String cntpre[] = pre.split("\n");
		String cntnow[] = now.split("\n");
		for (int i = 0; i <Math.min(cntpre.length, cntnow.length); i++) {
			if (!cntpre[i].equals(cntnow[i])) {
				num++;
			}
		}
		return num;

	}
}
