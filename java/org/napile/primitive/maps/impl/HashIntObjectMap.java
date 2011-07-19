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
package org.napile.primitive.maps.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.napile.primitive.Containers;
import org.napile.primitive.iterators.IntIterator;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.abstracts.AbstractIntObjectMap;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.abstracts.AbstractIntSet;

/**
 * Hash table based implementation of the <tt>Map</tt> interface.  This
 * implementation provides all of the optional map operations, and permits
 * <tt>null</tt> values and the <tt>null</tt> key.  (The <tt>HashMap</tt>
 * class is roughly equivalent to <tt>Hashtable</tt>, except that it is
 * unsynchronized and permits nulls.)  This class makes no guarantees as to
 * the order of the map; in particular, it does not guarantee that the order
 * will remain constant over time.
 * <p/>
 * <p>This implementation provides constant-time performance for the basic
 * operations (<tt>get</tt> and <tt>put</tt>), assuming the hash function
 * disperses the elements properly among the buckets.  Iteration over
 * collection views requires time proportional to the "capacity" of the
 * <tt>HashMap</tt> instance (the number of buckets) plus its size (the number
 * of key-value mappings).  Thus, it's very important not to set the initial
 * capacity too high (or the load factor too low) if iteration performance is
 * important.
 * <p/>
 * <p>An instance of <tt>HashMap</tt> has two parameters that affect its
 * performance: <i>initial capacity</i> and <i>load factor</i>.  The
 * <i>capacity</i> is the number of buckets in the hash table, and the initial
 * capacity is simply the capacity at the time the hash table is created.  The
 * <i>load factor</i> is a measure of how full the hash table is allowed to
 * get before its capacity is automatically increased.  When the number of
 * entries in the hash table exceeds the product of the load factor and the
 * current capacity, the hash table is <i>rehashed</i> (that is, internal data
 * structures are rebuilt) so that the hash table has approximately twice the
 * number of buckets.
 * <p/>
 * <p>As a general rule, the default load factor (.75) offers a good tradeoff
 * between time and space costs.  Higher values decrease the space overhead
 * but increase the lookup cost (reflected in most of the operations of the
 * <tt>HashMap</tt> class, including <tt>get</tt> and <tt>put</tt>).  The
 * expected number of entries in the map and its load factor should be taken
 * into account when setting its initial capacity, so as to minimize the
 * number of rehash operations.  If the initial capacity is greater
 * than the maximum number of entries divided by the load factor, no
 * rehash operations will ever occur.
 * <p/>
 * <p>If many mappings are to be stored in a <tt>HashMap</tt> instance,
 * creating it with a sufficiently large capacity will allow the mappings to
 * be stored more efficiently than letting it perform automatic rehashing as
 * needed to grow the table.
 * <p/>
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a hash map concurrently, and at least one of
 * the threads modifies the map structurally, it <i>must</i> be
 * synchronized externally.  (A structural modification is any operation
 * that adds or deletes one or more mappings; merely changing the value
 * associated with a key that an instance already contains is not a
 * structural modification.)  This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the map.
 * <p/>
 * If no such object exists, the map should be "wrapped" using the
 * {@link Collections#synchronizedMap Collections.synchronizedMap}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the map:<pre>
 *   Map m = Collections.synchronizedMap(new HashMap(...));</pre>
 * <p/>
 * <p>The iterators returned by all of this class's "collection view methods"
 * are <i>fail-fast</i>: if the map is structurally modified at any time after
 * the iterator is created, in any way except through the iterator's own
 * <tt>remove</tt> method, the iterator will throw a
 * {@link ConcurrentModificationException}.  Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than risking
 * arbitrary, non-deterministic behavior at an undetermined time in the
 * future.
 * <p/>
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw <tt>ConcurrentModificationException</tt> on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness: <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 * <p/>
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @param <V> the type of mapped values
 * @author Doug Lea
 * @author Josh Bloch
 * @author Arthur van Hoff
 * @author Neal Gafter
 * @version %I%, %G%
 * @see	 IntObjectMap
 * @see	 TreeIntObjectMap
 * @see Object#hashCode()
 * @see IntCollection
 * @since 1.2
 */
