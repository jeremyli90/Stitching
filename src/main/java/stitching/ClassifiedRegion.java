/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * An execption is the FFT implementation of Dave Hale which we use as a library,
 * wich is released under the terms of the Common Public License - v1.0, which is 
 * available at http://www.eclipse.org/legal/cpl-v10.html  
 *
 * @author Mark Hiner, Stephan Preibisch
 */

package stitching;

import java.util.HashSet;
import java.util.Set;

/**
 * Representation of a N-dimensional region with one or more classes/labels
 * applied to it. Regions are effectively just collections of one or more
 * {@link Interval}s.
 * 
 * @author Mark Hiner hinerm at gmail.com
 */
public class ClassifiedRegion {

	private final Interval[] intervals;
	private final Set<Integer> classes = new HashSet<Integer>();
	private int[] ints = null;

	/**
	 * Creates an empty {@code ClassifiedRegion} of the indicated size
	 * (dimensionality).
	 */
	public ClassifiedRegion(final int size) {
		intervals = new Interval[size];
	}

	/**
	 * Creates a new region using the given list of intervals.
	 */
	public ClassifiedRegion(final Interval... intervals) {
		this.intervals = intervals;
	}

	/**
	 * Copying constructor. Creates a shallow copy.
	 */
	public ClassifiedRegion(final ClassifiedRegion copy) {
		this.intervals = copy.intervals;
	}

	// Public API

	/**
	 * Add a class, in the form an int value, to this region.
	 */
	public void addClass(final int label) {
		classes.add(label);
	}

	/**
	 * Merges the classes attached to another region with the classes of this
	 * region.
	 */
	public void addAllClasses(final ClassifiedRegion region) {
		classes.addAll(region.classes);
	}

	/**
	 * @return A primitive array of all classes associated with this region.
	 */
	public int[] classArray() {
		if (ints == null || ints.length != classes.size()) {
			ints = new int[classes.size()];
		}
		int index = 0;
		for (final Integer i : classes) {
			ints[index++] = i;
		}
		return ints;
	}

	/**
	 * Sets the interval of this region for the specified axis (index)
	 */
	public void set(final Interval interval, final int index) {
		intervals[index] = interval;
	}

	/**
	 * Gets the interval of this region for the specified axis (index)
	 */
	public Interval get(final int index) {
		return intervals[index];
	}

	/**
	 * @return The dimensionality of this region
	 */
	public int size() {
		return intervals.length;
	}

	/**
	 * As {@link #intersects(ClassifiedRegion, boolean)} with
	 * {@code ignoreOverlap = false}.
	 */
	public boolean intersects(final ClassifiedRegion other) {
		return intersects(other, false);
	}

	/**
	 * Returns true if this region intersects with the provided target region. If
	 * {@code ignoreOverlap} is true, both regions will be treated as sets of
	 * exclusive intervals.
	 */
	public boolean intersects(final ClassifiedRegion other,
		final boolean ignoreOverlap)
	{
		boolean matches = true;
		for (int i = 0; matches && i < Math.min(other.size(), size()); i++) {
			matches = get(i).intersects(other.get(i), ignoreOverlap);
		}
		return matches;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < intervals.length; i++) {
			s += intervals[i] + "; ";
		}
		return s;
	}
}