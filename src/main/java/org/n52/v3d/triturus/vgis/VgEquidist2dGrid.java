/***************************************************************************************
 * Copyright (C) 2011 by 52 North Initiative for Geospatial Open Source Software GmbH  *
 *                                                                                     *
 * Contact: Benno Schmidt & Martin May, 52 North Initiative for Geospatial Open Source *
 * Software GmbH, Martin-Luther-King-Weg 24, 48155 Muenster, Germany, info@52north.org *
 *                                                                                     *
 * This program is free software; you can redistribute and/or modify it under the      *
 * terms of the GNU General Public License version 2 as published by the Free Software *
 * Foundation.                                                                         *
 *                                                                                     *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied WARRANTY *
 * OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public  *
 * License for more details.                                                           *
 *                                                                                     *
 * You should have received a copy of the GNU General Public License along with this   *
 * program (see gnu-gpl v2.txt). If not, write to the Free Software Foundation, Inc.,  *
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA, or visit the Free Software *
 * Foundation web page, http://www.fsf.org.                                            *
 **************************************************************************************/
package org.n52.v3d.triturus.vgis;

import org.n52.v3d.triturus.core.T3dException;

/**
 * Class to manage equidistant grid geometries that be oriented arbitrarily inside the x-y plane.<br /><br />
 * <i>German:</i> Klasse zur Verwaltung &auml;quidistanter, beliebig in der xy-Ebene orientierter Gittergeometrien.
 * @author Benno Schmidt
 */
// TODO Unterschied zu VgEquidistGrid in JavaDoc erläutern!
abstract public class VgEquidist2dGrid extends VgGeomObject
{
	/**
     * returns the grid's number of rows (first grid axis).
     */
	abstract public int numberOfRows();

    /**
     * returns the grid's number of columns (second grid axis).
     */
	abstract public int numberOfColumns();

	/**
     * returns the direction vector of the grid's first axis (row direction).
     */
	abstract public VgPoint getDirectionColumns();

    /**
     * returns the direction vector of the grid's second  axis (column direction).
     */
	abstract public VgPoint getDirectionRows();

	/**
     * returns the grid's cell-sizes.
     */
	abstract public void getDelta(Double pDeltaRows, Double pDeltaColumns);

	/**
     * return the grid's origin point.
     */
	abstract public VgPoint getOrigin();

	/**
     * returns the coordinate of the grid-element with the given indices.<br />
     * The assertions <i>0 &lt;= i &lt; this.numberOfRows(), 0 &lt;= j &lt; this.numberOfColumns()</i> must hold,
     * otherwise a <i>T3dException</i> will be thrown.
     * @param i Index of grid row
     * @param j Index of grid column
     * @return Vertex consisting of x- and y-coordinate (with z undefined)
     * @throws T3dException
	 */
	abstract public VgPoint getVertexCoordinate(int i, int j) throws T3dException;

	public String toString() {
		return "[" + "(#" + this.numberOfRows() + " rows x #" +
			this.numberOfColumns() + " cols)" + "]";
	}
}