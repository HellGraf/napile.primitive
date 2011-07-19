/*
 * Copyright (c) 1997, 2007, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.napile.primitive.sets.impl;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.napile.primitive.collections.LongCollection;
import org.napile.primitive.iterators.IntIterator;
import org.napile.primitive.iterators.LongIterator;
import org.napile.primitive.lists.impl.CArrayLongList;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.abstracts.AbstractLongSet;

/**
 * A {@link java.util.Set} that uses an internal {@link CopyOnWriteArrayList}
 * for all of its operations.  Thus, it shares the same basic properties:
 * <ul>
 * <li>It is best suited for applications in which set sizes generally
 * stay small, read-only operations
 * vastly outnumber mutative operations, and you need
 * to prevent interference among threads during traversal.
 * <li>It is thread-safe.
 * <li>Mutative operations (<tt>add</tt>, <tt>set</tt>, <tt>remove</tt>, etc.)
 * are expensive since they usually entail copying the entire underlying
 * array.
 * <li>Iterators do not support the mutative <tt>remove</tt> operation.
 * <li>Traversal via iterators is fast and cannot encounter
 * interference from other threads. Iterators rely on
 * unchanging snapshots of the array at the time the iterators were
 * constructed.
 * </ul>
 * <p/>
 * <p> <b>Sample Usage.</b> The following code sketch uses a
 * copy-on-write set to maintain a set of Handler objects that
 * perform some action upon state updates.
 * <p/>
 * <pre>
 * class Handler { void handle(); ... }
 *
 * class X {
 *    private final CArrayLongSet&lt;Handler&gt; handlers
 *       = new CArrayLongSet&lt;Handler&gt;();
 *    public void addHandler(Handler h) { handlers.add(h); }
 *
 *    private long internalState;
 *    private synchronized void changeState() { internalState = ...; }
 *
 *    public void update() {
 *       changeState();
 *       for (Handler handler : handlers)
 *          handler.handle();
 *	 }
 * }
 * </pre>
 * <p/>
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @author Doug Lea
 * @see CArrayLongList
 * @since 1.5
 */
public class CArrayLongSet extends AbstractLongSet implements java.io.Serializable
{
	private final CArrayLongList al;

	/**
	 * Creates an empty set.
	 */
	public CArrayLongSet()
	{
		al = new CArrayLongList();
	}

	/**
	 * Creates a set containing all of the elements of the specified
	 * collection.
	 *
	 * @param c the collection of elements to initially contain
	 * @throws NullPointerException if the specified collection is null
	 */
	public CArrayLongSet(LongCollection c)
	{
		this();
		al.addAllAbsent(c);
	}

	/**
	 * Returns the number of elements in this set.
	 *
	 * @return the number of elements in this set
	 */
	public int size()
	{
		return al.size();
	}

