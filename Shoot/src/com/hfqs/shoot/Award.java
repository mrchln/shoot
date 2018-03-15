package com.hfqs.shoot;
/**
 * 奖励
 * @author hanfuqingshi
 *
 */
public interface Award {

	int DOUBLE_FIRE = 0;//双倍火力
	int LIFT = 1;//1条命
	/**
	 * 获得奖励类型（上面的0或1）
	 */
	int getType();
}
