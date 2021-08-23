import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;


/**A class that is used to create a KTree
 * 
 * @author Carlos Haddock, CS310, Prof. Russel Section-002
 *
 * @param <E> a generic type E
 */
public class KTree<E> implements TreeIterable<E> {
	private int size = 0;
	private int height;
	private int givenk = 0;
	private int CAP;
	private Object[] arrayTreeCopy;
	private Object[] original;
	/**Used to create a KTree with a given array, and branch factor of K.
	 * 
	 * @param arrayTree The given array that will be built into a KTree
	 * @param k the branching factor for the KTree
	 * @throws InvalidKException if the branching factor is set to anything below 2.
	 */
	public KTree(E[] arrayTree, int k) throws InvalidKException {
		arrayTreeCopy = new Object[arrayTree.length];
		for(int i = 0 ; i < arrayTree.length; i++) {
				arrayTreeCopy[i] = arrayTree[i];
				if (arrayTree[i] != null) {
					size++;
				}
		}
		givenk = k;
		CAP = arrayTreeCopy.length;
		height = (int) Math.ceil((Math.log((k-1)*size+1)/Math.log(k))-1 ) ;
		
		if (k < 2) {
			throw new InvalidKException();
		}
	}
	/**Used to get the Branching factor of the KTree
	 * 
	 * @return returns the Branching factor of the KTree
	 */
	public int getK() {
		return givenk;
	}
	/**Used to get the number of elements in the KTree
	 * 
	 * @return the number of elements in the KTree
	 */
	public int size() {
		return size;
	}
	/**Used to get the height of the KTree
	 * 
	 * @return the height of the KTree.
	 */
	public int height() {
		return height;
	}
	/**Used to get the value located at the given index.
	 * 
	 * @param i The given index where the value will be retrieved from.
	 * @return the value located at the given index.
	 */
	public E get(int i) {

		if (arrayTreeCopy[i] == null) {
			throw new IllegalArgumentException();
		}
		else {
			E temp = (E) arrayTreeCopy[i];
			return temp;
		}
	}
	/**Used to set the value at the given index, expands if the value is able to expand the size of the tree.
	 * 
	 * @param i The given index where the value will be set.
	 * @param value The value that will be set at the given index.
	 * @return if the value was set successfully.
	 * @throws InvalidTreeException If the location is not valid because the tree would become invalid
	 */
	public boolean set(int i, E value) throws InvalidTreeException {
		

		if (value == null) {
			if(arrayTreeCopy[i] != null ) {
				if(arrayTreeCopy[2*i+1] == null && arrayTreeCopy[2*i+2] == null) {
					arrayTreeCopy[i] = null;

					size--;
					height = (int) Math.ceil((Math.log((getK()-1)*size+1)/Math.log(getK()))-1 ) ;
					return true;
				}
			}
		}
		
		else if (i <= CAP && arrayTreeCopy[i] != null) {
			if(arrayTreeCopy[i] == null ) {
				arrayTreeCopy[i] = value;
				size++;
				height = (int) Math.ceil((Math.log((getK()-1)*size+1)/Math.log(getK()))-1 ) ;
				return true;
			}
			else {
				arrayTreeCopy[i] = value;
				height = (int) Math.ceil((Math.log((getK()-1)*size+1)/Math.log(getK()))-1 ) ;
				return true;
			}
		}
		
		else {
			if (arrayTreeCopy[Math.floorDiv(i-1, 2)] == null) {
				throw new InvalidTreeException();
			}
			else if(get((i-1)/2) != null && i < 0 ) {
				return false;
			}
			else {
				if(i > CAP && get(Math.floorDiv(i-1, 2)) != null){
					expand();
					arrayTreeCopy[size] = value;
					size++;
					return true;
				}
			}
		}
		return false;
	}
	/**Builds an array representation of the KTree, if the tree is able to shrink, the tree will shrink
	 * 
	 * @return the array representation of the KTree
	 */
	public Object[] toArray() {
		int newArraySize = (int) ( ( Math.pow((double) getK() , (double) (height()+1) ) -1)/(getK()-1) );
		Object[] arrayTreeTA = new Object[newArraySize];
			for(int i = 0; i < arrayTreeTA.length; i++) {
				arrayTreeTA[i] = arrayTreeCopy[i];
			}
	return arrayTreeTA;
	}
	/**Expands the Ktree if called
	 * 
	 */
	private void expand() {
		original = arrayTreeCopy;
		int temp_CAP = CAP * 2;
		arrayTreeCopy = (E[]) new Object[temp_CAP];
		for (int f = 0; f < CAP; f++)
			arrayTreeCopy[ f ] = original[ f ];
		original = null;
		CAP = temp_CAP;
		temp_CAP = 0;
	}
	/**Creates a String representation of the KTree if called
	 * 
	 * @return a String representation  of the KTree
	 */
	@Override
	public String toString() {
		int count = 1;
		int f = 1;
		StringBuilder ret = new StringBuilder();
		StringBuilder finalStr = new StringBuilder();
		finalStr.append(arrayTreeCopy[0]);
		finalStr.append("\n");
		for (int i =1; i < CAP; i++)  {
				finalStr.append(arrayTreeCopy[i]);
				finalStr.append(" ");
				if ((double) count == Math.pow(getK(), f)) {
					finalStr.append("\n");
					f++;
					count = 0;
				}
				count++;
		}
		return finalStr.toString();
	}
	/**Creates an anonymous class LevelOrderIterator, that prints the KTree in level order, this then returns the iterator.
	 *  
	 */
	public Iterator<E> getLevelOrderIterator() {
		return new Iterator<E>() {
			int x = 0;
			public boolean hasNext() {
				return (x < CAP);
			}
			public E next() {
				if(hasNext() == false) {
					throw new NullPointerException("Tried to call next() when there are no more items");
				}
				E temp = null;
				temp =  (E) arrayTreeCopy[x];
				if(temp == null) {
					temp = (E) "";
					x++;
					return temp;
				}
				temp = (E) (temp + " ");
				x++;
				return temp;
			}
		};
	}
	/**Creates a string representation of a level order traversal of the KTree.
	 * 
	 * @return returns the representation of a level order traversal.
	 */
	public String toStringLevelOrder() {
		Iterator<E> it = this.getLevelOrderIterator();
		String str = "";
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			sb.append(it.next());
		}
		str = sb.toString();
		return str; 
	}
	/**Creates an anonymous class PostOrderIterator, that prints the KTree in post order, this then returns the iterator.
	 *  
	 */
	public Iterator<E> getPostOrderIterator(){
		return new Iterator<E>() {
			Queue<E> q = new Queue<E>(CAP);
			int count = 0;
			boolean init = true;;
			int tp = 0;
			public boolean hasNext() {
				return (tp < size());
			}
			public E next() {
				if(hasNext() == false) {
					throw new NullPointerException("Tried to call next() when there are no more items");
				}
				if (init) {
					for (int x = 1 ; x <= getK() ; x++) {
							for (int n = 1 ; n <= getK(); n++) { 
	
								if ( (getK()*x+n) < CAP) { 
									if(getK()*x+n == getK()*x+1) {
										q.q((E) arrayTreeCopy[getK()*x+n]);
										count++;
									}
									else if(getK()*x+n == getK()*x+getK()) {
										q.q((E) arrayTreeCopy[getK()*x+n]);
										count++;
									}
									else {
										q.q((E) arrayTreeCopy[getK()*x+n]);
										count++;
									}
									if(count == getK()) {
										count = 0;
									}
								}
							}
							q.q((E) arrayTreeCopy[x]);
						}
					q.q((E) arrayTreeCopy[0]);
					init = false;
				}
				E temp = q.dq();

				if(temp == null) {
					temp = (E) "";
					return temp;
				}
				temp = (E) (temp + " ");
				tp++;
				return temp;
			}
		};
	}
	/**Creates an anonymous class PreOrderIterator, that prints the KTree in pre order, this then returns the iterator.
	 *  
	 */
	public Iterator<E> getPreOrderIterator(){

		return new Iterator<E>() {
			Queue<E> q = new Queue<E>(CAP);
			int count = 0;
			boolean init = true;;
			int tp = 0;
			public boolean hasNext() {
				return (tp < size());
			}
			public E next() {
				if(hasNext() == false) {
					throw new NullPointerException("Tried to call next() when there are no more items");
				}
				if (init) {
					q.q((E) arrayTreeCopy[0]);
					for (int x = 1 ; x <= getK() ; x++) {
						q.q((E) arrayTreeCopy[x]);
							for (int n = 1 ; n <= getK(); n++) { 
								if ( (getK()*x+n) < CAP) { 
									if(getK()*x+n == getK()*x+1) {
										q.q((E) arrayTreeCopy[getK()*x+n]);
										count++;
									}
									else if(getK()*x+n == getK()*x+getK()) {
										q.q((E) arrayTreeCopy[getK()*x+n]);
										count++;
									}
									else {
										q.q((E) arrayTreeCopy[getK()*x+n]);
										count++;
									}
									if(count == getK()) {
										count = 0;
									}
								}
							}
						}
					init = false;
				}
				E temp = q.dq();

				if(temp == null) {
					temp = (E) "";
					return temp;
				}
				temp = (E) (temp + " ");
				tp++;
				return temp;
			}
		};	
	}
	/**Creates a string representation of a Post order traversal of the KTree.
	 * 
	 * @return returns the representation of a Post order traversal.
	 */
	public String toStringPostOrder() {
		Iterator<E> it = this.getPostOrderIterator();
		String str = "";
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			sb.append(it.next());
		}
		str = sb.toString();
		return str; 
	}
	/**Creates a string representation of a pre-order traversal of the KTree.
	 * 
	 * @return returns the representation of a pre-order traversal.
	 */
	public String toStringPreOrder() {
		Iterator<E> it = this.getPreOrderIterator();
		String str = "";
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			sb.append(it.next());
		}
		str = sb.toString();
		return str; 
	}
	/**Decodes a given KTree by using a given codedMessage of Type String
	 * 
	 * @param tree a given KTree of type String
	 * @param codedMessage a given codedMessage of Type String, that will be decoded
	 * @return the decoded message retrieved from the codedMessage using the given KTree
	 */
	public static String decode(KTree<String> tree, String codedMessage) {
		Object[] arr =  tree.toArray();
		int capacity = arr.length;
		int currentPos = 0;
		int chosePath = 0 ;
		int countedChildren = 0 ;
		Iterator<String> str = tree.getPostOrderIterator();
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < codedMessage.length(); i++) {
			chosePath = Character.getNumericValue(codedMessage.charAt(i));
			countedChildren = 0 ;
			
			
			if(chosePath == tree.getK()-1) {
				currentPos = (tree.getK() * currentPos + tree.getK());
				for(int n = 1 ; n <= tree.getK(); n++) {
					if((tree.getK() * currentPos + n) >= capacity ) {
						countedChildren++;
						
					}
					else if(arr[(tree.getK() * currentPos + n)] == null) {
						countedChildren++;
					}

				}
				if(countedChildren == tree.getK()) {
					sb.append(arr[currentPos]);
					currentPos = 0;
					

				}
			}
			else if(chosePath == 0) {
				currentPos = (tree.getK() * currentPos + 1);
				for(int n = 1 ; n <= tree.getK(); n++) {
					if((tree.getK() * currentPos + n) >= capacity ) {
						countedChildren++;
						
					}
					else if(arr[(tree.getK() * currentPos + n)] == null) {
						countedChildren++;
					}

				}
				if(countedChildren == tree.getK()) {
					sb.append(arr[currentPos]);
					currentPos = 0;
					

				}
			}
			else {
				currentPos = (tree.getK() * currentPos + (chosePath+1));
				for(int n = 1 ; n <= tree.getK(); n++) {
					if((tree.getK() * currentPos + n) >= capacity ) {
						countedChildren++;
						
					}
					else if(arr[(tree.getK() * currentPos + n)] == null) {
						countedChildren++;
					}

				}
				if(countedChildren == tree.getK()) {
					sb.append(arr[currentPos]);
					currentPos = 0;
					

				}
			}
		}// end of loop
		
		return sb.toString();
	}
	
	/****************************************/
	/* EDIT THIS MAIN METHOD FOR TESTS. PUT */
	/* HELPER TEST METHODS IN THIS SECTION  */
	/* AS WELL. TESTS REQUIRED FOR FULL     */
    /* CREDIT.                              */
	/****************************************/
	
	/** Test Driver
	 * 
	 * 
	 */
	public static void main(String[] args) {
		//change this method around to test!
		String[] strings = { "A", "B", "C", "B-left", "B-right", "C-left", "C-right", null , null , null , null, null, null, "**I should be on a new row in toString**"};
		KTree<String> k = new KTree<>(strings, 3);
		//System.out.println(k.size());
		
		
	    Integer[] input = {0, null, 2, null, null, 5, 6};
	    KTree<Integer> tree = new KTree<>(input, 2);
	    Integer[] i2 = {0, 1, 2, null, 4, 5, null}; 
	    KTree<Integer> t2 = new KTree<>(i2, 2); 
	    Integer[] i3 = {0, 1, 2, 3, 4, 5, 6}; 
	    KTree<Integer> t3 = new KTree<>(i3, 2);
/*	    Integer[] failure = {0, 1, 2, 3, 4, 5, 6}; 
	    KTree<Integer> failureTree = new KTree<>(i3, 1);*/
/*	    System.out.println(k.height());
	    System.out.println(tree.size);
	    System.out.println(tree.get(0));
	    System.out.println(tree.get(1));
	    
	    System.out.println(tree.get(6));
	    System.out.println(tree.set(3,3));
	    System.out.println(tree.set(14,14));
	    System.out.println(tree.set(2,null));
	    System.out.println(tree.set(-1,null));
	    Object[] output = tree.toArray();*/
	    System.out.println("Testing tree.toString()");
	    System.out.println("-------------------------------");
	    System.out.println(tree.toString());
	    System.out.println("Testing k.toString()");
	    System.out.println("-------------------------------");
	    System.out.println(k.toString());
	    

	    
	    Iterator<Integer> it = t2.getLevelOrderIterator(); 
	    Iterator<String> it2 = k.getLevelOrderIterator(); 
	    Iterator<Integer> it3 = tree.getLevelOrderIterator(); 
	    
	    System.out.println("Testing LevelOrder Iterators");
	    System.out.println("-------------------------------");
	    System.out.println();
	    while (it.hasNext()) {
	    	System.out.print(it.next());
	    }
	    System.out.println();
	    while (it2.hasNext()) {
	    	System.out.print(it2.next());
	    }
	    System.out.println();
	    while (it3.hasNext()) {
	    	System.out.print(it3.next());
	    }
	    System.out.println();

	    Iterator<Integer> a = tree.getPreOrderIterator();
	    Iterator<Integer> b = t2.getPreOrderIterator();
	    Iterator<Integer> c = t3.getPreOrderIterator();
	    System.out.println();
	    System.out.println("Testing PreOrder Iterators");
	    System.out.println("-------------------------------");
	    System.out.println();
	    while (a.hasNext()) {
	    	System.out.print(a.next());
	    }

	    System.out.println();
	    while (b.hasNext()) {
	    	System.out.print(b.next());
	    }
	    System.out.println();

	    while (c.hasNext()) {
	    	System.out.print(c.next());
	    }
	    System.out.println();
	    System.out.println();
	    System.out.println("Testing PostOrder Iterators");
	    System.out.println("-------------------------------");
 
	    Iterator<Integer> z = t2.getPostOrderIterator();
	    Iterator<Integer> z2 = tree.getPostOrderIterator();
	    Iterator<Integer> x = t3.getPostOrderIterator();
	    while (z.hasNext()) {
	    	System.out.print(z.next());
	    }
	    System.out.println();
	    while (z2.hasNext()) {
	    	System.out.print(z2.next());
	    }

	    System.out.println();

	    while (x.hasNext()) {
	    	System.out.print(x.next());
	    }
	    
	    System.out.println();
	    System.out.println();
	    System.out.println("Testing Printers! LevelOrder");
	    System.out.println("-------------------------------");
	    System.out.println(tree.toStringLevelOrder());
	    System.out.println(t2.toStringLevelOrder());
	    System.out.println(t3.toStringLevelOrder());
	    System.out.println("Testing Printers! PreOrder");
	    System.out.println("-------------------------------");
	    System.out.println(tree.toStringPreOrder());
	    System.out.println(t2.toStringPreOrder());
	    System.out.println(t3.toStringPreOrder());
	    System.out.println("Testing Printers! PostOrder");
	    System.out.println("-------------------------------");
	    System.out.println(tree.toStringPostOrder());
	    System.out.println(t2.toStringPostOrder());
	    System.out.println(t3.toStringPostOrder());
	    System.out.println("-------------------------------");
	    String[] str = {"_","_","A","B","N",null,null};  
	    KTree<String> sKTree = new KTree<>(str, 2);
	    
	    String[] testStr = {"_","_","_","N","E",null,"M", null, "A", "G",null,null,null};  
	    KTree<String> sKTree2 = new KTree<>(testStr, 3);
	    
	    String[] finished = {"_","_","_","_","_","E",null, null, "!", null,"D",null,null, null, null,"N", null, null, null, null, "O"};  
	    KTree<String> finishedKTree = new KTree<>(finished, 4);
	    
	    System.out.println(decode(sKTree, "001011011")); // Banana
	    System.out.println(decode(sKTree2,"020012112")); // Megan
	    System.out.println(decode(finishedKTree,"1133220003")); //Done!
	    
	    System.out.println("Testing if shrinking of an array works\n");
	    String[] important = {"A", "C", null, null, null, null, null};  
	    KTree<String> importantT = new KTree<>(important, 2);
	    System.out.println("Before Shrink");
	    System.out.println("Array Length-"+important.length); // 7 
	    System.out.println("KTree height-"+importantT.height()); //1
	    System.out.println("KTree size-"+importantT.size()); // 2
	    Object[] itoArray = importantT.toArray(); 
	    String[] itoString = Arrays.copyOf(itoArray, itoArray.length, String[].class);
	    KTree<String> itoArrayTree = new KTree<>(itoString, 2);	  
	    System.out.println("After Shrink");
	    System.out.println("Array Length-"+itoArray.length); // 3
	    System.out.println("KTree height-"+itoArrayTree.height()); //1
	    System.out.println("KTree size-"+itoArrayTree.size()); // 2
	    System.out.println("\nmethodSigCheck, checking everything but the EC: \n");
	    methodSigCheck();

	}
	/**A basic queue created for the above iterators.
	 * 
	 * @author Carlos Haddock, CS310, Prof. Russel Section-002
	 *
	 * @param <E> a generic type E
	 */
	private class Queue<E>{
		private Object[] arr;
		private int head = 0; 
		private int tail = 0;
		private int sizea = 0;
		private Queue(int length) {
			arr = new Object[length];
		}
		private void q(E item) {
			if(tail == 0) {
				arr[head] = item;
				tail ++;
				sizea ++;
			}
			else {

				arr[tail] = item;
				tail++;
				sizea ++;

			}
		}
		private E dq() {
			E temp = null;
			if (sizea == 0){
				return null;
			}
			if (sizea == 1) {

				temp = (E) arr[head];
				arr[head] = null;
				sizea--;
				tail--;
			}
			else {
				temp = (E) arr[head];
				arr[head] = null; 
				sizea--;
				head++;
			}
			
			if (tail == CAP) {
				tail = tail % CAP;
			}
			if (head == CAP) {
				head = head % CAP;
			}
			return temp;
		
		}
	}
	
	/****************************************/
	/* DO NOT EDIT ANYTHING BELOW THIS LINE */
	/****************************************/

	/** Test Driver to test if the above methods have the correct signature.
	 * 
	 */
	public static void methodSigCheck() {
		//This ensures that you've written your method signatures correctly
		//and understand how to call the various methods from the assignment
		//description.
		
		String[] strings = { "_", "_", "A", "B", "N", null, null };
		
		KTree<String> tree = new KTree<>(strings, 2);
		int x = tree.getK(); //should return 2
		int y = tree.size(); //should return 5
		int z = tree.height(); //should return 2
		System.out.println(x);
		System.out.println(y);
		System.out.println(z);
		
		String v = tree.get(0); //should be "_"
		boolean b = tree.set(0, "x"); //should set the root to "x"
		Object[] o = tree.toArray(); //should return [ "x", "_", "A", "B", "N", null, null ]
		System.out.println(v);
		System.out.println(b);
		System.out.println(o);
		String s = tree.toString(); //should be "x\n_ A\nB N null null"
		String s2 = "" + tree; //should also be "x\n_ A\nB N null null"
		System.out.println(s);
		System.out.println(s2);

		Iterator<String> it1 = tree.getLevelOrderIterator(); //gets an iterator
		Iterator<String> it2 = tree.getPreOrderIterator(); //gets an iterator
		Iterator<String> it3 = tree.getPostOrderIterator(); //gets an iterator
		
		String s3 = tree.toStringLevelOrder(); //should be "_ _ A B N"
		String s4 = tree.toStringPreOrder(); //should be "_ _ B N _ N"
		String s5 = tree.toStringPostOrder(); //should be "B N _ A _"
		System.out.println(s3);
		System.out.println(s4);
		System.out.println(s5);
		String s6 = decode(tree, "001011011"); //should be "BANANA"
		System.out.println(s6);

		//Object[] o2 = tree.mirror(); //should return [ "x", "A", "_", null, null, "N", "B" ]
		//Object[] o3 = tree.subtree(1); //should return [ "_", "B", "N" ]
	}



}

