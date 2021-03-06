/**
 * Copyright (C) 2007-2019 52 North Initiative for Geospatial Open Source 
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
 * if the distribution is compliant with both the GNU General Public License 
 * version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * Contact: Benno Schmidt and Martin May, 52 North Initiative for Geospatial 
 * Open Source Software GmbH, Martin-Luther-King-Weg 24, 48155 Muenster, 
 * Germany, info@52north.org
 */
package org.n52.v3d.triturus.gisimplm;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.vgis.*;

/**
 * Class to hold a TIN-geometry that is static in its size.
 * 
 * <b>TODO: Bounding-Box-Berechnung ist noch nicht optimiert; siehe ggf. Coorg.n52.v3d.</b>
 * 
 * @author Benno Schmidt, Ilya Abramov
 */
public class GmSimpleTINGeometry extends VgIndexedTIN
{
	private int mNumberOfPoints;
	private VgPoint[] mPoints;
	private int mNumberOfTriangles;
	private int mTriangles[][];
	private GmEnvelope mEnv;
	private GmSimpleMesh mMesh;

	/**
     * Constructor.<br/>
     * Note that the TIN's size might be changed later via 
     * <tt>this.newPointList()</tt> and <tt>this.newTriangleList()</tt>.
     * 
     * @param pNumberOfPoints Number of points of the TIN
     * @param pNumberOfTriangles Number of triangles of the TIN
	 */
	public GmSimpleTINGeometry(int pNumberOfPoints, int pNumberOfTriangles)
    {
		mNumberOfPoints = pNumberOfPoints;
		mNumberOfTriangles = pNumberOfTriangles;

		mPoints = new GmPoint[mNumberOfPoints];
		this.allocateStorage();
		mTriangles = new int[mNumberOfTriangles][3];
		
		mEnv = null;
	}

	private void allocateStorage() {
		for (int i = 0; i < mNumberOfPoints; i++) {
			mPoints[i] = new GmPoint(0., 0., 0.);
		}
	}

	public int numberOfPoints() {
		return mNumberOfPoints;
	}

	public int numberOfTriangles() {
		return mNumberOfTriangles;
	}

	public VgPoint getPoint(int i) throws T3dException {
		try {
			return mPoints[i];
		}
		catch (Exception e) {
			throw new T3dException(e.getMessage());
		}
	}

    /**
	 * sets the i-th point (vertex) of the TIN structure. Assert that the 
	 * condition 0 &lt;<= i &lt; <tt>this.numberOfPoints()</tt> holds; 
	 * otherwise a <tt>T3dException</tt> will be thrown.
	 * 
     * @param i Point index
	 */
	public void setPoint(int i, VgPoint pPnt) throws T3dException {
		try {
			if (mEnvIsUpToDate) {
				VgPoint lOldPoint = mPoints[i];
				this.updateBounds(lOldPoint, pPnt);
			}

			mPoints[i].set(pPnt);
		}
		catch (T3dException e) {
			throw e;
		}
	}

	public VgTriangle getTriangle(int i) throws T3dException {
		try {
			return new GmTriangle(
				mPoints[mTriangles[i][0]],
				mPoints[mTriangles[i][1]],
				mPoints[mTriangles[i][2]]);
		}
		catch (Exception e) {
			throw new T3dException(e.getMessage());
		}
	}

	/**
     * sets the i-th triangle (facet) of the TIN structure. <tt>pPntIdx1</tt>, 
     * <tt>pPntIdx2</tt>, and <tt>pPntIdx3</tt> give the triangle's indices. 
     * Assert that the condition 0 &lt;<= i &lt; <tt>this.numberOfTriangles()</tt> 
     * holds; otherwise a <tt>T3dException</tt> will be thrown.
	 */
    public void setTriangle(int i, int pPntIdx1, int pPntIdx2, int pPntIdx3)
		throws T3dException 
    {
		try {
			mTriangles[i][0] = pPntIdx1;
			mTriangles[i][1] = pPntIdx2;
			mTriangles[i][2] = pPntIdx3;
		}
		catch (Exception e) {
			throw new T3dException(e.getMessage());
		}
	}

	public int[] getTriangleVertexIndices(int i) throws T3dException {
		try {
			return new int[] { mTriangles[i][0], mTriangles[i][1], mTriangles[i][2] };
		}
		catch (Exception e) {
			throw new T3dException(e.getMessage());
		}
	}

	/**
     * removes an existing point-list. Note that the structure's triangle list
     * will be removed, too. 
     * 
	 * @param pNumberOfPoints Number of points of the TIN
	 */
	public void newPointList(int pNumberOfPoints) {
		mNumberOfPoints = pNumberOfPoints;
		mPoints = new GmPoint[mNumberOfPoints];
		this.allocateStorage();

		mNumberOfTriangles = 0;
		mTriangles = null;

		mEnv = null;
	}

