package com.hzy.jdt.view;

import com.hzy.jdt.ast.OldNode;

public class ErrorMessage {
	private String error, warning;

	ErrorMessage() {
		error = new String();
		warning = new String();
		error = "";
		warning = "";
	}

	public String getError() {
		return error;
	}

	public String getWarning() {
		return warning;
	}

	public void getMessage(OldNode root) {
		if (root.getEdges() == null)
			return;
		for (int i = 0; i < root.getEdges().size(); i++) {
			OldNode a = root.getEdges().get(i);
			getMessage(a);
			String namea = a.getName();
			if (a.tag() == true && a.getChecked() == true)
				namea = a.getRename();
			for (int j = i + 1; j < root.getEdges().size(); j++) {
				OldNode b = root.getEdges().get(j);
				String nameb = b.getName();
				// getMessage(b);
				if (b.tag() == true && b.getChecked() == true)
					nameb = b.getRename();
				if (namea.equals(nameb) && a.getType() == b.getType()) {
					if (a.tag() == false && b.tag() == false) // 不能重命名
						continue;
					if (a.getRename().equals(a.getName()) && b.getRename().equals(b.getName())) // 原来就冲突
						continue;
					if (b.getChecked() == false && a.getChecked() == false) // 原名相同，不重构，没冲突
						continue;
					if (a.getType().equals("localvariable") || a.getType().equals("method")) {
						warning += a.getPath() + "下将存在两个" + a.getType() + " " + namea + "\n\n\r";
					} else {
						error += a.getPath() + "下存在两个冲突的" + a.getType() + " " + namea + "\n\n\r";
					}
				}
			}
		}

	}
}
