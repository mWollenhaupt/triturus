/**
 * Copyright (C) 2007-2015 52°North Initiative for Geospatial Open Source
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
 * icense version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * Contact: Benno Schmidt & Martin May, 52 North Initiative for Geospatial Open Source
 * Software GmbH, Martin-Luther-King-Weg 24, 48155 Muenster, Germany, info@52north.org
 */
package org.n52.v3d.triturus.web;

import org.n52.v3d.triturus.core.T3dException;

/**
 * todo engl. JavaDoc
 * Diese Hilfsklasse enth�lt verschiedene Methoden, die den Umgang mit MIME-Typen erleichtern.<p>
 * @author Benno Schmidt
 */
public class MimeTypeHelper
{
    /**
     * liefert die f�r Dateien des angegebenen Typs �blicherweise verwendete Dateiextension.<p>
     * @param pMimeType MIME-Typ, z. B. "image/png"
     * @return Extension (ohne Punkt), z. B. "png"
     */
    static public String getFileExtension(String pMimeType)
    {
        if (pMimeType.equalsIgnoreCase("model/vrml")) return "wrl";
        if (pMimeType.equalsIgnoreCase("model/x3d")) return "x3d";
        if (pMimeType.equalsIgnoreCase("image/bmp")) return "bmp";
        if (pMimeType.equalsIgnoreCase("image/gif")) return "gif";
        if (pMimeType.equalsIgnoreCase("image/jpeg")) return "jpg";
        if (pMimeType.equalsIgnoreCase("image/png")) return "png";
        if (pMimeType.equalsIgnoreCase("image/ppm")) return "ppm";
        if (pMimeType.equalsIgnoreCase("image/rle")) return "rle";
        if (pMimeType.equalsIgnoreCase("image/tga")) return "tga";
        if (pMimeType.equalsIgnoreCase("image/tiff")) return "tif";
        if (pMimeType.equalsIgnoreCase("text/html")) return "htm";
        if (pMimeType.equalsIgnoreCase("text/plain")) return "txt";
        if (pMimeType.equalsIgnoreCase("text/xml")) return "xml";

        throw new T3dException("Unsupported MIME type: " + pMimeType);
    }

    /**
     * liefert den MIME-Typ, dem Dateien mit der angegebenen Extension �blicherweise entsprechen.<p>
     * @param pFileExt Extension (ohne Punkt), z. B. "png"
     * @return MIME-Typ, z. B. "image/png"
     */
    static public String getMimeType(String pFileExt)
    {
        if (pFileExt.equalsIgnoreCase("asc")) return "text/plain";
        if (pFileExt.equalsIgnoreCase("bmp")) return "image/bmp";
        if (pFileExt.equalsIgnoreCase("jpeg")) return "image/jpeg";
        if (pFileExt.equalsIgnoreCase("jpg")) return "image/jpeg";
        if (pFileExt.equalsIgnoreCase("gif")) return "image/gif";
        if (pFileExt.equalsIgnoreCase("grd")) return "text/plain";
        if (pFileExt.equalsIgnoreCase("htm")) return "text/html";
        if (pFileExt.equalsIgnoreCase("html")) return "text/html";
        if (pFileExt.equalsIgnoreCase("png")) return "image/png";
        if (pFileExt.equalsIgnoreCase("ppm")) return "image/ppm";
        if (pFileExt.equalsIgnoreCase("rle")) return "image/rle";
        if (pFileExt.equalsIgnoreCase("tga")) return "image/tga";
        if (pFileExt.equalsIgnoreCase("tif")) return "image/tiff";
        if (pFileExt.equalsIgnoreCase("tiff")) return "image/tiff";
        if (pFileExt.equalsIgnoreCase("tin")) return "text/plain";
        if (pFileExt.equalsIgnoreCase("txt")) return "text/plain";
        if (pFileExt.equalsIgnoreCase("wrl")) return "model/vrml";
        if (pFileExt.equalsIgnoreCase("x3d")) return "model/x3d";
        if (pFileExt.equalsIgnoreCase("xml")) return "text/xml";
        if (pFileExt.equalsIgnoreCase("xyz")) return "text/plain";

        throw new T3dException("Unsupported file extension: " + pFileExt);
    }
}
