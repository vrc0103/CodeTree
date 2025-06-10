import java.io.*;
import java.util.*;

public class Main {
    static StringBuilder sb;
    static int idx;
    static int res;
    static List<Integer> map;
    static List<Integer> exist;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();

        map = new ArrayList<>();
        map.add(0);
        exist = new ArrayList<>();
        exist.add(idx++);

        // init
        int cnt = Integer.parseInt(br.readLine().trim()) - 1;
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        st.nextToken(); // 첫 숫자는 무조건 100
        int num = Integer.parseInt(st.nextToken());

        while(num-- > 0) {
            map.add(Integer.parseInt(st.nextToken()));
            exist.add(idx++);
        }

        // cmd
        while(cnt-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == 200) {
                int loc = Integer.parseInt(st.nextToken());
                map.add(loc);
                exist.add(idx++);
            } else if(cmd == 300) {
                int loc = Integer.parseInt(st.nextToken());
                exist.remove(Collections.binarySearch(exist, loc));
            } else {
                int ant = Integer.parseInt(st.nextToken());
                find(ant);
            }
        }

        System.out.print(sb);
    }

    static void find(int num) {
        int[] start = new int[num];
        res = Integer.MAX_VALUE;

        int left = 0;
        int right = map.get(exist.get(exist.size() - 1)) - map.get(exist.get(1));

        while(left <= right) {
            int cnt = 1;
            int mid = (left + right) / 2;
            int now = map.get(exist.get(1));

            for(int i = 1; i < exist.size(); i++) {
                if(map.get(exist.get(i)) - now <= mid) {
                    continue;
                }

                now = map.get(exist.get(i));
                cnt++;
            }

            if(cnt <= num) {
                right = mid - 1;
                res = mid;
            } else {
                left = mid + 1;
            }

            // System.out.printf("개미 : %d , 거리 : %d , 결과 : %d\n", cnt, mid, res);
        }

        // System.out.println();

        sb.append(res).append("\n");
    }
}