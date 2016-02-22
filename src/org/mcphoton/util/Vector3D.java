/*
 * Copyright (C) 2015 ElectronWill
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcphoton.util;

/**
 * A geometric vector in a 3D space.
 *
 * @author ElectronWill
 */
public class Vector3D {

	/**
	 * Creates a new 3D vector which is the sum of two vectors v1 and v2.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vector3D sum(Vector3D v1, Vector3D v2) {
		return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}

	/**
	 * Creates a new 3D vector which is the sum of several vectors.
	 *
	 * @param vectors several vectors
	 * @return
	 */
	public static Vector3D sum(Vector3D... vectors) {
		float x = 0, y = 0, z = 0;
		for (Vector3D v : vectors) {
			x += v.x;
			y += v.y;
			z += v.z;
		}
		return new Vector3D(x, y, z);
	}

	/**
	 * Creates a new 3D vector which is the difference between two vectors v1 and v2.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vector3D difference(Vector3D v1, Vector3D v2) {
		return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	/**
	 * Calculates the dot product of two vectors.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float dotProduct(Vector3D v1, Vector3D v2) {
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}

	/**
	 * Calculates the dot product of two vectors, in the plan XY
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float dotProductXY(Vector3D v1, Vector3D v2) {
		return (v1.x * v2.x) + (v1.y * v2.y);//xx'+yy'
	}

	/**
	 * Calculates the dot product of two vectors, int the plan XZ.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float dotProductXZ(Vector3D v1, Vector3D v2) {
		return (v1.x * v2.x) + (v1.z * v2.z);
	}

	/**
	 * Calculates the dot product of two vectors, in the plan YZ.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float dotProductYZ(Vector3D v1, Vector3D v2) {
		return (v1.y * v2.y) + (v1.z * v2.z);
	}

	/**
	 * Calculates the cosinus of the angle between two vectors, in the plan XY.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float cosBetweenXY(Vector3D v1, Vector3D v2) {
		double l1 = Math.sqrt(v1.x * v1.x + v1.y * v1.y);//l=sqrt(x²+y²)
		double l2 = Math.sqrt(v2.x * v2.x + v2.y * v2.y);
		return (float) ((v1.x * v2.x + v1.y * v2.y) / (l1 * l2));//cos(v1,v2) = (xx'+yy')/(||v1||*||v2||)
	}

	/**
	 * Calculates the cosinus of the angle between two vectors, in the plan XZ.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float cosBetweenXZ(Vector3D v1, Vector3D v2) {
		double l1 = Math.sqrt(v1.x * v1.x + v1.z * v1.z);
		double l2 = Math.sqrt(v2.x * v2.x + v2.z * v2.z);
		return (float) ((v1.x * v2.x + v1.z * v2.z) / (l1 * l2));
	}

	/**
	 * Calculates the cosinus of the angle between two vectors, in the plan YZ.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float cosBetweenYZ(Vector3D v1, Vector3D v2) {
		double l1 = Math.sqrt(v1.y * v1.y + v1.z * v1.z);
		double l2 = Math.sqrt(v2.y * v2.y + v2.z * v2.z);
		return (float) ((v1.y * v2.y + v1.z * v2.z) / (l1 * l2));
	}

	/**
	 * Calculates the angle between two vectors in the plan XY, in radians.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double angleBetweenXY(Vector3D v1, Vector3D v2) {
		return Math.acos(cosBetweenXY(v1, v2));//radians
	}

	/**
	 * Calculates the angle between two vectors in the XZ plane, in radians.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double angleBetweenXZ(Vector3D v1, Vector3D v2) {
		return Math.acos(cosBetweenXZ(v1, v2));//radians
	}

	/**
	 * Calculates the angle between two vectors in the YZ plane, in radians.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double angleBetweenYZ(Vector3D v1, Vector3D v2) {
		return Math.acos(cosBetweenYZ(v1, v2));//radians
	}

	/**
	 * Calculates the cross product of the two given vectors.
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vector3D crossProduct(Vector3D v1, Vector3D v2) {
		float nx = (v1.y * v2.z) - (v1.z * v2.y);
		float ny = (v1.z * v2.x) - (v1.x * v2.z);
		float nz = (v1.x * v2.y) - (v1.y * v2.x);
		return new Vector3D(nx, ny, nz);
	}

	public float x;
	public float y;
	public float z;

	/**
	 * Creates a new 3D vector with the given coordinates.
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a new 3d vector between two points A and B
	 *
	 * @param xA x coordinate of the point A
	 * @param yA y coordinate of the point A
	 * @param zA z coordinate of the point A
	 * @param xB x coordinate of the point B
	 * @param yB y coordinate of the point B
	 * @param zB z coordinate of the point B
	 */
	public Vector3D(float xA, float yA, float zA, float xB, float yB, float zB) {
		this(xB - xA, yB - yA, zB - zA);
	}

	/**
	 * Creates a new 3D vector between two points A and B. Note that the world of the two locations
	 * do not change the resulting vector.
	 *
	 * @param a
	 * @param b
	 */
	public Vector3D(Location a, Location b) {
		this((float) (a.x - b.x), (float) (a.y - b.y), (float) (a.z - b.z));
	}

	/**
	 * Sets the coordinates of this vector to the given ones.
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void reset(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Sets this vector to the one between two points A and B.
	 *
	 * @param xA x coordinate of the point A
	 * @param yA y coordinate of the point A
	 * @param zA z coordinate of the point A
	 * @param xB x coordinate of the point B
	 * @param yB y coordinate of the point B
	 * @param zB z coordinate of the point B
	 */
	public void reset(float xA, float yA, float zA, float xB, float yB, float zB) {
		x = xB - xA;
		y = yB - yA;
		z = zB - zA;
	}

