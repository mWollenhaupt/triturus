/**
 * Copyright (C) 2007-2019 52North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *  - Apache License, version 2.0
 *  - Apache Software License, version 1.0
 *  - GNU Lesser General Public License, version 3
 *  - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *  - Common Development and Distribution License (CDDL), version 1.0.
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * license version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * Contact: Benno Schmidt & Martin May, 52 North Initiative for Geospatial 
 * Open Source Software GmbH, Martin-Luther-King-Weg 24, 48155 Muenster, 
 * Germany, info@52north.org
 */
package org.n52.v3d.triturus.vgis;

import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.core.T3dException;

/**
 * Class to hold a triangle that might be arbitrarily oriented in 3-D space.
 * 
 * @author Benno Schmidt
 */
abstract public class VgTriangle extends VgGeomObject2d 
{
	/** 
	 * sets the triangle's corner-points.
	 * 
	 * @param pCorner1 first corner point
	 * @param pCorner2 second corner point
	 * @param pCorner3 third corner point
	 */
	abstract public void setCornerPoints(VgPoint pCorner1, VgPoint pCorner2, VgPoint pCorner3);
	
	/** 
	 * returns the triangle's corner-points.
	 * 
	 * @return Array consisting of three elements holding the corner-points
	 */
	abstract public VgPoint[] getCornerPoints();

	/**
	 * returns the triangle area referring to the assigned coordinate reference system.
	 * 
	 * @return Area value
	 * @see VgGeomObject#getSRS
	 */
	public double area()
	{
		VgPoint[] p = this.getCornerPoints();
		VgPoint p1 = p[0], p2 = p[1], p3 = p[2];
		
		double l12 = p2.distance(p1); // TODO: Here a NullPointerException might be thrown!
		double l13 = p3.distance(p1);
		double l23 = p3.distance(p2);
		
		double s = (l12 + l13 + l23) / 2.;
		return Math.sqrt(s * (s - l12) * (s - l13) * (s - l23)); // Heron's formula
	}

	/**
	 * returns the triangle's circumference referring to the assigned 
	 * coordinate reference system. Note that the calculation is done 
	 * in "3D" considering z-coordinates.
	 * 
	 * @return Area value
	 * @see VgGeomObject#getSRS
	 */
	public double circumference() {
		VgPoint[] p = this.getCornerPoints();
		double sum = p[1].distance(p[0]);
		sum += p[2].distance(p[1]);
		sum += p[0].distance(p[2]);
		return sum;
	}

	/**
	 * performs z-value interpolation at a given position. The method provides 
	 * a result, even if the given position 'pt' lies outside the triangle 
	 * (extrapolation). If necessary, check this case before calling the 
	 * method, e.g. using <tt>this.isInside()</tt>.
	 * <br /> 
	 * Notes: 1. The z-coordinate of 'pt' will be ignored.<br />
	 * 2. If the triangle area is 0, a <tt>T3dException</tt> might be thrown
	 * ("Division by zero error").
	 * 
	 * @param pt Position
	 * @return z-value
	 * @see VgTriangle#isInsideXY
	 * @see T3dException
	 */
	public double interpolateZ(VgPoint pt) 
	{
		VgPoint[] p = this.getCornerPoints();
		
		T3dVector dir0 = new T3dVector();
		dir0.assignDiff(p[0], p[2]);
		T3dVector dir1 = new T3dVector();
		dir1.assignDiff(p[1], p[2]);
		
		// Solve plane equation p[2] + s0 * (p[0] - p[2]) + s1 * (p[1] - p[2]) = 0 
		// using Cramer's rule:
		double detNum =
			- dir0.getX() * dir1.getY() * p[2].getZ()
			+ dir1.getX() * (pt.getY() - p[2].getY()) * dir0.getZ()
			+ (pt.getX() - p[2].getX()) * dir0.getY() * dir1.getZ()
			- dir0.getZ() * dir1.getY() * (pt.getX() - p[2].getX())
			- dir1.getZ() * (pt.getY() - p[2].getY()) * dir0.getX()
			+ p[2].getZ() * dir0.getY() * dir1.getX();		    
		double detDenom = -dir0.getX() * dir1.getY() + dir0.getY() * dir1.getX();
		//System.out.println("tri = " + this);
		//System.out.println("dir0 = " + dir0);
		//System.out.println("dir1 = " + dir1);
		//System.out.println("pt = " + pt);
		//System.out.println("detNum = " + detNum);
		//System.out.println("detDenom = " + detDenom);
		                     
		if (Math.abs(detDenom) < 0.000001)
			throw new T3dException("Divison by zero error."); // should not occur
		else
			return detNum / detDenom;
	}

	/**
	 * checks, with respect to the x-y-plane, if 'pt' is inside the triangle. 
	 * If in <tt>pEdge</tt> a <i>true</i> value is passed, the method provides 
	 * <i>true</i> as result, even if 'pt' lies on one of the triangle's edges 
	 * (boundary).
	 * <br />
	 * Notes: 1. The z-coordinate of 'pt' will be ignored, since the 
	 * computation will be done inside the x-y-plane.<br />
	 * 2. If the triangle area is 0, a <tt>T3dException</tt> might be thrown
	 * ("Division by zero error").
	 * 
	 * @param pt
	 * @param pEdge
	 * @return <i>true</i>, if <tt>pt</tt> lies inside the triangle, else <i>false</i>
	 * @see T3dEception
	 */
	public boolean isInsideXY(VgPoint pt, boolean pEdge)
	{
		VgPoint[] p = this.getCornerPoints();
		
		T3dVector dir0 = new T3dVector();
		dir0.assignDiff(p[0], p[2]);
		T3dVector dir1 = new T3dVector();
		dir1.assignDiff(p[1], p[2]);
		   
		// The equation p[2] + s0 * (p[0] - p[2]) + s1 * (p[1] - t[2]) = 0, 0 < s0, s1 < 1 
		// gives the set of all points that lie inside the parallelogram given by 
		// (p[0] - p[2]), (p[1] - p[2]).
		   
		double detNum0 = (pt.getX() - p[2].getX()) * dir1.getY() - (pt.getY() - p[2].getY()) * dir1.getX();
		double detNum1 = dir0.getX() * (pt.getY() - p[2].getY()) - dir0.getY() * (pt.getX() - p[2].getX());
		double detDenom = dir0.getX() * dir1.getY() - dir0.getY() * dir1.getX();
		   
		if (Math.abs(detDenom) < 0.000001)
			throw new T3dException("Divison by zero error."); // should not occur
		
		double s0 = detNum0 / detDenom;
		double s1 = detNum1 / detDenom;
		
		// pt is inside the triangle, if 0 < s0 + s1 < 1:
		if (s0 < 0. || s0 < 0. || s0 > 1. || s0 > 1.)
			return false; // point outside parallelogram
		if (s0 + s1 < 1.)
			return true; // point conmpletely inside triangle
		if (s0 + s1 == 1. && pEdge)
			return true; // point on boundary
		return false; // point inside parallelogram and not inside triangle
	}
	
	public String toString() {
		VgPoint[] p = this.getCornerPoints();
		return "[" + p[0].toString() + ", " + p[1].toString() + ", " + p[2].toString() + "]";
	}
}