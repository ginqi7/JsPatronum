package com.qiqijin.jspatronum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;

public class Compressor {
	private AstNode astNode;

	/**
	 *
	 */
	public Compressor(AstNode astNode) {
        this.astNode = astNode; 
    }

    private String removeEnter(String source) {
        String newStr = "";
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) != '\n') {
                newStr += source.substring(i, i+1);
            }
        }
        return newStr;
	}

	private List<String> splitSource() {
        String source = this.astNode.toSource();
		source = this.removeEnter(source);
		String[] sources = source.split(" ");
		List<String> sourceList = new ArrayList<String>();
		sourceList.addAll(Arrays.asList(sources));
		Iterator<String> it = sourceList.iterator();
		while (it.hasNext()) {
			String sourceItem = it.next();
			if (sourceItem.length() == 0) {
				it.remove();
			}
		}
		return sourceList;
	}

	private boolean isNameChar(char c) {
        for (int i = 0; i < Build_in.varCharArray.length; i++) {
            if (c == Build_in.varCharArray[i]) {
                return true;
            }
        }
        return false;
	}

    private boolean isCanMerge(String beforItem, String item) {
        if (beforItem == null) {
            return true;
        }
        char beforLastChar = beforItem.charAt(beforItem.length()-1);
        char nowFirstChar = item.charAt(0);
		if (Build_in.notNameSymbol.contains(beforLastChar) || Build_in.notNameSymbol.contains(nowFirstChar)) {
            return true;
        }
        return false;
    }

	private String mergeSource(List<String> sourceList) {
        String source = "";
        String beforItem = null;
        for (String item : sourceList) {
			if (!isCanMerge(beforItem, item)) {
				source += " ";
			}
			source += item;
			beforItem = item;
		}
		return source;
	}

	public String compress() {
		List<String> sourceList = this.splitSource();
		String source = this.mergeSource(sourceList);
		return source;
	}
}
