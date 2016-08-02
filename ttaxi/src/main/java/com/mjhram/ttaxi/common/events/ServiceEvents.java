package com.mjhram.ttaxi.common.events;

import android.location.Location;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.mjhram.ttaxi.common.DriverInfo;
import com.mjhram.ttaxi.common.TRequestObj;

public class ServiceEvents {
    public static class DriverLocationUpdate {
        public DriverInfo driverInfo;
        public DriverLocationUpdate(DriverInfo drvInfo) {
            driverInfo = drvInfo;
        }
    }
    public static class ErrorConnectionEvent {
        public ErrorConnectionEvent(){
        }
    }

    public static class UpdateAnnouncement {
        public String annImage, annText, countOfDrivers, countOfPassengers;
        public UpdateAnnouncement(String image, String text, String countDrv, String countPas){
            annImage = image;
            annText = text;
            countOfDrivers = countDrv;
            countOfPassengers = countPas;
        }
    }

    public static class updateDrivers {
        public double[] drvLong, drvLat;
        public int drvCount;

        public updateDrivers(int driversCount, double[] drvLatitude, double[] drvLongitude){
            drvCount = driversCount;
            drvLong = drvLongitude;
            drvLat = drvLatitude;
        }
    }

    public static class UpdateStateEvent {
        public TRequestObj treqObj;
        public UpdateStateEvent(TRequestObj theTreqObj){
            treqObj = theTreqObj;
        }
    }

    public static class TRequestUpdated {
        public String treqState;
        public TRequestUpdated(String state){
            treqState = state;
        }
    }

    public static class TRequestAccepted {
        public int drvId;
        public TRequestAccepted(int driverId){
            drvId = driverId;
        }
    }

    public static class CancelTRequests {
        public String msg;
        public CancelTRequests(String errorMsg){
            msg = errorMsg;
        }
    }
    /**
     * New location
     */
    public static class LocationUpdate {
        public Location location;
        public LocationUpdate(Location loc) {
            this.location = loc;
        }
    }

    /**
     * Number of visible satellites
     */
    /*public static class SatelliteCount {
        public int satelliteCount;
        public SatelliteCount(int count) {
            this.satelliteCount = count;
        }
    }*/

    /**
     * Whether the logging service is still waiting for a location fix
     */
    public static class WaitingForLocation {
        public boolean waiting;
        public WaitingForLocation(boolean waiting) {
            this.waiting = waiting;
        }
    }

    /**
     * Indicates that GPS/Network location services have temporarily gone away
     */
    public static class LocationServicesUnavailable {
    }

    /**
     * Status of the user's annotation, whether it has been written or is pending

    public static class AnnotationStatus {
        public boolean annotationWritten;
        public AnnotationStatus(boolean written){
            this.annotationWritten = written;
        }
    }*/

    /**
     * Whether GPS logging has started; raised after the start/stop button is pressed
     */
    public static class LoggingStatus {
        public boolean loggingStarted;
        public LoggingStatus(boolean loggingStarted) {
            this.loggingStarted = loggingStarted;
        }
    }

    /**
     * The file name has been set
     */
    public static class FileNamed {
        public String newFileName;
        public FileNamed(String newFileName) {
            this.newFileName = newFileName;
        }
    }

    public static class ActivityRecognitionEvent {
        public ActivityRecognitionResult result;
        public ActivityRecognitionEvent(ActivityRecognitionResult arr) {
            this.result = arr;
        }
    }
}