	/**
	 * Sets this vector to the one between two points A and B.
	 *
	 * @param a
	 * @param b
	 */
	public void reset(Location a, Location b) {
		x = (float) (a.x - b.x);
		y = (float) (a.y - b.y);
		z = (float) (a.z - b.z);
	}

	/**
	 * Adds n to each coordinate of this vector.
	 *
	 * @param n
	 * @return this vector
	 */
	public Vector3D add(float n) {
		this.x += n;
		this.y += n;
		this.z += n;
		return this;
	}

	/**
	 * Adds x, y and z to the x, y and z coordinates of this vector.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return this vector
	 */
	public Vector3D add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	/**
	 * Adds the coordinates of the given vector v to this one.
	 *
	 * @param v
	 * @return this vector
	 */
	public Vector3D add(Vector3D v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}

	/**
	 * Substracts n to each coordinate of this vector.
	 *
	 * @param n
	 * @return this vector
	 */
	public Vector3D substract(float n) {
		this.x -= n;
		this.y -= n;
		this.z -= n;
		return this;
	}

	/**
	 * Substracts x, y and z to the x, y and z coordinates of this vector.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return this vector
	 */
	public Vector3D substract(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	/**
	 * Adds the coordinates of the given vector v to this one.
	 *
	 * @param v
	 * @return this vector
	 */
	public Vector3D substract(Vector3D v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}

	/**
	 * Multiplies each coordinate of this vector by n.
	 *
	 * @param n
	 * @return this vector
	 */
	public Vector3D multiply(float n) {
		this.x *= n;
		this.y *= n;
		this.z *= n;
		return this;
	}

	/**
	 * Multiplies the x, y and z coordinate of this vector by the given x, y and z.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return this vector
	 */
	public Vector3D multiply(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	/**
	 * Multiplies the coordinates of this one by those of the given vector v.
	 *
	 * @param v
	 * @return this vector
	 */
	public Vector3D multiply(Vector3D v) {
		this.x *= v.x;
		this.y *= v.y;
		this.z *= v.z;
		return this;
	}

	/**
	 * Divides the coordinates of this vector by n.
	 *
	 * @param n
	 * @return this vector
	 */
	public Vector3D divide(float n) {
		this.x /= n;
		this.y /= n;
		this.z /= n;
		return this;
	}

	/**
	 * Divides the x, y and z coordinate of this vector by the given x, y and z.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return this vector
	 */
	public Vector3D divide(float x, float y, float z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		return this;
	}

	/**
	 * Divides the coordinates of the given vector v to this one.
	 *
	 * @param v
	 * @return this vector
	 */
	public Vector3D divide(Vector3D v) {
		this.x /= v.x;
		this.y /= v.y;
		this.z /= v.z;
		return this;
	}

	/**
	 * Returns the length of this 3D vector.
	 *
	 * @return
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Returns the square of the length of this vector.
	 *
	 * @return
	 */
	public float squareLength() {
		return x * x + y * y + z * z;
	}

	/**
	 * Normalize this vector, i.e. make it so that it length is 1.
	 *
	 * @return this vector
	 */
	public Vector3D normalize() {
		float l = length();
		x /= l;
		y /= l;
		z /= l;
		return this;
	}

	/**
	 * Calculates the dot product of this vector.
	 *
	 * @param v2
	 * @return
	 */
	public float dotProduct(Vector3D v2) {
		return (x * v2.x) + (y * v2.y) + (z * v2.z);
	}

	/**
	 * Calculates the dot product of this vector and the given one in the plane XY.
	 *
	 * @param v2
	 * @return
	 */
	public float dotProductXY(Vector3D v2) {
		return (x * v2.x) + (y * v2.y);//xx'+yy'
	}

	/**
	 * Calculates the dot product of this vector and the given one in the plane XZ
	 *
	 * @param v2
	 * @return
	 */
	public float dotProductXZ(Vector3D v2) {
		return (x * v2.x) + (z * v2.z);
	}

	/**
	 * Calculates the dot product of this vector and the given one in the plane YZ.
	 *
	 * @param v2
	 * @return
	 */
	public float dotProductYZ(Vector3D v2) {
		return (y * v2.y) + (z * v2.z);
	}

	/**
	 * Calculates the angle between this vector and the given one in the XY plane.
	 *
	 * @param v2
	 * @return
	 */
	public double angleXY(Vector3D v2) {
		return Math.acos(cosBetweenXY(this, v2));//radians
	}

	/**
	 * Calculates the angle between this vector and the given one in the XZ plane.
	 *
	 * @param v2
	 * @return
	 */
	public double angleXZ(Vector3D v2) {
		return Math.acos(cosBetweenXZ(this, v2));//radians
	}

	/**
	 * Calculates the angle between this vector and the given one in the YZ plane.
	 *
	 * @param v2
	 * @return
	 */
	public double angleYZ(Vector3D v2) {
		return Math.acos(cosBetweenYZ(this, v2));//radians
	}

	/**
	 * Calculates the cross product of this vector and the given one.
	 *
	 * @param v2
	 * @return a new vector which is the cross product of this vector by the given one
	 */
	public Vector3D crossProduct(Vector3D v2) {
		float nx = (y * v2.z) - (z * v2.y);
		float ny = (z * v2.x) - (x * v2.z);
		float nz = (x * v2.y) - (y * v2.x);
		return new Vector3D(nx, ny, nz);
	}

}
