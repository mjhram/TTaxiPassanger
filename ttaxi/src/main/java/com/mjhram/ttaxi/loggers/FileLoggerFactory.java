/*
*    This file is part of GPSLogger for Android.
*
*    GPSLogger for Android is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 2 of the License, or
*    (at your option) any later version.
*
*    GPSLogger for Android is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with GPSLogger for Android.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.mjhram.ttaxi.loggers;

import android.content.Context;

import com.mjhram.ttaxi.common.AppSettings;
import com.mjhram.ttaxi.common.MyInfo;
import com.mjhram.ttaxi.common.Session;
import com.mjhram.ttaxi.common.Utilities;
import com.mjhram.ttaxi.loggers.customurl.CustomUrlLogger;

import java.util.ArrayList;
import java.util.List;

public class FileLoggerFactory {
    public static List<IFileLogger> GetFileLoggers(Context context) {

        List<IFileLogger> loggers = new ArrayList<IFileLogger>();

        /*if(Utilities.IsNullOrEmpty(AppSettings.getGpsLoggerFolder())){
            return loggers;
        }

        File gpxFolder = new File(AppSettings.getGpsLoggerFolder());
        if (!gpxFolder.exists()) {
            gpxFolder.mkdirs();
        }

        if (AppSettings.shouldLogToGpx()) {
            File gpxFile = new File(gpxFolder.getPath(), Session.getCurrentFileName() + ".gpx");
            loggers.add(new Gpx10FileLogger(gpxFile, Session.shouldAddNewTrackSegment(), Session.getSatelliteCount()));
        }

        if (AppSettings.shouldLogToKml()) {
            File kmlFile = new File(gpxFolder.getPath(), Session.getCurrentFileName() + ".kml");
            loggers.add(new Kml22FileLogger(kmlFile, Session.shouldAddNewTrackSegment()));
        }

        if (AppSettings.shouldLogToPlainText()) {
            File file = new File(gpxFolder.getPath(), Session.getCurrentFileName() + ".txt");
            loggers.add(new PlainTextFileLogger(file));
        }

        if (AppSettings.shouldLogToOpenGTS()) {
            loggers.add(new OpenGTSLogger(context));
        }*/

        if (AppSettings.shouldLogToCustomUrl()) {
            float batteryLevel = Utilities.GetBatteryLevel(context);
            String androidId = Utilities.GetAndroidId(context);
            loggers.add(new CustomUrlLogger(AppSettings.getCustomLoggingUrl(), Session.getSatelliteCount(), batteryLevel, androidId));
        }


        return loggers;
    }
    public static void Write(Context context, MyInfo loc) throws Exception {
        for (IFileLogger logger : GetFileLoggers(context)) {
            logger.Write(loc);
        }
    }

    /*public static void Annotate(Context context, String description, MyInfo loc) throws Exception {
        for (IFileLogger logger : GetFileLoggers(context)) {
            logger.Annotate(description, loc);
        }
    }

    public static void Write(Context context, Location loc) throws Exception {
        for (IFileLogger logger : GetFileLoggers(context)) {
            logger.Write(loc);
        }
    }

    public static void Annotate(Context context, String description, Location loc) throws Exception {
        for (IFileLogger logger : GetFileLoggers(context)) {
            logger.Annotate(description, loc);
        }
    }*/
}
