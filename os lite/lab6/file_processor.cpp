#include <fstream>
#include <string>


using namespace std;

int main(int argc, char *argv[]) { // 1 - filename
	string file_name = static_cast<string>(argv[1]);
	fstream fstr_in(file_name, fstream::in);
	fstream fstr_out(file_name, fstream::app);

	int size;
	fstr_in >> size;
	
	for (int i = 0; i < size; ++i) {
		int curr_num;
		fstr_in >> curr_num;
		fstr_out << curr_num * 2 << ' ';
	}

	return 0;
}