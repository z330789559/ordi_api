package cn.iocoder.yudao.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Test
 *
 * @author libaozhong
 * @version 2024-02-29
 **/

public class Test {
	public int uniquePaths(int m, int n) {
		int[] dp = new int[m];
		int j;
		for(int i = 0; i < n; i++) {
			for(j = 0; j < m; j++) {
				if(j == 0) {
					dp[j] = 1;
				}
				else {
					// 其他的点都可以由左边的点和上面的点到达
					dp[j] += dp[j-1];
				}
			}
		}

		return dp[m-1];
	}

}
