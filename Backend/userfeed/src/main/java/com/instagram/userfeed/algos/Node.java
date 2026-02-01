package com.instagram.userfeed.algos;

public class Node {

	private Node prev;
	private String data;
	private Node next;
	
	Node(String data) {
		this.data = data;
	}
	
	public Node getPrev() {
		return prev;
	}
	public void setPrev(Node prev) {
		this.prev = prev;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Node getNext() {
		return next;
	}
	public void setNext(Node next) {
		this.next = next;
	}
	
	
}
