package custom_model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import scala.Tuple2;

public class FPTree<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6929539985923995498L;
	
	Node<T> root = new Node<T>(null);
	
	HashMap<T, Summary<T>> summaries = new HashMap<T, Summary<T>>();
	
	public FPTree<T> add(List<T>t, int count){
		if (count < 0) return this;
		Node<T> curr = root;
		curr.count += count;
		for(T item :t){
			Summary<T> summary = summaries.getOrDefault(item, new Summary<T>());
			summaries.put(item, summary);
			summary.count += count;			
			Node<T> child = curr.children.getOrDefault(item, new Node<T>(curr));
			curr.children.put(item, child);
			child.item = item;
			child.count += count;
			curr = child;
			if (!summary.nodes.contains(child)){
				summary.nodes.add(child);
			}
		}
		return this;
	}
	
	public FPTree<T> merge(FPTree<T> other){
		List<Tuple2<List<T>, Integer>> transactions = other.transactions();
		for(Tuple2<List<T>, Integer> transaction : transactions){
			this.add(transaction._1, transaction._2);
		}
		return this;
	}
	
	
	public List<Tuple2<List<T>, Integer>> transactions(){
		return getTransactions(root);
	}
	
	private List<Tuple2<List<T>, Integer>> getTransactions(Node<T> node){
		int count = node.count;
		List<Tuple2<List<T>, Integer>> result = new ArrayList<Tuple2<List<T>, Integer>>();
		for(T item : node.children.keySet()){
			Node<T> child = node.children.get(item);
			List<Tuple2<List<T>, Integer>> transactions = getTransactions(child);
			for(Tuple2<List<T>, Integer> transaction : transactions){
				transaction._1.add(0, item);
				count -= transaction._2;
			}
			result.addAll(transactions);
		}
		if (count>0){
			result.add(new Tuple2<List<T>, Integer>(new ArrayList<T>(), count));
		}
		return result;		
	}
		
	
	public FPTree<T> project(T suffix){
		FPTree<T> tree = new FPTree<T>();
		if (summaries.containsKey(suffix)){
			Summary<T> summary = summaries.get(suffix);
			for(Node<T> node : summary.nodes){
				ArrayList<T> t = new ArrayList<T>();
				Node<T> curr = node.parent;
				while(!curr.isRoot()){
					t.add(curr.item);
					curr = curr.parent;
				}
				tree.add(t, node.count);
			}
		}
		
		return tree;
	}
	
	public List<Tuple2<List<T>, Integer>> extract(int minCount){
		 List<Tuple2<List<T>, Integer>> result = new ArrayList<Tuple2<List<T>, Integer>>();
		 for(T item : summaries.keySet()){
			 Summary<T> summary = summaries.get(item);
			 if(summary.count >= minCount){
				 // add current pattern
				 List<T> curPattern = new ArrayList<T>();
				 curPattern.add(item);
				 result.add(new Tuple2<List<T>, Integer>(curPattern, summary.count));
				 // recursion
				 List<Tuple2<List<T>, Integer>> patterns = project(item).extract(minCount);
				 for(Tuple2<List<T>, Integer> pattern : patterns){
					 pattern._1.add(0, item);
				 }
				 result.addAll(patterns);
			 }
		 }
		 return result;
	}
	
	
	private class Node<T> implements Serializable{
		private static final long serialVersionUID = 5892249353933414824L;
		T item;
		int count = 0;
		HashMap<T, Node<T>> children = new HashMap<T, Node<T>>();
		Node<T> parent = null;
		
		public Node(Node<T> parent) {
			this.parent = parent;
		}
		
		boolean isRoot(){
			return parent == null;
		}
		
	}
	
	private class Summary<T> implements Serializable{
		private static final long serialVersionUID = 2256734813310893768L;
		int count = 0;
		ArrayList<Node<T>> nodes = new ArrayList<Node<T>>();
	}
	
	void printTree(){
		//Test Tree construct
		for(T item :summaries.keySet()){
			Summary<T> summary = summaries.get(item);
			List<Node<T>> nodes = summary.nodes;
			for(Node<T> node : nodes){
				System.out.println(node.item+" "+ node.count);
			}
		}
	}
	
	static String list2T(List<String> list){
		StringBuffer sb = new StringBuffer();
		for(String str : list){
			sb.append(str);
		}
		return sb.toString();		
	}
	
	@Test
	public void test(){

	}
	
	public static void main(String []args){
		System.out.println("Main");
		String[] item1 = {"z r p ", //r z h k p  
		"z x y s t ",//"z y x w v u t s",
		"x s r",//"s x o n r",
		"z x y s t q",//"x z y m t s q e",
		"z",
		"z x y t r q p "//"x z y r q t p",	
		};
		System.out.println("Test Construct FPTree");
		FPTree<String> tree = new FPTree<String>();
		List<String> transactions = Arrays.asList(item1);
		for(String transaction : transactions){
			List<String> items = Arrays.asList(transaction.split(" "));
			tree.add(items, 1);
		}
		//tree.printTree();;
		/**
		System.out.println("Test Project");
		FPTree projectTree = tree.project("z");
		printTree(projectTree);
		*/
		System.out.println("Extract");
		 List<Tuple2<List<String>, Integer>> result = tree.extract(2);
		 for(Tuple2<List<String>, Integer> item: result){
			 System.out.println("["+list2T(item._1)+"]"+","+item._2);
		 }	
	
		 System.out.println("transactions");
	 	List<Tuple2<List<String>, Integer>> ori_transactions = tree.transactions();
	 	for(Tuple2<List<String>, Integer> transaction : ori_transactions){
	 		List<String> items = transaction._1;
	 		System.out.println(String.join(",", items.toArray(new String[]{})) + transaction._2());
	 	}
	}

}