@SuppressWarnings("unchecked")
public class HashIntObjectMap<V> extends AbstractIntObjectMap<V> implements IntObjectMap<V>, Cloneable, Serializable
{
	/**
	 * The default initial capacity - MUST be a power of two.
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 16;

	/**
	 * The maximum capacity, used if a higher value is implicitly specified
	 * by either of the constructors with arguments.
	 * MUST be a power of two <= 1<<30.
	 */
	static final int MAXIMUM_CAPACITY = 1 << 30;

	/**
	 * The load factor used when none specified in constructor.
	 */
	static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * The table, resized as necessary. Length MUST Always be a power of two.
	 */
	transient Entry<V>[] table;

	/**
	 * The number of key-value mappings contained in this map.
	 */
	transient int size;

	/**
	 * The next size value at which to resize (capacity * load factor).
	 *
	 * @serial
	 */
	int threshold;

	/**
	 * The load factor for the hash table.
	 *
	 * @serial
	 */
	final float loadFactor;

	/**
	 * The number of times this HashMap has been structurally modified
	 * Structural modifications are those that change the number of mappings in
	 * the HashMap or otherwise modify its internal structure (e.g.,
	 * rehash).  This field is used to make iterators on Collection-views of
	 * the HashMap fail-fast.  (See ConcurrentModificationException).
	 */
	transient volatile int modCount;

	/**
	 * Constructs an empty <tt>HashMap</tt> with the specified initial
	 * capacity and load factor.
	 *
	 * @param initialCapacity the initial capacity
	 * @param loadFactor	  the load factor
	 * @throws IllegalArgumentException if the initial capacity is negative
	 *                                  or the load factor is nonpositive
	 */
	public HashIntObjectMap(int initialCapacity, float loadFactor)
	{
		if(initialCapacity < 0)
		{
			throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
		}
		if(initialCapacity > MAXIMUM_CAPACITY)
		{
			initialCapacity = MAXIMUM_CAPACITY;
		}
		if(loadFactor <= 0 || Float.isNaN(loadFactor))
		{
			throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
		}

		// Find a power of 2 >= initialCapacity
		int capacity = 1;
		while(capacity < initialCapacity)
		{
			capacity <<= 1;
		}

		this.loadFactor = loadFactor;
		threshold = (int) (capacity * loadFactor);
		table = new Entry[capacity];
		init();
	}