	/**
	 * Returns <tt>true</tt> if this set contains no elements.
	 *
	 * @return <tt>true</tt> if this set contains no elements
	 */
	public boolean isEmpty()
	{
		return al.isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this set contains the specified element.
	 * More formally, returns <tt>true</tt> if and only if this set
	 * contains an element <tt>e</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
	 *
	 * @param o element whose presence in this set is to be tested
	 * @return <tt>true</tt> if this set contains the specified element
	 */
	public boolean contains(int o)
	{
		return al.contains(o);
	}

	/**
	 * Returns an array containing all of the elements in this set.
	 * If this set makes any guarantees as to what order its elements
	 * are returned by its iterator, this method must return the
	 * elements in the same order.
	 * <p/>
	 * <p>The returned array will be "safe" in that no references to it
	 * are maintained by this set.  (In other words, this method must
	 * allocate a new array even if this set is backed by an array).
	 * The caller is thus free to modify the returned array.
	 * <p/>
	 * <p>This method acts as bridge between array-based and collection-based
	 * APIs.
	 *
	 * @return an array containing all the elements in this set
	 */
	public long[] toArray()
	{
		return al.toArray();
	}

	/**
	 * Returns an array containing all of the elements in this set; the
	 * runtime type of the returned array is that of the specified array.
	 * If the set fits in the specified array, it is returned therein.
	 * Otherwise, a new array is allocated with the runtime type of the
	 * specified array and the size of this set.
	 * <p/>
	 * <p>If this set fits in the specified array with room to spare
	 * (i.e., the array has more elements than this set), the element in
	 * the array immediately following the end of the set is set to
	 * <tt>null</tt>.  (This is useful in determining the length of this
	 * set <i>only</i> if the caller knows that this set does not contain
	 * any null elements.)
	 * <p/>
	 * <p>If this set makes any guarantees as to what order its elements
	 * are returned by its iterator, this method must return the elements
	 * in the same order.
	 * <p/>
	 * <p>Like the {@link #toArray()} method, this method acts as bridge between
	 * array-based and collection-based APIs.  Further, this method allows
	 * precise control over the runtime type of the output array, and may,
	 * under certain circumstances, be used to save allocation costs.
	 * <p/>
	 * <p>Suppose <tt>x</tt> is a set known to contain only strings.
	 * The following code can be used to dump the set into a newly allocated
	 * array of <tt>String</tt>:
	 * <p/>
	 * <pre>
	 *     String[] y = x.toArray(new String[0]);</pre>
	 *
	 * Note that <tt>toArray(new Object[0])</tt> is identical in function to
	 * <tt>toArray()</tt>.
	 *
	 * @param a the array into which the elements of this set are to be
	 *          stored, if it is big enough; otherwise, a new array of the same
	 *          runtime type is allocated for this purpose.
	 * @return an array containing all the elements in this set
	 * @throws ArrayStoreException  if the runtime type of the specified array
	 *                              is not a supertype of the runtime type of every element in this
	 *                              set
	 * @throws NullPointerException if the specified array is null
	 */
	public long[] toArray(long[] a)
	{
		return al.toArray(a);
	}

	/**
	 * Removes all of the elements from this set.
	 * The set will be empty after this call returns.
	 */
	public void clear()
	{
		al.clear();
	}

	/**
	 * Removes the specified element from this set if it is present.
	 * More formally, removes an element <tt>e</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>,
	 * if this set contains such an element.  Returns <tt>true</tt> if
	 * this set contained the element (or equivalently, if this set
	 * changed as a result of the call).  (This set will not contain the
	 * element once the call returns.)
	 *
	 * @param o object to be removed from this set, if present
	 * @return <tt>true</tt> if this set contained the specified element
	 */
	public boolean remove(long o)
	{
		return al.remove(o);
	}

	/**
	 * Adds the specified element to this set if it is not already present.
	 * More formally, adds the specified element <tt>e</tt> to this set if
	 * the set contains no element <tt>e2</tt> such that
	 * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>.
	 * If this set already contains the element, the call leaves the set
	 * unchanged and returns <tt>false</tt>.
	 *
	 * @param e element to be added to this set
	 * @return <tt>true</tt> if this set did not already contain the specified
	 *         element
	 */
	public boolean add(long e)
	{
		return al.addIfAbsent(e);
	}

	/**
	 * Returns <tt>true</tt> if this set contains all of the elements of the
	 * specified collection.  If the specified collection is also a set, this
	 * method returns <tt>true</tt> if it is a <i>subset</i> of this set.
	 *
	 * @param c collection to be checked for containment in this set
	 * @return <tt>true</tt> if this set contains all of the elements of the
	 *         specified collection
	 * @throws NullPointerException if the specified collection is null
	 * @see #contains(int)
	 */
	public boolean containsAll(LongCollection c)
	{
		return al.containsAll(c);
	}

	/**
	 * Adds all of the elements in the specified collection to this set if
	 * they're not already present.  If the specified collection is also a
	 * set, the <tt>addAll</tt> operation effectively modifies this set so
	 * that its value is the <i>union</i> of the two sets.  The behavior of
	 * this operation is undefined if the specified collection is modified
	 * while the operation is in progress.
	 *
	 * @param c collection containing elements to be added to this set
	 * @return <tt>true</tt> if this set changed as a result of the call
	 * @throws NullPointerException if the specified collection is null
	 * @see #add(long)
	 */
	public boolean addAll(LongCollection c)
	{
		return al.addAllAbsent(c) > 0;
	}

	/**
	 * Removes from this set all of its elements that are contained in the
	 * specified collection.  If the specified collection is also a set,
	 * this operation effectively modifies this set so that its value is the
	 * <i>asymmetric set difference</i> of the two sets.
	 *
	 * @param c collection containing elements to be removed from this set
	 * @return <tt>true</tt> if this set changed as a result of the call
	 * @throws ClassCastException   if the class of an element of this set
	 *                              is incompatible with the specified collection (optional)
	 * @throws NullPointerException if this set contains a null element and the
	 *                              specified collection does not permit null elements (optional),
	 *                              or if the specified collection is null
	 * @see #remove(long)
	 */
	public boolean removeAll(LongCollection c)
	{
		return al.removeAll(c);
	}

	/**
	 * Retains only the elements in this set that are contained in the
	 * specified collection.  In other words, removes from this set all of
	 * its elements that are not contained in the specified collection.  If
	 * the specified collection is also a set, this operation effectively
	 * modifies this set so that its value is the <i>intersection</i> of the
	 * two sets.
	 *
	 * @param c collection containing elements to be retained in this set
	 * @return <tt>true</tt> if this set changed as a result of the call
	 * @throws ClassCastException   if the class of an element of this set
	 *                              is incompatible with the specified collection (optional)
	 * @throws NullPointerException if this set contains a null element and the
	 *                              specified collection does not permit null elements (optional),
	 *                              or if the specified collection is null
	 * @see #remove(long)
	 */
	public boolean retainAll(LongCollection c)
	{
		return al.retainAll(c);
	}

	/**
	 * Returns an iterator over the elements contained in this set
	 * in the order in which these elements were added.
	 * <p/>
	 * <p>The returned iterator provides a snapshot of the state of the set
	 * when the iterator was constructed. No synchronization is needed while
	 * traversing the iterator. The iterator does <em>NOT</em> support the
	 * <tt>remove</tt> method.
	 *
	 * @return an iterator over the elements in this set
	 */
	public LongIterator iterator()
	{
		return al.iterator();
	}

	/**
	 * Compares the specified object with this set for equality.
	 * Returns {@code true} if the specified object is the same object
	 * as this object, or if it is also a {@link Set} and the elements
	 * returned by an {@linkplain org.napile.primitive.lists.IntList#iterator() iterator} over the
	 * specified set are the same as the elements returned by an
	 * iterator over this set.  More formally, the two iterators are
	 * considered to return the same elements if they return the same
	 * number of elements and for every element {@code e1} returned by
	 * the iterator over the specified set, there is an element
	 * {@code e2} returned by the iterator over this set such that
	 * {@code (e1==null ? e2==null : e1.equals(e2))}.
	 *
	 * @param o object to be compared for equality with this set
	 * @return {@code true} if the specified object is equal to this set
	 */
	public boolean equals(Object o)
	{
		if(o == this)
		{
			return true;
		}
		if(!(o instanceof IntSet))
		{
			return false;
		}
		IntSet set = (IntSet) (o);
		IntIterator it = set.iterator();

		// Uses O(n^2) algorithm that is only appropriate
		// for small sets, which CopyOnWriteArraySets should be.

		//  Use a single snapshot of underlying array
		long[] elements = al.toArray();
		int len = elements.length;
		// Mark matched elements to avoid re-checking
		boolean[] matched = new boolean[len];
		int k = 0;
		outer:
		while(it.hasNext())
		{
			if(++k > len)
			{
				return false;
			}
			int x = it.next();
			for(int i = 0; i < len; ++i)
			{
				if(!matched[i] && x == elements[i])
				{
					matched[i] = true;
					continue outer;
				}
			}
			return false;
		}
		return k == len;
	}
}
