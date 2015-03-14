package pl.grm.geocompression;

import java.util.*;

public class ValuePositions extends TreeMap<Long, Byte> {
	private static final long	serialVersionUID	= 1L;
	
	public Collection<String> toStringFullList() {
		SortedSet<String> set = new TreeSet<>();
		Iterator<Long> it = keySet().iterator();
		while (it.hasNext()) {
			Long index = it.next();
			Byte pos = get(index);
			set.add("index: ".concat(index + " P: " + pos));
		}
		return set;
	}
	
	public String toSimplifiedString() {
		String string = "";
		Iterator<Long> it = keySet().iterator();
		while (it.hasNext()) {
			Long index = it.next();
			Byte pos = get(index);
			string = string.concat("i" + index + "p" + pos);
		}
		return string.concat("e");
	}
}
