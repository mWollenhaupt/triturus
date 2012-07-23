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
package org.n52.v3d.triturus.t3dutil.operatingsystem;

/**
 * todo engl. JavaDoc
 * Klasse zur Steuerung von Zeitscheiben-bezogener Ressourcen-Zuteilungen.
 * @author Benno Schmidt
 */
public class TimeSliceAssigner
{
    // Zeitpunkt der Instanziierung des TimeSliceAssigner-Objekts:
    private long mInitTime;
    // interne Nummer der n�chsten freien Zeitscheibe:
    private long mNextFreeSlice;
    // max. Zeitraum, um den eine angeforderte Zeitscheibe in der Zukunft liegen darf (angegeben als
    // Zeitscheiben-Anzahl):
    private int mMaxWaitTime = 5;
    // Zeitscheiben-L�nge in in Millisekunden
    private long mDeltaT = 10000;
    
    /**
     * Konstruktor. Als Parameter sind der maximale Zeitraum, um den eine angeforderte Zeitscheibe in der Zukunft liegen
     * darf,  und die zeitliche L�nge der Zeitscheiben zu �bergeben.<p>
     * Der maximale Zeitraum, um den eine angeforderte Zeitscheibe in der Zukunft liegen darf, ist als
     * Zeitscheiben-Anzahl anzugeben. Die Zeitdauer in Millisekunden betr�gt
     * <tt>this.getSliceInterval() * this.maxWaitTime()</tt>. Voreingestellt ist der Wert 5. Wird diese Anzahl
     * �berschritten, liefert die Methode <tt>getAssignedSlice</tt> bei der Zuteilung keine nutzbare Zeitscheibe.<p>
     * F�r die zeitliche L�nge der Zeitscheiben ist der Wert 10000 (entspr. 10 Sekunden) voreingestellt.<p>
     * @param pMaxWaitTime max. Zeitscheiben-Anzahl, um den eine angeforderte Zeitscheibe in der Zukunft liegen darf
     * @param pDeltaT Dauer in Millisekunden
     * @see TimeSliceAssigner#getAssignedSlice
     * @see TimeSliceAssigner#getSliceInterval
     * @see TimeSliceAssigner#maxWaitTime
     */
    public TimeSliceAssigner(int pMaxWaitTime, long pDeltaT) {
        mInitTime = this.currentTime();    
        mNextFreeSlice = 0;
        mMaxWaitTime = pMaxWaitTime;
        mDeltaT = pDeltaT;
    }

    /**
     * Konstruktor.<p>
     */
    public TimeSliceAssigner() {
        mInitTime = this.currentTime();
        mNextFreeSlice = 0;
    }

    /**
     * liefert die aktuelle Systemzeit.<p>
     */
    public long currentTime() {
        return new java.util.Date().getTime();
    }

    /**
     * liefert die konfigurierte zeitliche L�nge der Zeitscheiben.<p>
     * @return Dauer in Milisekunden
     */
    public long getSliceInterval() {
        return mDeltaT;
    }

    /**
     * liefert den maximalen Zeitraum, um den eine angeforderte Zeitscheibe in der Zukunft liegen darf (angegeben als
    // Zeitscheiben-Anzahl).<p>
     * @return max. Zeitscheiben-Anzahl, um den eine angeforderte Zeitscheibe in der Zukunft liegen darf
     */
    public int maxWaitTime() {
        return mMaxWaitTime;
    }

    /**
     * teilt dem anfragenden Prozess eine Zeitscheibe zu und gibt den zugeh�rigen Startzeitpunkt zur�ck. Falls keine der
     * n�chsten <i>N</i> Zeitscheiben frei ist, wird <i>null</i> zur�ckgegeben. Dieser Fall tritt auf, wenn die
     * konfigurierte Anzahl <i>N</i> der Zeitscheiben, um den eine angeforderte Zeitscheibe in der Zukunft liegen darf,
     * �berschritten wird.<p>
     * Bei jedem Methodenaufruf wird eine neue Zeitscheibe belegt. Seitens der aufrufenden Anwendung ist somit
     * sicherzustellen, dass je anzufordernder Zeitscheibe nur ein einziger Aufruf erfolgt!<p>
     * Bem.: Die Dauer die Zeitscheibe ist mittels <tt>getSliceInterval()</tt> abfragbar, die maximale
     * Zeitscheiben-Anzahl, um den eine angeforderte Zeitscheibe in der Zukunft liegen darf, �ber die Methode
     * <tt>maxWaitTime()</tt>. Die Kontrolle dessen, was sich w�hrend des Ablaufs der Zeitscheibe ereignet, obliegt der
     * Anwendung, welche die vorliegende Klasse nutzt.<p>
     * @return Startzeitpunkt der zugeteilten Zeitscheibe oder <i>null</i>
     */
    public Long getAssignedSlice() 
    { 
        long lCurrTime = this.currentTime();
        long lSliceNo = (long) Math.floor((lCurrTime - mInitTime) / mDeltaT);
        if (mNextFreeSlice <= lSliceNo) {
            mNextFreeSlice = lSliceNo + 1;
            return this.startTime(lSliceNo);
        }
        else {
            // mNextFreeSlice > lSliceNo
            if (mNextFreeSlice - lSliceNo >= mMaxWaitTime)
                return null; // da "busy"
            else {
                mNextFreeSlice++;
                return this.startTime(mNextFreeSlice);
            }
        }
    }

    private Long startTime(long pSliceNo) {
        return new Long(mInitTime + pSliceNo * mDeltaT);
    }
}