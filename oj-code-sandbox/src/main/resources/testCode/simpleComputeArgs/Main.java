public class Main {
    public static void main(String[] args) {
        // 至少需要输入：n（长度）+ k（阈值）+ n 个数组元素
        if (args.length < 2) {
            return;
        }

        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);

        if (args.length < 2 + n) {
            return;
        }

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(args[i + 2]);
        }

        int count = 0;

        if (n >= 3) {
            for (int i = 0; i <= n - 3; i++) {
                int xorValue = arr[i] ^ arr[i + 1] ^ arr[i + 2];
                if (xorValue >= k) {
                    count++;
                }
            }
        }

        System.out.println("结果是：" + count);
    }
}

