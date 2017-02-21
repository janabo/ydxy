package com.dk.mp.oldoa.utils;


import com.dk.mp.oldoa.R;

public class AnimUtil {
	public static final int ANIM_LEFT_TO_RIGHT = 1;
	public static final int ANIM_RIGHT_TO_LEFT = 2;
	public static final int ANIM_UP_TO_DOWN = 3;
	public static final int ANIM_DOWM_TO_UP = 4;
	public static final int ANIM_ZOOMIN_FADEOUT = 5;
	public static final int ANIM_TURN_FADEOUT1 = 6;
	public static final int ANIM_TURN_FADEOUT2 = 7;
	public static final int ANIM_LEFT_TOP_FADEOUT = 8;
	public static final int ANIM_ZOOMOUT_FADEOUT = 9;
	public static final int ANIM_CROSS_LEFT_RIGHT = 10;
	public static final int ANIM_LEFT_TOP_ZOOMIN2 = 11;
	public static final int ANIM_CROSS_UP_DOWN = 12;
	public static final int ANIM_FADEIN_FADEOUT = 13;
	public static final int ANIM_ZOOMIN_FADEOUT2 = 14;
	public static final int ANIM_DEFAULT = 100;
	
	/**
	 * 获取返回的动画动作(相反).
	 * @param animType 前进动画
	 * @return 返回动画
	 */
	public static int getBackAnim(final int animType) {
		int backAnim = ANIM_DEFAULT;
		switch (animType) {
		case ANIM_LEFT_TO_RIGHT:
			backAnim = ANIM_RIGHT_TO_LEFT;
			break;
		case ANIM_UP_TO_DOWN:
			backAnim = ANIM_DOWM_TO_UP;
			break;
		case ANIM_RIGHT_TO_LEFT:
			backAnim = ANIM_LEFT_TO_RIGHT;
			break;
		case ANIM_DOWM_TO_UP:
			backAnim = ANIM_UP_TO_DOWN;
			break;
		default:
			backAnim = ANIM_RIGHT_TO_LEFT;
			break;
		}
		return backAnim;
	}

	/**
	 * 跳转页面动画.
	 * @param animType  跳转动画类型<br>
	 *  ANIM_FADEIN_FADEOUT://淡入淡出效果<br>
	 *  ANIM_ZOOMIN_FADEOUT://放大淡出效果<br>
	 *  ANIM_URN_FADEOUT1://转动淡出效果1<br>
	 *  ANIM_TURN_FADEOUT2://转动淡出效果2<br>
	 *  ANIM_LEFT_TOP_FADEOUT://左上角展开淡出效果<br>
	 *  ANIM_ZOOMOUT_FADEOUT://压缩变小淡出效果<br>
	 *  ANIM_RIGHT_TO_LEFT://左往右推出效果<br>
	 *  ANIM_LEFT_TO_RIGHT://右往左推出效果<br>
	 *  ANIM_UP_TO_DOWN://上往下推出效果<br>
	 *  ANIM_DOWM_TO_UP://下往上推出效果<br>
	 *  ANIM_CROSS_LEFT_RIGHT://左右交错效果<br>
	 *  ANIM_ZOOMIN_FADEOUT2://放大淡出效果<br>
	 *  ANIM_LEFT_TOP_ZOOMIN2://左上角缩小<br>
	 *  ANIM_ROSS_UP_DOWN://上下交错效果<br>
	 */
	public static int[] startAnim(int animType) {
		int[] s = new int[2];
		switch (animType) {
//		case ANIM_FADEIN_FADEOUT://淡入淡出效果
//			/*注意：此方法只能在startActivity和finish方法之后调用。
//			  第一个参数为第一个Activity离开时的动画，第二参数为所进入的Activity的动画效果*/
//			s[0]=R.anim.fade;
//			s[1]=R.anim.hold;
//			break;
//		case ANIM_ZOOMIN_FADEOUT://放大淡出效果
//			s[0]=R.anim.my_scale_action;
//			s[1]=R.anim.my_alpha_action;
//			//overridePendingTransition(R.anim.my_scale_action, R.anim.my_alpha_action);
//			break;
//		case ANIM_TURN_FADEOUT1://转动淡出效果1
//			s[0]=R.anim.scale_rotate;
//			s[1]=R.anim.my_alpha_action;
//			//overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
//			break;
//		case ANIM_TURN_FADEOUT2://转动淡出效果2
//			s[0]=R.anim.scale_translate_rotate;
//			s[1]=R.anim.my_alpha_action;
//			//overridePendingTransition(R.anim.scale_translate_rotate, R.anim.my_alpha_action);
//			break;
//		case ANIM_LEFT_TOP_FADEOUT://左上角展开淡出效果
//			s[0]=R.anim.scale_translate;
//			s[1]=R.anim.my_alpha_action;
//			//overridePendingTransition(R.anim.scale_translate, R.anim.my_alpha_action);
//			break;
//		case ANIM_ZOOMOUT_FADEOUT://压缩变小淡出效果
//			s[0]=R.anim.hyperspace_in;
//			s[1]=R.anim.hyperspace_out;
//			//overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
//			break;
		case ANIM_RIGHT_TO_LEFT://左往右推出效果
			s[0]= R.anim.push_right_in;
			s[1]=R.anim.push_right_out;
			//overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case ANIM_LEFT_TO_RIGHT://右往左推出效果
			s[0]=R.anim.push_left_in;
			s[1]=R.anim.push_left_out;
			//overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case ANIM_UP_TO_DOWN://上往下推出效果
			s[0]=R.anim.push_down_in;
			s[1]=R.anim.push_down_out;
			//overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case ANIM_DOWM_TO_UP://下往上推出效果
			s[0]=R.anim.push_up_in;
			s[1]=R.anim.push_up_out;
			//overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			break;
		case ANIM_CROSS_LEFT_RIGHT://左右交错效果
			s[0]=R.anim.slide_left;
			s[1]=R.anim.slide_right;
			//overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
			break;
//		case ANIM_ZOOMIN_FADEOUT2://放大淡出效果
//			s[0]=R.anim.wave_scale;
//			s[1]=R.anim.my_alpha_action;
//			//overridePendingTransition(R.anim.wave_scale, R.anim.my_alpha_action);
//			break;
//		case ANIM_LEFT_TOP_ZOOMIN2://左上角缩小
//			s[0]=R.anim.zoom_enter;
//			s[1]=R.anim.zoom_exit;
//			//overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
//			break;
		case ANIM_CROSS_UP_DOWN://上下交错效果
			s[0]=R.anim.slide_up_in;
			s[1]=R.anim.slide_down_out;
			//overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
			break;
		}
		return s;
	}

}
