import java.io.*;
import java.util.*;

public class Main {
    static StringBuilder sb;
    static int next;
    static int cnt;
    static int res;
    static List<Integer> map;
    static List<Integer> exist;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();

        map = new ArrayList<>();
        map.add(0);
        exist = new ArrayList<>();
        exist.add(next++);

        // init
        cnt = Integer.parseInt(br.readLine().trim()) - 1;
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        st.nextToken(); // 첫 숫자는 무조건 100
        int num = Integer.parseInt(st.nextToken());

        while(num-- > 0) {
            map.add(Integer.parseInt(st.nextToken()));
            exist.add(next++);
        }

        // cmd
        while(cnt-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == 200) {
                int loc = Integer.parseInt(st.nextToken());
                map.add(loc);
                exist.add(next++);
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

        start[0] = 0;
        comb(1, num, start);

        start[0] = exist.get(1);
        comb(1, num, start);

        sb.append(res).append("\n");
    }

    static void comb(int count, int num, int[] start) {
        if(count == num) {
            int max = 0;
            int dist = 0;

            // 각 개미 이동 거리
            for(int i = 1; i < num; i++) {
                int from = map.get(exist.get(start[i - 1]));
                int to = map.get(exist.get(start[i] - 1));

                dist = to - from;
                max = Math.max(max, dist);
            }

            // 마지막 개미 이동 거리
            dist = map.get(exist.size() - 1) - map.get(exist.get(start[num - 1]));
            max = Math.max(max, dist);

            res = Math.min(res, max);

            return;
        }

        for(int i = count; i < exist.size(); i++) {
            start[count] = i;
            comb(count + 1, num, start);
        }
    }
}