import java.util.Iterator;
/**An Interface that is used for the Iterators.
 * 
 * @author Carlos Haddock, CS310, Prof. Russel Section-002
 *
 * @param <T> a generic type T
 */
interface TreeIterable<T> {
	/**Interface for an anonymous class LevelOrderIterator, that prints the KTree in level order, this then returns the iterator.
	 * 
	 *  @return a Level Order Iterator of type T
	 */

	public Iterator<T> getLevelOrderIterator();
	/**Interface for an anonymous class PostOrderIterator, that prints the KTree in post order, this then returns the iterator.
	 *  
	 *  @return a Pre Order Iterator of type T
	 */
	public Iterator<T> getPreOrderIterator();
	/**Interface for an anonymous class PreOrderIterator, that prints the KTree in pre order, this then returns the iterator.
	 *  
	 *  @return a Post Order Iterator of type T
	 */
	public Iterator<T> getPostOrderIterator();
}