	/**
     * removes an existing triangle-list.
     * 
     * @param pNumberOfTriangles Number of triangles of the TIN
	 */
	public void newTriangleList(int pNumberOfTriangles) {
		mNumberOfTriangles = pNumberOfTriangles;
		mTriangles = new int[mNumberOfTriangles][3];
	}

	/**
     * returns the TIN geometry's bounding-box.
     * 
     * @return Bounding-box (in x-y plane), or <i>null</i> if an error occurs
     * */
	public VgEnvelope envelope() {
		if (!mEnvIsUpToDate) {
			try {
				this.calculateBounds();
			}
			catch (Exception e) {
				return null;
			}
		}
		return mEnv;
	}

    public VgGeomObject footprint() {
        throw new T3dNotYetImplException();
    }

    /**
     * returns the TIN's minimum value (lowest elevation).
     * 
     * @throws T3dException
     */
	public double minimalElevation() throws T3dException {
		if (mEnv == null) {
			throw new T3dException("TIN envelope not available.");
		}
		else {
			return mEnv.getZMin();
		}
	}

    /**
     * returns the TIN's maximum value (highest elevation).
     * 
     * @throws T3dException
     */
    public double maximalElevation() throws T3dException {
		if (mEnv == null) {
			throw new T3dException("TIN envelope not available.");
		}
		else {
			return mEnv.getZMax();
		}
	}

    /**
     * deactivates lazy evaluation mode for bounding-box calculation. For 
     * performance reasons, it might be advantageous to deactivate this mode 
     * prior to TIN edits (<tt>this.setPoint()</tt>-calls).
     */
	public void setBoundsInvalid() {
		mEnvIsUpToDate = false;
	}

	// private helpers to determine the bounding-Box ("lazy evaluation"!):

	private boolean mEnvIsUpToDate = false;

	private void calculateBounds() throws T3dException {
		if (mNumberOfPoints <= 0) {
			throw new T3dException("Tried to access empty TIN.");
		}

		if (!mEnvIsUpToDate) {
			mEnv =
				new GmEnvelope(
					mPoints[0].getX(),
					mPoints[0].getX(),
					mPoints[0].getY(),
					mPoints[0].getY(),
					mPoints[0].getZ(),
					mPoints[0].getZ());

			for (int i = 1; i < mNumberOfPoints; i++) {
				mEnv.letContainPoint(mPoints[i]);

			}
			mEnvIsUpToDate = true;
		}
	}

	private void updateBounds(VgPoint pOldPnt, VgPoint pNewPnt) {
		mEnvIsUpToDate = false; // L�sung suboptimal, aber korrekt ;-)
	}

	/**
	 * @deprecated
     * deletes the i-th triangle from the TIN.<br/>
     * Note that the following triangles (<i>i' &gt;= i</i>) will be 
     * &quot;moved&quot;!<br/>
     * Assert that the condition <i>0 &lt;= i &lt; this.numberOfTriangles()</i>
     * holds; otherweise a <tt>T3dException</tt> will be thrown.<br/>
     * TODO: This method deserves to be improved! 
     * 
	 * @param i Index of the triangle to be deleted
	 * @throws T3dException
	 */
	public void deleteTriangle(int i) throws T3dException {
		if (i > this.numberOfTriangles()) {
			throw new T3dException("Wrong index.");
		}
		for (int c = i; c < mTriangles.length; c++) {
			mTriangles[c][0] = mTriangles[c + 1][0];
			mTriangles[c][1] = mTriangles[c + 1][1];
			mTriangles[c][2] = mTriangles[c + 1][2];
		}
	}

	/**
	 * @deprecated
     * provides a mesh generated from the TIN structure.
     * 
     * TODO ???
	 * @return Mesh
	 */
	public GmSimpleMesh getMesh() {
		mMesh = generateMesh();
		return mMesh;
	}

    private GmSimpleMesh generateMesh() {
		GmSimpleMesh result = new GmSimpleMesh(mNumberOfPoints);
		//	copy vertices
		//	for (int i = 0; i < mNumberOfPoints; i++) {
		//		result.setPoint(i, getPoint(i));
		//	}
		result.setPoints(mPoints);
		// perform meshing
		int[] trVertInd;
		for (int i = 0; i < mNumberOfTriangles; i++) {
			trVertInd = getTriangleVertexIndices(i);
			result.addLineSegment(trVertInd[0], trVertInd[1]);
			result.addLineSegment(trVertInd[1], trVertInd[2]);
			result.addLineSegment(trVertInd[2], trVertInd[0]);
		}
		return result;
	}

	/**
	 * @return Vertex array (VgPoint!)
	 */
	public VgPoint[] getPoints() {
		return mPoints;
	}
}
