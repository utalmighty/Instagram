package com.instagram.userfeed.algos;

import java.util.ArrayList;
import java.util.List;

public class DoublyLinkedList {

	private Node head;
	private Node tail;
	private int size;
	private int maxSize;
	
	public DoublyLinkedList(int size) {
		this.maxSize = size;
	}

	public void add(Node node) {
		if (size == 0) {
			this.head = node;
			this.tail = node;
			size += 1;
			return;
		}
		if (size == maxSize) {
			tail = tail.getPrev();
			tail.getNext().setPrev(null);
			tail.getNext().setNext(null);
			size -= 1;
		}
		head.setPrev(node);
		node.setNext(head);
		head = node;
		size += 1;
	}
	
	public void useNode(Node node) {
		if (head == node) return;
		if (tail == node) {
			tail = tail.getPrev();
			tail.setNext(null);
			node.setPrev(null);
		}
		else {
			node.getPrev().setNext(node.getNext());
			node.getNext().setPrev(node.getPrev());
			node.setNext(null);
			node.setPrev(null);
		}
		size -= 1;
		add(node);
	}

	public int getSize() {
		return size;
	}
	
	public Node getTail() {
		return tail;
	}
	
	public List<String> getData() {
		List<String> list = new ArrayList<>(size);
		Node current = head;
		while (current != null) {
			list.add(current.getData());
			current = current.getNext();
		}
		return list;
	}
	

}
