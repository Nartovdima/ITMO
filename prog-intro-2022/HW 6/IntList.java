public class IntList {
	private int[] data;
	private int size;

	public IntList() {
		data = new int[1];
		size = 0;
	}

	public void add(int element) {
		if (size == data.length) {
			data = java.util.Arrays.copyOf(data, data.length * 2);
		}
		data[size] = element;
		size++;
	}

	public int get(int index) {
		return data[index];
	}

	public int size() {
		return size;
	}

	public int capacity() {
		return data.length;
	}

	@Override
	public String toString() {
		StringBuffer prnt = new StringBuffer();
		for (int i = 0; i < size; i++) {
			if (i != size - 1)
				prnt.append(data[i] + " ");
			else
				prnt.append(data[i]);
		}
		return prnt.toString();
	}
}
