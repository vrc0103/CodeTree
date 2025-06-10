import java.io.*;
import java.util.*;

public class Main {
    static StringBuilder sb;
    static int cnt;
    static int res;
    static List<Integer> map;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();

        map = new ArrayList<>();
        cnt = Integer.parseInt(br.readLine().trim()) - 1;

        // init
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        st.nextToken(); // 첫 숫자는 무조건 100
        int num = Integer.parseInt(st.nextToken());

        while(num-- > 0) {
            map.add(Integer.parseInt(st.nextToken()));
        }

        // cmd
        while(cnt-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == 200) {
                int loc = Integer.parseInt(st.nextToken());
                map.add(loc);
            } else if(cmd == 300) {
                int loc = Integer.parseInt(st.nextToken());
                map.remove(loc);
            } else {
                int ant = Integer.parseInt(st.nextToken());
                find(ant);
            }
        }

        System.out.print(sb);
    }

    static void find(int num) {
        res = Integer.MAX_VALUE;

        int[] start = new int[num];
        start[0] = 0;

        comb(1, num, start);

        sb.append(res).append("\n");
    }

    static void comb(int count, int num, int[] start) {
        if(count == num) {
            int max = 0;
            int dist = 0;

            // 각 개미 이동 거리
            for(int i = 1; i < num; i++) {
                dist = map.get(start[i] - 1) - map.get(start[i - 1]);
                max = Math.max(max, dist);
            }

            // 마지막 개미 이동 거리
            dist = map.get(map.size() - 1) - map.get(start[num - 1]);
            max = Math.max(max, dist);

            res = Math.min(res, max);

            return;
        }

        for(int i = count; i < map.size(); i++) {
            start[count] = i;
            comb(count + 1, num, start);
        }
    }
}