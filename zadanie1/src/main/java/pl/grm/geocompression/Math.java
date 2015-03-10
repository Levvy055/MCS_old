package pl.grm.geocompression;

import java.util.*;

public class Math {
	
	public static <T> List getDuplicate(Collection<T> list) {
		List<T> duplicatedObjects = new ArrayList<T>();
		Set<T> set = new HashSet<T>() {
			@Override
			public boolean add(T e) {
				if (contains(e)) {
					duplicatedObjects.add(e);
				}
				return super.add(e);
			}
		};
		for (T t : list) {
			set.add(t);
		}
		return duplicatedObjects;
	}
	
	public static <T> boolean hasDuplicate(Collection<T> list) {
		if (getDuplicate(list).isEmpty())
			return false;
		return true;
	}
}
