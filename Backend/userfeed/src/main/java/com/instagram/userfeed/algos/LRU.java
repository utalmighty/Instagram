package com.instagram.userfeed.algos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LRU {
	
	private int size;
	private Map<String, Node> map;
	private DoublyLinkedList list;
	
	public LRU() {
		this(20);
	} 
	
	public LRU(int maxSize) {
		size = maxSize;
		map = new HashMap<>(maxSize);
		list = new DoublyLinkedList(maxSize);
	}
	
	public void add(String data) {
		if (map.containsKey(data)) {
			list.useNode(map.get(data));
			return;
		}
		if (size == list.getSize()) {
			Node evicted = list.getTail();
			map.remove(evicted.getData());
		}
		Node node = new Node(data);
		map.put(data, node);
		list.add(node);
	}
	
	public List<String> getData(){
		return list.getData();
	}
}
