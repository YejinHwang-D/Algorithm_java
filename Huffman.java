package assignment3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

class Node {	// ��� ����
	public Node left;	//���� �ڽ�
	public Node right;	//������ �ڽ�
	
	public int freq;	//�� ������ �󵵼� ����
	public char alphabet;	//���ĺ� or space �ϳ� ����
	
	public Node(int frequency, char character) {	//Node Ŭ���� ������ (�ʱ�ȭ)
		left = right = null;
		this.freq = frequency;
		this.alphabet = character;
	}
}

class MinHeap {		//�ּ� Heap
	private ArrayList<Node> tree = new ArrayList<Node>(28);
	//���� tree ����
	
	public MinHeap() {	//�ε��� 0�� Null�� ����α�
		tree.add(null);
	}
	
	public boolean isEmpty() {	//Heap ����ִ��� Ȯ��
		return (tree.size() <= 1);
	}
	
	public void insert_info(Node n) {	//Heap �Է�
		tree.add(n);
		int childNode = tree.size()-1;	//�� �ڽ��� �ε���
		int parentNode = childNode/2;	//�� �θ��� �ε���
		
		while (parentNode > 1 && tree.get(childNode).freq < tree.get(parentNode).freq) {
			//root Node���� �˻��߰ų� �ڽ��� �θ𺸴� Ŭ �� ����
			Collections.swap(tree, childNode, parentNode);	//Data swap
			childNode = parentNode;
			parentNode = childNode/2;
		}
	}
	
	// �ּ� ��� ��ȯ.
	public Node return_min() {
		if(isEmpty()) 
			return null; // heap�� ������� ��� �� �� return

		Node min = tree.get(1);	//0��° ���� ���� ���� �� ��������
		int top = tree.size()-1;	//�� ������ �ε���

		tree.set(1, tree.get(top));	// top index�� �� ������ �Űܿ���
		tree.remove(top);

		int current = 1;
		int left = current*2;
		int right = current*2 + 1;


		while(true) {
			int temp;

			if (left > tree.size()-1) {
				break;
			}
			else if(right > tree.size()-1) {
				if(tree.get(left).freq >= tree.get(current).freq)
					break;
				temp = left;
			}
			else {
				if(tree.get(left).freq >= tree.get(current).freq && tree.get(right).freq >= tree.get(current).freq)
					break; 
				temp = (tree.get(left).freq < tree.get(right).freq) ? left : right;
				if (tree.get(left).freq < tree.get(current).freq)
					temp = left;
				else
					temp = right;
			}
			Collections.swap(tree, temp, current);

			current = temp;
			left = current*2;
			right = current*2 + 1;
		}
		return min;
	}
}


public class Huffman {	//Huffman Code
	public static HashMap<Character, Integer> tree = new HashMap<Character, Integer>();
	public static Node huffman=null;

	// ���ĺ� �󵵼� count�ϴ� �Լ�
	public static void count_freq() {
		try {
		  int i;
		  char t;
		  String temp;
	      FileReader fr = new FileReader("src/alphabet1.txt"); //FileReader�� I/O ó���� �ʼ�
	      BufferedReader br = new BufferedReader(fr);
	      
	      while ((temp = br.readLine()) != null) {
	    	  for (i=0; i< temp.length(); i++) {	//���̸�ŭ
	    		  t = temp.charAt(i);	//����ȯ
	    		  if (tree.containsKey(t))	//HashMap�� ���� �ִ��� Ȯ��
	    			  tree.put(t, tree.get(t)+1);	//+1
	    		  else
	    			  tree.put(t, 1); //���ٸ� ���� �߰�
	    	  }
	      }
	      br.close();
	    } 
		catch (IOException err) {
	        System.exit(1);
	    }
	}
	
	public static void huffman() {
		MinHeap new_heap = new MinHeap();	//���ο� �ּ� Heap ����
		if(tree.isEmpty())
			return;

		for(char key : tree.keySet())
			new_heap.insert_info(new Node(tree.get(key), key));

		while(true) {
			Node leftChild = new_heap.return_min();	//���� ���� �ΰ� ��������
			Node rightChild = new_heap.return_min();
			
			huffman = new Node(leftChild.freq+rightChild.freq, '.'); //�� ����� ���� �θ� ���� �����
			huffman.left = leftChild;	//�ڽ� ����
			huffman.right = rightChild;

			if(new_heap.isEmpty()) //��� heap ������ٸ� Ż��
				return;
			new_heap.insert_info(huffman);
		}
	}

	public static void print_code(Node current, int trace[], int top) {
		if(current.left != null) {	//���� �ڽ����� ���� ��δ� 0
			trace[top] = 0; //�ڵ� ����
			print_code(current.left, trace, top+1); //��ȯ
		}
		if(current.right != null) {	//������ �ڽ����� ���� ��δ� 1
			trace[top] = 1; //�ڵ� ����
			print_code(current.right, trace, top+1); //��ȯ
		}

		if(current.left == null && current.right == null) { //���� ���
			System.out.print("���� \"" + current.alphabet + "\"�� �ڵ� : ");
			for(int i = 0; i < top; i++)
				System.out.print(trace[i]);
			System.out.println("");
		}
	}

	public static void main(String[] args) {
		count_freq();
		huffman();
		
		int []arr = new int[tree.size()-1];
		System.out.println("��� 1. �� ������ �󵵼�");
		for(char c : tree.keySet())
			System.out.println("���� \"" + c + "\"�� �󵵼� : " + tree.get(c));

		System.out.println("��� 2. �Ҵ�� Huffman �ڵ�");
		print_code(huffman, arr, 0);
	}
}
