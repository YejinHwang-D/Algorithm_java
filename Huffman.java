package assignment3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

class Node {	// 노드 정의
	public Node left;	//왼쪽 자식
	public Node right;	//오른쪽 자식
	
	public int freq;	//각 문자의 빈도수 저장
	public char alphabet;	//알파벳 or space 하나 저장
	
	public Node(int frequency, char character) {	//Node 클래스 생성자 (초기화)
		left = right = null;
		this.freq = frequency;
		this.alphabet = character;
	}
}

class MinHeap {		//최소 Heap
	private ArrayList<Node> tree = new ArrayList<Node>(28);
	//이진 tree 정의
	
	public MinHeap() {	//인덱스 0은 Null로 비워두기
		tree.add(null);
	}
	
	public boolean isEmpty() {	//Heap 비어있는지 확인
		return (tree.size() <= 1);
	}
	
	public void insert_info(Node n) {	//Heap 입력
		tree.add(n);
		int childNode = tree.size()-1;	//들어갈 자식의 인덱스
		int parentNode = childNode/2;	//들어갈 부모의 인덱스
		
		while (parentNode > 1 && tree.get(childNode).freq < tree.get(parentNode).freq) {
			//root Node까지 검사했거나 자식이 부모보다 클 때 종료
			Collections.swap(tree, childNode, parentNode);	//Data swap
			childNode = parentNode;
			parentNode = childNode/2;
		}
	}
	
	// 최소 노드 반환.
	public Node return_min() {
		if(isEmpty()) 
			return null; // heap이 비어있을 경우 빈 값 return

		Node min = tree.get(1);	//0번째 제외 가장 작은 값 가져오기
		int top = tree.size()-1;	//맨 마지막 인덱스

		tree.set(1, tree.get(top));	// top index를 맨 앞으로 옮겨오기
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

	// 알파벳 빈도수 count하는 함수
	public static void count_freq() {
		try {
		  int i;
		  char t;
		  String temp;
	      FileReader fr = new FileReader("src/alphabet1.txt"); //FileReader는 I/O 처리가 필수
	      BufferedReader br = new BufferedReader(fr);
	      
	      while ((temp = br.readLine()) != null) {
	    	  for (i=0; i< temp.length(); i++) {	//길이만큼
	    		  t = temp.charAt(i);	//형변환
	    		  if (tree.containsKey(t))	//HashMap에 값이 있는지 확인
	    			  tree.put(t, tree.get(t)+1);	//+1
	    		  else
	    			  tree.put(t, 1); //없다면 새로 추가
	    	  }
	      }
	      br.close();
	    } 
		catch (IOException err) {
	        System.exit(1);
	    }
	}
	
	public static void huffman() {
		MinHeap new_heap = new MinHeap();	//새로운 최소 Heap 구조
		if(tree.isEmpty())
			return;

		for(char key : tree.keySet())
			new_heap.insert_info(new Node(tree.get(key), key));

		while(true) {
			Node leftChild = new_heap.return_min();	//제일 앞의 두개 가져오기
			Node rightChild = new_heap.return_min();
			
			huffman = new Node(leftChild.freq+rightChild.freq, '.'); //두 노드의 합을 부모 노드로 만들기
			huffman.left = leftChild;	//자식 지정
			huffman.right = rightChild;

			if(new_heap.isEmpty()) //모든 heap 만들었다면 탈출
				return;
			new_heap.insert_info(huffman);
		}
	}

	public static void print_code(Node current, int trace[], int top) {
		if(current.left != null) {	//왼쪽 자식으로 가는 경로는 0
			trace[top] = 0; //코드 저장
			print_code(current.left, trace, top+1); //순환
		}
		if(current.right != null) {	//오른쪽 자식으로 가는 경로는 1
			trace[top] = 1; //코드 저장
			print_code(current.right, trace, top+1); //순환
		}

		if(current.left == null && current.right == null) { //끝일 경우
			System.out.print("문자 \"" + current.alphabet + "\"의 코드 : ");
			for(int i = 0; i < top; i++)
				System.out.print(trace[i]);
			System.out.println("");
		}
	}

	public static void main(String[] args) {
		count_freq();
		huffman();
		
		int []arr = new int[tree.size()-1];
		System.out.println("■□ 1. 각 문자의 빈도수");
		for(char c : tree.keySet())
			System.out.println("문자 \"" + c + "\"의 빈도수 : " + tree.get(c));

		System.out.println("■□ 2. 할당된 Huffman 코드");
		print_code(huffman, arr, 0);
	}
}
