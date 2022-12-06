#include <iostream>
#include <map>

using namespace std;

#define M 64
#define N 60
#define K 32

const int CACHE_WAY = 2;
const int CACHE_SETS_COUNT = 32;

const int CACHE_HIT = 6;
const int CACHE_MISS = 4;
const int MEM_CTR = 100;
const int DATA_TRANSPORT = 1;
const int D1_BUS_DATA = 16;
const int D2_BUS_DATA = 16;

int my_time = 0;
int cache_hits = 0, cache_queries = 0;


//read

//write

struct cache_line {
	int valid, dirty, age;
	int tag, data;

	cache_line() {
		valid = 0;
		tag = -1;
	}

	cache_line(int valid, int dirty, int age, int tag, int data) {
		this -> valid = valid;
		this -> dirty = dirty;
		this -> age = age;
		this -> tag = tag;
		this -> data = data;
	}
};

cache_line cache[32][2];

void read(int addr) {
	cache_queries++;
	int tag = (addr >> 9);
	int set = ((addr >> 4) & ((1 << 5) - 1));
	int offset = (addr & ((1 << 4) - 1));

	if (cache[set][0].tag == tag) {
		cache_hits++;
		my_time += CACHE_HIT;
		cache[set][0].age = 0;
		cache[set][1].age = 1;
		return;
	} 

	if (cache[set][1].tag == tag) {
		cache_hits++;
		my_time += CACHE_HIT;
		cache[set][1].age = 0;
		cache[set][0].age = 1;
		return;
	}

	my_time += CACHE_MISS;
	int num = -1;

	if (cache[set][0].valid == 0) {
		num = 0;
	} else if (cache[set][1].valid == 0) {
		num = 1;
	}

	if (num == -1) {
		if (cache[set][0].age == 1) {
			num = 0;
		} else {
			num = 1;
		}
	}

	if (cache[set][num].valid != 0 && cache[set][num].dirty == 1) {
		my_time += MEM_CTR;
	}
	my_time += MEM_CTR;
	cache[set][num] = cache_line(1, 0, 0, tag, 1010);
	cache[set][1 - num].age = 1;
}

void write(int addr) {
	cache_queries++;
	int tag = (addr >> 9);
	int set = ((addr >> 4) & ((1 << 5) - 1));
	int offset = (addr & ((1 << 4) - 1));
	
	if (cache[set][0].tag == tag) {
		cache_hits++;
		my_time += CACHE_HIT;
		cache[set][0].dirty = 1;
		cache[set][0].age = 0;
		cache[set][1].age = 1;
		return;
	} 

	if (cache[set][1].tag == tag) {
		cache_hits++;
		my_time += CACHE_HIT;
		cache[set][1].dirty = 1;
		cache[set][1].age = 0;
		cache[set][0].age = 1;
		return;
	}

	my_time += CACHE_MISS;
	int num = -1;

	if (cache[set][0].valid == 0) {
		num = 0;
	} else if (cache[set][1].valid == 0) {
		num = 1;
	}

	if (num == -1) {
		if (cache[set][0].age == 1) {
			num = 0;
		} else {
			num = 1;
		}
	}

	if (cache[set][num].valid != 0 && cache[set][num].dirty == 1) {
		my_time += MEM_CTR;
	}
	my_time += MEM_CTR;
	cache[set][num] = cache_line(1, 1, 0, tag, 1010);
	cache[set][1 - num].age = 1;

}

int main() {
	for (int i = 0; i < 32; i++) {
		for (int j = 0; j < 2; j++) {
			cache[i][j] = cache_line();
		}
	}

	int pa, pb, pc;
	pa = 0;
	my_time++;
	pc = 5888;
	my_time++;
	int addr = 0;
	my_time++;
	for (int y = 0; y < M; y++) {
		my_time++;
		my_time++;
		for (int x = 0; x < N; x++) {
			my_time++;
			my_time++;
			pb = 2048;
			my_time += 2;
			for (int k = 0; k < K; k++) {
				my_time++;
				addr = (pa + k);
				read(addr);
				my_time += 2;
				addr = (pb + 2 * x);
				my_time++;
				read(addr);
				my_time += 2;
				pb += (2 * N);
				my_time++;
				my_time += 6;
			}
			addr = (pc + 4 * x);
			my_time++;
			write(addr);
			my_time += 32 / 16;
		}
		pc += 4 * N;
		pa += K;
		my_time += 2;
	}

	cout << "cache_hits: " << cache_hits << ", cache_queries: " << cache_queries << '\n';
	cout << "tacts: " << my_time + 1 << '\n'; 
	return 0;
}