#include <iostream>
#include <cstdlib>

using namespace std;

int main(int argc, char* argv[]) {
    int a = std::atoi(argv[1]);  // 将命令行参数转换为整数
    int b = std::atoi(argv[2]);

    cout << "结果：" << (a + b) << endl;  // 输出结果
    return 0;
}

