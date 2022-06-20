/*
 * Copyright (c) 2012 Jason Polites
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.polites.android;

import android.graphics.PointF;
import java.lang.Math;
import android.view.MotionEvent;

public class MathUtils {
	
	public static double distance(MotionEvent event) {
		double x = event.getX(0) - event.getX(1);
		double y = event.getY(0) - event.getY(1);
		return Math.sqrt(x * x + y * y);
	}
	
	public static double distance(PointF p1, PointF p2) {
		double x = p1.x - p2.x;
		double y = p1.y - p2.y;
		return Math.sqrt(x * x + y * y);
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		double x = x1 - x2;
		double y = y1 - y2;
		return Math.sqrt(x * x + y * y);
	}

	public static void midpoint(MotionEvent event, PointF point) {
		double x1 = event.getX(0);
		double y1 = event.getY(0);
		double x2 = event.getX(1);
		double y2 = event.getY(1);
		midpoint(x1, y1, x2, y2, point);
	}

	public static void midpoint(double x1, double y1, double x2, double y2, PointF point) {
		point.x = (float) (x1 + x2) / 2.0f;
		point.y = (float) (y1 + y2) / 2.0f;
	}
	/**
	 * Rotates p1 around p2 by angle degrees.
	 * @param p1
	 * @param p2
	 * @param angle
	 */
	public void rotate(PointF p1, PointF p2, double angle) {
		double px = p1.x;
		double py = p1.y;
		double ox = p2.x;
		double oy = p2.y;
		p1.x = (float) (Math.cos(angle) * (px-ox) - Math.sin(angle) * (py-oy) + ox);
		p1.y = (float) (Math.sin(angle) * (px-ox) + Math.cos(angle) * (py-oy) + oy);
	}
	
	public static double angle(PointF p1, PointF p2) {
		return angle(p1.x, p1.y, p2.x, p2.y);
	}	
	
	public static double angle(double x1, double y1, double x2, double y2) {
		return (double) Math.atan2(y2 - y1, x2 - x1);
	}
}