	/**
	 * Constructs an empty <tt>HashMap</tt> with the specified initial
	 * capacity and the default load factor (0.75).
	 *
	 * @param initialCapacity the initial capacity.
	 * @throws IllegalArgumentException if the initial capacity is negative.
	 */
	public HashIntObjectMap(int initialCapacity)
	{
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Constructs an empty <tt>HashMap</tt> with the default initial capacity
	 * (16) and the default load factor (0.75).
	 */
	public HashIntObjectMap()
	{
		this.loadFactor = DEFAULT_LOAD_FACTOR;
		threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
		table = new Entry[DEFAULT_INITIAL_CAPACITY];
		init();
	}

	/**
	 * Constructs a new <tt>HashMap</tt> with the same mappings as the
	 * specified <tt>Map</tt>.  The <tt>HashMap</tt> is created with
	 * default load factor (0.75) and an initial capacity sufficient to
	 * hold the mappings in the specified <tt>Map</tt>.
	 *
	 * @param m the map whose mappings are to be placed in this map
	 * @throws NullPointerException if the specified map is null
	 */
	public HashIntObjectMap(IntObjectMap<? extends V> m)
	{
		this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1, DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
		putAllForCreate(m);
	}

	// internal utilities

	/**
	 * Initialization hook for subclasses. This method is called
	 * in all constructors and pseudo-constructors (clone, readObject)
	 * after HashMap has been initialized but before any entries have
	 * been inserted.  (In the absence of this method, readObject would
	 * require explicit knowledge of subclasses.)
	 */
	void init()
	{
	}

	/**
	 * Applies a supplemental hash function to a given hashCode, which
	 * defends against poor quality hash functions.  This is critical
	 * because HashMap uses power-of-two length hash tables, that
	 * otherwise encounter collisions for hashCodes that do not differ
	 * in lower bits. Note: Null keys always map to hash 0, thus index 0.
	 */
	static int hash(int value)
	{
		int h = Containers.hashCode(value);
		// This function ensures that hashCodes that differ only by
		// constant multiples at each bit position have a bounded
		// number of collisions (approximately 8 at default load factor).
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	/**
	 * Returns index for hash code h.
	 */
	static int indexFor(int h, int length)
	{
		return h & (length - 1);
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 *
	 * @return <tt>true</tt> if this map contains no key-value mappings
	 */
	public boolean isEmpty()
	{
		return size == 0;
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or {@code null} if this map contains no mapping for the key.
	 * <p/>
	 * <p>More formally, if this map contains a mapping from a key
	 * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
	 * key.equals(k))}, then this method returns {@code v}; otherwise
	 * it returns {@code null}.  (There can be at most one such mapping.)
	 * <p/>
	 * <p>A return value of {@code null} does not <i>necessarily</i>
	 * indicate that the map contains no mapping for the key; it's also
	 * possible that the map explicitly maps the key to {@code null}.
	 * The {@link #containsKey(int)} operation may be used to
	 * distinguish these two cases.
	 *
	 * @see #put(int, Object)
	 */
	public V get(int key)
	{
		int hash = hash(key);
		for(Entry<V> e = table[indexFor(hash, table.length)]; e != null; e = e.next)
			if(e.hash == hash && e.key == key)
				return e.value;

		return null;
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the
	 * specified key.
	 *
	 * @param key The key whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map contains a mapping for the specified
	 *         key.
	 */
	public boolean containsKey(int key)
	{
		return getEntry(key) != null;
	}

	/**
	 * Returns the entry associated with the specified key in the
	 * HashMap.  Returns null if the HashMap contains no mapping
	 * for the key.
	 */
	final Entry<V> getEntry(int key)
	{
		int hash = hash(key);
		for(Entry<V> e = table[indexFor(hash, table.length)]; e != null; e = e.next)
		{
			int k;
			if(e.hash == hash && ((k = e.key) == key))
			{
				return e;
			}
		}
		return null;
	}


	/**
	 * Associates the specified value with the specified key in this map.
	 * If the map previously contained a mapping for the key, the old
	 * value is replaced.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with <tt>key</tt>, or
	 *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
	 *         (A <tt>null</tt> return can also indicate that the map
	 *         previously associated <tt>null</tt> with <tt>key</tt>.)
	 */
	public V put(int key, V value)
	{
		int hash = hash(key);
		int i = indexFor(hash, table.length);
		for(Entry<V> e = table[i]; e != null; e = e.next)
		{
			int k;
			if(e.hash == hash && ((k = e.key) == key))
			{
				V oldValue = e.value;
				e.value = value;
				e.recordAccess(this);
				return oldValue;
			}
		}

		modCount++;
		addEntry(hash, key, value, i);
		return null;
	}

	/**
	 * This method is used instead of put by constructors and
	 * pseudoconstructors (clone, readObject).  It does not resize the table,
	 * check for comodification, etc.  It calls createEntry rather than
	 * addEntry.
	 */
	private void putForCreate(int key, V value)
	{
		int hash = hash(key);
		int i = indexFor(hash, table.length);

		/**
		 * Look for preexisting entry for key.  This will never happen for
		 * clone or deserialize.  It will only happen for construction if the
		 * input Map is a sorted map whose ordering is inconsistent w/ equals.
		 */
		for(Entry<V> e = table[i]; e != null; e = e.next)
		{
			int k;
			if(e.hash == hash && ((k = e.key) == key))
			{
				e.value = value;
				return;
			}
		}

		createEntry(hash, key, value, i);
	}

	private void putAllForCreate(IntObjectMap<? extends V> m)
	{
		for(Iterator<? extends IntObjectMap.Entry<? extends V>> i = m.entrySet().iterator(); i.hasNext();)
		{
			IntObjectMap.Entry<? extends V> e = i.next();
			putForCreate(e.getKey(), e.getValue());
		}
	}

	/**
	 * Rehashes the contents of this map into a new array with a
	 * larger capacity.  This method is called automatically when the
	 * number of keys in this map reaches its threshold.
	 * <p/>
	 * If current capacity is MAXIMUM_CAPACITY, this method does not
	 * resize the map, but sets threshold to Integer.MAX_VALUE.
	 * This has the effect of preventing future calls.
	 *
	 * @param newCapacity the new capacity, MUST be a power of two;
	 *                    must be greater than current capacity unless current
	 *                    capacity is MAXIMUM_CAPACITY (in which case value
	 *                    is irrelevant).
	 */
	void resize(int newCapacity)
	{
		Entry<V>[] oldTable = table;
		int oldCapacity = oldTable.length;
		if(oldCapacity == MAXIMUM_CAPACITY)
		{
			threshold = Integer.MAX_VALUE;
			return;
		}

		Entry<V>[] newTable = new Entry[newCapacity];
		transfer(newTable);
		table = newTable;
		threshold = (int) (newCapacity * loadFactor);
	}

	/**
	 * Transfers all entries from current table to newTable.
	 */
	void transfer(Entry<V>[] newTable)
	{
		Entry[] src = table;
		int newCapacity = newTable.length;
		for(int j = 0; j < src.length; j++)
		{
			Entry<V> e = src[j];
			if(e != null)
			{
				src[j] = null;
				do
				{
					Entry<V> next = e.next;
					int i = indexFor(e.hash, newCapacity);
					e.next = newTable[i];
					newTable[i] = e;
					e = next;
				}
				while(e != null);
			}
		}
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 * These mappings will replace any mappings that this map had for
	 * any of the keys currently in the specified map.
	 *
	 * @param m mappings to be stored in this map
	 * @throws NullPointerException if the specified map is null
	 */
	public void putAll(IntObjectMap<? extends V> m)
	{
		int numKeysToBeAdded = m.size();
		if(numKeysToBeAdded == 0)
		{
			return;
		}

		/*
				 * Expand the map if the map if the number of mappings to be added
				 * is greater than or equal to threshold.  This is conservative; the
				 * obvious condition is (m.size() + size) >= threshold, but this
				 * condition could result in a map with twice the appropriate capacity,
				 * if the keys to be added overlap with the keys already in this map.
				 * By using the conservative calculation, we subject ourself
				 * to at most one extra resize.
				 */
		if(numKeysToBeAdded > threshold)
		{
			int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
			if(targetCapacity > MAXIMUM_CAPACITY)
			{
				targetCapacity = MAXIMUM_CAPACITY;
			}
			int newCapacity = table.length;
			while(newCapacity < targetCapacity)
			{
				newCapacity <<= 1;
			}
			if(newCapacity > table.length)
			{
				resize(newCapacity);
			}
		}

		for(Iterator<? extends IntObjectMap.Entry<? extends V>> i = m.entrySet().iterator(); i.hasNext();)
		{
			IntObjectMap.Entry<? extends V> e = i.next();
			put(e.getKey(), e.getValue());
		}
	}

	/**
	 * Removes the mapping for the specified key from this map if present.
	 *
	 * @param key key whose mapping is to be removed from the map
	 * @return the previous value associated with <tt>key</tt>, or
	 *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
	 *         (A <tt>null</tt> return can also indicate that the map
	 *         previously associated <tt>null</tt> with <tt>key</tt>.)
	 */
	public V remove(int key)
	{
		Entry<V> e = removeEntryForKey(key);
		return (e == null ? null : e.value);
	}

	/**
	 * Removes and returns the entry associated with the specified key
	 * in the HashMap.  Returns null if the HashMap contains no mapping
	 * for this key.
	 */
	final Entry<V> removeEntryForKey(int key)
	{
		int hash = hash(key);
		int i = indexFor(hash, table.length);
		Entry<V> prev = table[i];
		Entry<V> e = prev;

		while(e != null)
		{
			Entry<V> next = e.next;
			int k;
			if(e.hash == hash && ((k = e.key) == key))
			{
				modCount++;
				size--;
				if(prev == e)
				{
					table[i] = next;
				}
				else
				{
					prev.next = next;
				}
				e.recordRemoval(this);
				return e;
			}
			prev = e;
			e = next;
		}

		return e;
	}

	/**
	 * Special version of remove for EntrySet.
	 */
	final Entry<V> removeMapping(Object o)
	{
		if(!(o instanceof IntObjectMap.Entry))
		{
			return null;
		}

		IntObjectMap.Entry<V> entry = (IntObjectMap.Entry<V>) o;
		Object key = entry.getKey();
		int hash = (key == null) ? 0 : hash(key.hashCode());
		int i = indexFor(hash, table.length);
		Entry<V> prev = table[i];
		Entry<V> e = prev;

		while(e != null)
		{
			Entry<V> next = e.next;
			if(e.hash == hash && e.equals(entry))
			{
				modCount++;
				size--;
				if(prev == e)
				{
					table[i] = next;
				}
				else
				{
					prev.next = next;
				}
				e.recordRemoval(this);
				return e;
			}
			prev = e;
			e = next;
		}

		return e;
	}

	/**
	 * Removes all of the mappings from this map.
	 * The map will be empty after this call returns.
	 */
	public void clear()
	{
		modCount++;
		Entry[] tab = table;
		for(int i = 0; i < tab.length; i++)
		{
			tab[i] = null;
		}
		size = 0;
	}

	/**
	 * Returns <tt>true</tt> if this map maps one or more keys to the
	 * specified value.
	 *
	 * @param value value whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map maps one or more keys to the
	 *         specified value
	 */
	public boolean containsValue(Object value)
	{
		if(value == null)
		{
			return containsNullValue();
		}

		Entry[] tab = table;
		for(int i = 0; i < tab.length; i++)
		{
			for(Entry e = tab[i]; e != null; e = e.next)
			{
				if(value.equals(e.value))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Special-case code for containsValue with null argument
	 */
	private boolean containsNullValue()
	{
		Entry[] tab = table;
		for(int i = 0; i < tab.length; i++)
		{
			for(Entry e = tab[i]; e != null; e = e.next)
			{
				if(e.value == null)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns a shallow copy of this <tt>HashMap</tt> instance: the keys and
	 * values themselves are not cloned.
	 *
	 * @return a shallow copy of this map
	 */
	public Object clone()
	{
		HashIntObjectMap<V> result = null;
		try
		{
			result = (HashIntObjectMap<V>) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			// assert false;
		}
		result.table = new Entry[table.length];
		result.entrySet = null;
		result.modCount = 0;
		result.size = 0;
		result.init();
		result.putAllForCreate(this);

		return result;
	}

	static class Entry<V> implements IntObjectMap.Entry<V>
	{
		final int key;
		V value;
		Entry<V> next;
		final int hash;

		/**
		 * Creates new entry.
		 */
		Entry(int h, int k, V v, Entry<V> n)
		{
			value = v;
			next = n;
			key = k;
			hash = h;
		}

		public final int getKey()
		{
			return key;
		}

		public final V getValue()
		{
			return value;
		}

		public final V setValue(V newValue)
		{
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		public final boolean equals(Object o)
		{
			if(!(o instanceof IntObjectMap.Entry))
			{
				return false;
			}
			IntObjectMap.Entry e = (IntObjectMap.Entry) o;
			int k1 = getKey();
			int k2 = e.getKey();
			if(k1 == k2)
			{
				Object v1 = getValue();
				Object v2 = e.getValue();
				if(v1 == v2 || (v1 != null && v1.equals(v2)))
				{
					return true;
				}
			}
			return false;
		}

		public final int hashCode()
		{
			return key ^ (value == null ? 0 : value.hashCode());
		}

		public final String toString()
		{
			return getKey() + "=" + getValue();
		}

		/**
		 * This method is invoked whenever the value in an entry is
		 * overwritten by an invocation of put(k,v) for a key k that's already
		 * in the HashMap.
		 */
		void recordAccess(HashIntObjectMap<V> m)
		{
		}

		/**
		 * This method is invoked whenever the entry is
		 * removed from the table.
		 */
		void recordRemoval(HashIntObjectMap<V> m)
		{
		}
	}

	/**
	 * Adds a new entry with the specified key, value and hash code to
	 * the specified bucket.  It is the responsibility of this
	 * method to resize the table if appropriate.
	 * <p/>
	 * Subclass overrides this to alter the behavior of put method.
	 */
	void addEntry(int hash, int key, V value, int bucketIndex)
	{
		Entry<V> e = table[bucketIndex];
		table[bucketIndex] = new Entry<V>(hash, key, value, e);
		if(size++ >= threshold)
		{
			resize(2 * table.length);
		}
	}

	/**
	 * Like addEntry except that this version is used when creating entries
	 * as part of Map construction or "pseudo-construction" (cloning,
	 * deserialization).  This version needn't worry about resizing the table.
	 * <p/>
	 * Subclass overrides this to alter the behavior of HashMap(Map),
	 * clone, and readObject.
	 */
	void createEntry(int hash, int key, V value, int bucketIndex)
	{
		Entry<V> e = table[bucketIndex];
		table[bucketIndex] = new Entry<V>(hash, key, value, e);
		size++;
	}

	private abstract class HashIterator<E> implements Iterator<E>
	{
		Entry<V> next;	// next entry to return
		int expectedModCount;	// For fast-fail
		int index;		// current slot
		Entry<V> current;	// current entry

		HashIterator()
		{
			expectedModCount = modCount;
			if(size > 0)
			{ // advance to first entry
				Entry[] t = table;
				while(index < t.length && (next = t[index++]) == null)
				{
					;
				}
			}
		}

		public final boolean hasNext()
		{
			return next != null;
		}

		final Entry<V> nextEntry()
		{
			if(modCount != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
			Entry<V> e = next;
			if(e == null)
			{
				throw new NoSuchElementException();
			}

			if((next = e.next) == null)
			{
				Entry[] t = table;
				while(index < t.length && (next = t[index++]) == null)
				{
					;
				}
			}
			current = e;
			return e;
		}

		public void remove()
		{
			if(current == null)
			{
				throw new IllegalStateException();
			}
			if(modCount != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
			int k = current.key;
			current = null;
			HashIntObjectMap.this.removeEntryForKey(k);
			expectedModCount = modCount;
		}
	}

	private abstract class HashIntIterator implements IntIterator
	{
		Entry<V> next;	// next entry to return
		int expectedModCount;	// For fast-fail
		int index;		// current slot
		Entry<V> current;	// current entry

		HashIntIterator()
		{
			expectedModCount = modCount;
			if(size > 0)
			{ // advance to first entry
				Entry[] t = table;
				while(index < t.length && (next = t[index++]) == null)
				{
					;
				}
			}
		}

		public final boolean hasNext()
		{
			return next != null;
		}

		final Entry<V> nextEntry()
		{
			if(modCount != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
			Entry<V> e = next;
			if(e == null)
			{
				throw new NoSuchElementException();
			}

			if((next = e.next) == null)
			{
				Entry[] t = table;
				while(index < t.length && (next = t[index++]) == null)
				{
					;
				}
			}
			current = e;
			return e;
		}

		public void remove()
		{
			if(current == null)
			{
				throw new IllegalStateException();
			}
			if(modCount != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
			int k = current.key;
			current = null;
			HashIntObjectMap.this.removeEntryForKey(k);
			expectedModCount = modCount;
		}
	}

	private final class ValueIterator extends HashIterator<V>
	{
		public V next()
		{
			return nextEntry().value;
		}
	}

	private final class KeyIterator extends HashIntIterator
	{
		public int next()
		{
			return nextEntry().getKey();
		}
	}

	private final class EntryIterator extends HashIterator<IntObjectMap.Entry<V>>
	{
		public IntObjectMap.Entry<V> next()
		{
			return nextEntry();
		}
	}

	// Subclass overrides these to alter behavior of views' iterator() method
	IntIterator newKeyIterator()
	{
		return new KeyIterator();
	}

	Iterator<V> newValueIterator()
	{
		return new ValueIterator();
	}

	Iterator<IntObjectMap.Entry<V>> newEntryIterator()
	{
		return new EntryIterator();
	}


	// Views

	private transient Set<IntObjectMap.Entry<V>> entrySet = null;

	/**
	 * Returns a {@link Set} view of the keys contained in this map.
	 * The set is backed by the map, so changes to the map are
	 * reflected in the set, and vice-versa.  If the map is modified
	 * while an iteration over the set is in progress (except through
	 * the iterator's own <tt>remove</tt> operation), the results of
	 * the iteration are undefined.  The set supports element removal,
	 * which removes the corresponding mapping from the map, via the
	 * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
	 * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
	 * operations.
	 */
	public IntSet keySet()
	{
		IntSet ks = keySet;
		return (ks != null ? ks : (keySet = new KeySet()));
	}

	private final class KeySet extends AbstractIntSet
	{
		public IntIterator iterator()
		{
			return newKeyIterator();
		}

		public int size()
		{
			return size;
		}

		public boolean contains(int o)
		{
			return containsKey(o);
		}

		public boolean remove(int o)
		{
			return HashIntObjectMap.this.removeEntryForKey(o) != null;
		}

		public void clear()
		{
			HashIntObjectMap.this.clear();
		}
	}

	/**
	 * Returns a {@link Collection} view of the values contained in this map.
	 * The collection is backed by the map, so changes to the map are
	 * reflected in the collection, and vice-versa.  If the map is
	 * modified while an iteration over the collection is in progress
	 * (except through the iterator's own <tt>remove</tt> operation),
	 * the results of the iteration are undefined.  The collection
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
	 * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
	 * support the <tt>add</tt> or <tt>addAll</tt> operations.
	 */
	public Collection<V> values()
	{
		Collection<V> vs = values;
		return (vs != null ? vs : (values = new Values()));
	}

	private final class Values extends AbstractCollection<V>
	{
		public Iterator<V> iterator()
		{
			return newValueIterator();
		}

		public int size()
		{
			return size;
		}

		public boolean contains(Object o)
		{
			return containsValue(o);
		}

		public void clear()
		{
			HashIntObjectMap.this.clear();
		}
	}

	/**
	 * Returns a {@link Set} view of the mappings contained in this map.
	 * The set is backed by the map, so changes to the map are
	 * reflected in the set, and vice-versa.  If the map is modified
	 * while an iteration over the set is in progress (except through
	 * the iterator's own <tt>remove</tt> operation, or through the
	 * <tt>setValue</tt> operation on a map entry returned by the
	 * iterator) the results of the iteration are undefined.  The set
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
	 * <tt>clear</tt> operations.  It does not support the
	 * <tt>add</tt> or <tt>addAll</tt> operations.
	 *
	 * @return a set view of the mappings contained in this map
	 */
	public Set<IntObjectMap.Entry<V>> entrySet()
	{
		return entrySet0();
	}

	private Set<IntObjectMap.Entry<V>> entrySet0()
	{
		Set<IntObjectMap.Entry<V>> es = entrySet;
		return es != null ? es : (entrySet = new EntrySet());
	}

	private final class EntrySet extends AbstractSet<IntObjectMap.Entry<V>>
	{
		public Iterator<IntObjectMap.Entry<V>> iterator()
		{
			return newEntryIterator();
		}

		public boolean contains(Object o)
		{
			if(!(o instanceof Map.Entry))
			{
				return false;
			}
			IntObjectMap.Entry<V> e = (IntObjectMap.Entry<V>) o;
			Entry<V> candidate = getEntry(e.getKey());
			return candidate != null && candidate.equals(e);
		}

		public boolean remove(Object o)
		{
			return removeMapping(o) != null;
		}

		public int size()
		{
			return size;
		}

		public void clear()
		{
			HashIntObjectMap.this.clear();
		}
	}

	/**
	 * Save the state of the <tt>HashMap</tt> instance to a stream (i.e.,
	 * serialize it).
	 *
	 * @serialData The <i>capacity</i> of the HashMap (the length of the
	 * bucket array) is emitted (int), followed by the
	 * <i>size</i> (an int, the number of key-value
	 * mappings), followed by the key (Object) and value (Object)
	 * for each key-value mapping.  The key-value mappings are
	 * emitted in no particular order.
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws IOException
	{
		Iterator<IntObjectMap.Entry<V>> i = (size > 0) ? entrySet0().iterator() : null;

		// Write out the threshold, loadfactor, and any hidden stuff
		s.defaultWriteObject();

		// Write out number of buckets
		s.writeInt(table.length);

		// Write out size (number of Mappings)
		s.writeInt(size);

		// Write out keys and values (alternating)
		if(i != null)
		{
			while(i.hasNext())
			{
				IntObjectMap.Entry<V> e = i.next();
				s.writeInt(e.getKey());
				s.writeObject(e.getValue());
			}
		}
	}

	private static final long serialVersionUID = 362498820763181265L;

	/**
	 * Reconstitute the <tt>HashMap</tt> instance from a stream (i.e.,
	 * deserialize it).
	 */
	private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException
	{
		// Read in the threshold, loadfactor, and any hidden stuff
		s.defaultReadObject();

		// Read in number of buckets and allocate the bucket array;
		int numBuckets = s.readInt();
		table = new Entry[numBuckets];

		init();  // Give subclass a chance to do its thing.

		// Read in size (number of Mappings)
		int size = s.readInt();

		// Read the keys and values, and put the mappings in the HashMap
		for(int i = 0; i < size; i++)
		{
			int key = s.readInt();
			V value = (V) s.readObject();
			putForCreate(key, value);
		}
	}

	// These methods are used when serializing HashSets
	public int capacity()
	{
		return table.length;
	}

	public float loadFactor()
	{
		return loadFactor;
	}
}
