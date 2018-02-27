package com.rabbitforever.binarytree;

public class Execute {
	private static BinaryTree createBinaryTree() {
	    BinaryTree bt = new BinaryTree();
	 
	    bt.add(6);
	    bt.add(4);
	    bt.add(8);
	    bt.add(3);
	    bt.add(5);
	    bt.add(7);
	    bt.add(9);
	 
	    return bt;
	}
	public static void main(String[] args) {
		BinaryTree bt = createBinaryTree();
		bt.delete(4);
		boolean containsNode = bt.containsNode(3);
		System.out.println(containsNode);
	}

}
