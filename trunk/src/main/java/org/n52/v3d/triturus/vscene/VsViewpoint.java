package org.n52.v3d.triturus.vscene;

import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.triturus.t3dutil.T3dVector;

/**
 * Allgemeine Basisklasse f�r Ansichtspunkt-Definitionen. Die Klasse dient innerhalb des Rahmenwerks dazu, 
 * Ansichtspunkte unabh�ngig von der konkret eingesetzten Rendering-/Visualisierungsumgebung spezifizieren
 * zu k�nnen.
 * <p>
 * Bem.: Die Positionen sind in Geo-Koordinaten anzugeben. H�ufig stellt die Verwendung relativer Koordinaten, 
 * die auf die Bounding-Box des Szeneninhalts (d. h. die r�umliche Ausdehnung aller "Shapes" in einer Szene) 
 * bezogen sind, eine wesentliche Arbeitserleichterung dar. F�r Geo-Anwendungen ist dies allerdings nicht 
 * unproblematisch, da sich diese Bounding-Box zur Programmlaufzeit dynamisch �ndern kann. Um dennoch relative 
 * Koordinaten verwenden zu k�nnen, lassen sich z. B. die jeweilige Szenen-Semantik ber�cksichtigende Methoden 
 * spezieller Szenen-Implementierungen nutzen; siehe z. B. Transformationsmethoden in der Klasse 
 * <tt>VsSimpleScene</tt>.
 * <p>
 * @author Benno Schmidt<br>
 * (c) 2004, con terra GmbH & Institute for Geoinformatics<br>
 */
public class VsViewpoint
{
	private VgPoint mLookFrom = null;
	private VgPoint mLookAt = null;
	private T3dVector mLookUp = new T3dVector(0., 0. ,1.);

	/**
	 * setzt die aktuelle Betrachterposition.<p>
	 * @param pLookFrom Position in Geo-Koordinaten
	 */
	public void setLookFrom(VgPoint pLookFrom) {
		mLookFrom = pLookFrom;
	}

	/**
	 * liefert die aktuelle Betrachterposition.<p>
	 * @return Position in Geo-Koordinaten
	 */
	public VgPoint getLookFrom() {
		return mLookFrom;
	}

	/**
	 * setzt den Fokus-Punkt der aktuellen Ansicht. Die Blickrichtung ergibt sich aus dem Differenzvektor von 
	 * Betrachterposition und Fokus-Punkt.<p>
	 * @param pLookAt Position in Geo-Koordinaten
	 */
	public void setLookAt(VgPoint pLookAt) {
		mLookAt = pLookAt;
	}

	/**
	 * liefert den Fokus-Punkt der aktuellen Ansicht. Die Blickrichtung ergibt sich aus dem Differenzvektor von 
	 * Betrachterposition und Fokus-Punkt.<p>
	 * @return Position in Geo-Koordinaten
	 */
	public VgPoint getLookAt() {
		return mLookAt;
	}

	/**
	 * setzt den Up-Vektor f�r den Ansichtspunkt. x- und y- Koordinate des Vektors beziehen sich auf die 
	 * Koordinatenachsen des r�umlichen Bezugssystems, in dem Betrachterposition und Fokuspunkt angegeben sind.
	 * Die z-Koordinate bezieht sich auf die vertikale Orientierung. Voreingestellt ist der Wert (0,0,1), d. h.
	 * die Kamera ist gegen�ber der Horizontalen nicht gedreht.<p>
	 * @param pLookUp Vektor bezogen auf die geor�umlichen Kordinatenachsen
	 */
	public void setLookUp(T3dVector pLookUp) {
		mLookUp = pLookUp;
	}

	/**
	 * liefert den Up-Vektor f�r den Ansichtspunkt.<p>
	 * @return Vektor bezogen auf die geor�umlichen Kordinatenachsen
	 * @see VsViewpoint#setLookUp
	 */
	public T3dVector getLookUp() {
		return mLookUp;
	}
}