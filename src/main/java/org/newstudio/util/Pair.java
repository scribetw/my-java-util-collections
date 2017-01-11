package org.newstudio.util;

/**
 * Pair 資料型態，可以儲存一對數值。
 */
public class Pair<T1, T2> {
	private final T1 _1;
	private final T2 _2;

	private Pair(T1 t1, T2 t2) {
		_1 = t1;
		_2 = t2;
	}

	public static <T1, T2> Pair<T1, T2> of(T1 t1, T2 t2) {
		return new Pair<>(t1, t2);
	}

	public T1 get_1() {
		return _1;
	}

	public T2 get_2() {
		return _2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Pair<?, ?> pair = (Pair<?, ?>) o;

		if (!_1.equals(pair._1)) return false;
		return _2.equals(pair._2);
	}

	@Override
	public int hashCode() {
		int result = _1.hashCode();
		result = 31 * result + _2.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "(" + _1 + ", " + _2 + ")";
	}

	/**
	 * 對調元素。
	 * 第一個變第二個，第二個變第一個。
	 *
	 * @return 對調後 Pair
	 */
	public Pair<T2, T1> swap() {
		return Pair.of(_2, _1);
	}
}
