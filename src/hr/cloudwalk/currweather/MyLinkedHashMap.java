package hr.cloudwalk.currweather;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class MyLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = 1L;
	private int myCapacity = 100;

	@Override
	public V put(K object, V value) {
		// TODO Auto-generated method stub
		if (size() > myCapacity) {
			Set<K> keys = keySet();
			Iterator<K> i = keys.iterator();
			i.next();
			i.remove();
		}
		return super.put(object, value);
	}

	public MyLinkedHashMap(int capacity) {
		super(capacity);
		this.myCapacity = capacity;
	}

}
