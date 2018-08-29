package de.syscy.kageban.common.util;

import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.text.ParsePosition;

@UtilityClass
public class Util {
	public static boolean isNumber(String string) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(string, pos);

		return string.length() == pos.getIndex();
	}
}