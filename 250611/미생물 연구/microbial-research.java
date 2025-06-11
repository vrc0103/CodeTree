import java.io.*;
import java.util.*;

public class Main {
    static BufferedReader br;
    static StringTokenizer st;
    static StringBuilder sb;

    static int size;    // <= 15
    static int loop;    // <= 50
    static int idx;
    static int[][] map; // 최대 15 * 15
    static int[] areaSize;
    static List<Area> area;

    static int[] dR = {0, 1, 0, -1};
    static int[] dC = {1, 0, -1, 0};

    static class Area implements Comparable<Area> {
        int num;
        int count;

        public Area(int num, int count) {
            this.num = num;
            this.count = count;
        }

        @Override
        public int compareTo(Area o) {
            if(o.count == this.count) {
                // 면적 같으면 먼저 들어온 미생물부터
                return this.num - o.num;
            }
            // 면적 내림차순
            return o.count - this.count;
        }
    }

    public static void main(String[] args) throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        st = new StringTokenizer(br.readLine().trim());
        size = Integer.parseInt(st.nextToken());
        loop = Integer.parseInt(st.nextToken());
        map = new int[size][size];
        area = new ArrayList<>();
        areaSize = new int[51];

        while(loop-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());

            idx++;

            insert(r1, c1, r2, c2);
            move();
            report();
        }

        System.out.print(sb);
    }

    static void insert(int x1, int y1, int x2, int y2) {
        /*
        * 영역 내 기존 미생물을 덮어씀
        * 기존 미생물의 영역이 둘 이상으로 나뉘면 모두 증발
        */

        ArrayDeque<Integer> removed = new ArrayDeque<>();
        boolean[] checked = new boolean[idx + 1];

        for(int x = x1; x < x2; x++) {
            for(int y = y1; y < y2; y++) {
                if(map[y][x] > 0) {
                    if(!checked[map[y][x]]) {
                        checked[map[y][x]] = true;
                        removed.offer(map[y][x]);
                    }

                    areaSize[map[y][x]]--;

                    for(int i = 0; i < area.size(); i++) {
                        if(area.get(i).num == map[y][x]) {
                            area.get(i).count--;
                        }
                    }
                }

                map[y][x] = idx;
                areaSize[idx]++;
            }
        }

        area.add(new Area(idx, areaSize[idx]));

        // 잡아먹힌 각 미생물 파악
        while(!removed.isEmpty()) {
            int num = removed.poll();
            int split = 0;
            boolean[][] visited = new boolean[size][size];
            
            first: for(int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if(!visited[y][x] && map[y][x] == num) {
                        split++;

                        // 탐색된 미생물이 다시 등장 : 분리된 미생물
                        if(split >= 2) {
                            break first;
                        }

                        ArrayDeque<int[]> bfs = new ArrayDeque<>();

                        bfs.offer(new int[] {y, x});
                        visited[y][x] = true;
                        while(!bfs.isEmpty()) {
                            int[] now = bfs.poll();

                            for(int i = 0; i < 4; i++) {
                                int nextY = now[0] + dR[i];
                                int nextX = now[1] + dC[i];

                                if(nextY < 0 || nextY >= size || nextX < 0 || nextX >= size) {
                                    continue;
                                }

                                if(!visited[nextY][nextX] && map[nextY][nextX] == num) {
                                    visited[nextY][nextX] = true;
                                    bfs.offer(new int[] {nextY, nextX});
                                }
                            }
                        }
                    }
                }
            }

            if(split >= 2) {
                areaSize[num] = 0;

                for(int i = 0; i < area.size(); i++) {
                    if(area.get(i).num == num) {
                        area.get(i).count = 0;
                    }
                }
            }
        }
    }

    static void move() {
        /*
        * 1. 기존 용기에서 가장 큰 무리(동일한 크기이면 먼저 들어온 무리) 선택
        * 2. 동일한 형태를 유지하도록 다른 배양 용기로 이동
        * 3. 다른 미생물 영역과 겹치지 않도록 x좌표 최소 -> y 좌표 최소 순서로 배치
        * 4. 이동이 불가능한 미생물은 증발
        */

        int[][] newMap = new int[size][size];
        Collections.sort(area);

        for(int i = 0; i < area.size(); i++) {
            if(area.get(i).count == 0) {
                area.remove(i);
                i--;
                continue;
            }

            int num = area.get(i).num;
            int minX = size;
            int minY = size;
            int maxX = 0;
            int maxY = 0;
            ArrayDeque<int[]> micro = new ArrayDeque<>();

            for(int r = 0; r < size; r++) {
                for(int c = 0; c < size; c++) {
                    if(map[r][c] == num) {
                        micro.offer(new int[] {r, c});
                        minX = Math.min(minX, c);
                        minY = Math.min(minY, r);
                        maxX = Math.max(maxX, c);
                        maxY = Math.max(maxY, r);
                    }
                }
            }

            int count = micro.size();

            // System.out.printf("num : %d , count : %d\n", num, count);

            // (0, 0)부터 시작
            for(int j = 0; j < count; j++) {
                int[] now = micro.poll();
                now[0] -= minY;
                now[1] -= minX;
                micro.offer(now);
            }

            maxX -= minX;
            maxY -= minY;

            // System.out.printf("num : %d - (0, 0) ~ (%d, %d)\n", num, maxY, maxX);
            
            boolean canLoc = true;

            first: for(int moveX = 0; moveX < size - maxX; moveX++) {
                for(int moveY = 0; moveY < size - maxY; moveY++) {
                    canLoc = true;

                    // System.out.printf("move : (%d, %d)\n", moveX, moveY);

                    for(int j = 0; j < count; j++) {
                        int[] now = micro.poll();

                        if(newMap[now[0] + moveY][now[1] + moveX] > 0) {
                            canLoc = false;
                        }

                        micro.offer(now);
                    }

                    if(canLoc) {
                        // 이동
                        while(!micro.isEmpty()) {
                            int[] now = micro.poll();
                            
                            newMap[now[0] + moveY][now[1] + moveX] = num;
                        }

                        break first;
                    }
                }
            }
        }

        map = newMap;

        // System.out.println("move");
        // for(int i = size - 1; i >= 0; i--) {
        //     System.out.println(Arrays.toString(newMap[i]));
        // }
    }

    static void report() {
        /*
        * 1. 인접한(상하좌우) 무리 확인
        * 2. 두 무리 영역의 넓이를 합친 값만큼을 성과로 계산
        */

        boolean[][] near = new boolean[idx + 1][idx + 1];
        boolean[][] visited = new boolean[size][size];

        for(int r = 0; r < size; r++) {
            for(int c = 0; c < size; c++) {
                if(!visited[r][c] && map[r][c] > 0) {
                    int num = map[r][c];
                    ArrayDeque<int[]> bfs = new ArrayDeque<>();

                    bfs.offer(new int[] {r, c});
                    visited[r][c] = true;

                    while(!bfs.isEmpty()) {
                        int[] now = bfs.poll();

                        for(int i = 0; i < 4; i++) {
                            int nextR = now[0] + dR[i];
                            int nextC = now[1] + dC[i];

                            if(nextR < 0 || nextR >= size || nextC < 0 || nextC >= size) {
                                continue;
                            }

                            if(!visited[nextR][nextC] && map[nextR][nextC] == num) {
                                bfs.offer(new int[] {nextR, nextC});
                                visited[nextR][nextC] = true;
                            }
                            
                            if(map[nextR][nextC] != num) {
                                int nearNum = map[nextR][nextC];
                                near[num][nearNum] = true;
                                near[nearNum][num] = true;
                            }
                        }
                    }
                }
            }
        }

        int point = 0;

        for(int r = 1; r <= idx; r++) {
            for(int c = r; c <= idx; c++) {
                if(near[r][c]) {
                    point += areaSize[r] * areaSize[c];
                }
            }
        }

        sb.append(point).append("\n");
    }
}