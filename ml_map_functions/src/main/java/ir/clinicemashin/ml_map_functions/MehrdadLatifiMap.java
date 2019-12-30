package ir.clinicemashin.ml_map_functions;

import android.location.Location;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MehrdadLatifiMap {



    public void DrawPolylines(
            GoogleMap googleMap,
            List<LatLng> latLngList,
            int StrokeWidth,
            int StrokeColor) {//____________________________________________________________________ Start DrawPolylines
        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(latLngList));
        stylePolyline(polyline,StrokeWidth,StrokeColor);
    }//_____________________________________________________________________________________________ End DrawPolylines


    private void stylePolyline(Polyline polyline, int StrokeWidth, int StrokeColor) {//_____________ Start stylePolyline
        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(StrokeWidth);
        polyline.setColor(StrokeColor);
        polyline.setJointType(JointType.ROUND);

    }//_____________________________________________________________________________________________ End stylePolyline


    public void DrawPolygon(
            GoogleMap googleMap,
            List<LatLng> latLngList,
            int StrokeWidth,
            int StrokeColor,
            int FillColor) {//______________________________________________________________________ Start DrawPolylines
        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .addAll(latLngList));
        stylePolygon(polygon, false, StrokeWidth,StrokeColor,FillColor,0,0);
    }//_____________________________________________________________________________________________ End DrawPolylines



    private void stylePolygon(
            Polygon polygon,
            boolean Pattern,
            int StrokeWidth,
            int StrokeColor,
            int FillColor,
            int PatternDashLenghtPx,
            int PatternGapLenghtPx) {//_____________________________________________________________ Start stylePolygon

        if (Pattern) {
            PatternItem DASH = new Dash(PatternDashLenghtPx);
            PatternItem GAP = new Gap(PatternGapLenghtPx);
            List<PatternItem> PATTERN_POLYGON = Arrays.asList(GAP, DASH);
            List<PatternItem> pattern = PATTERN_POLYGON;
            polygon.setStrokePattern(pattern);
        }
        polygon.setStrokeWidth(StrokeWidth);
        polygon.setStrokeColor(StrokeColor);
        polygon.setFillColor(FillColor);
    }//_____________________________________________________________________________________________ End stylePolygon




    public void DrawCircle(
            GoogleMap googleMap,
            LatLng latLng,
            double radius,
            int StrokeWidth,
            int StrokeColor,
            int FillColor) {//______________________________________________________________________ Start DrawCircle
        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeWidth(StrokeWidth)
                .strokeColor(StrokeColor)
                .fillColor(FillColor));
    }//_____________________________________________________________________________________________ End DrawCircle



    public float[] MeasureDistance(LatLng Old, LatLng New) {//______________________________________ Start MeasureDistance
        float[] results = new float[1];
        Location.distanceBetween(Old.latitude, Old.longitude,
                New.latitude, New.longitude, results);
        return results;
    }//_____________________________________________________________________________________________ End MeasureDistance


    public Marker AddMarker(
            GoogleMap googleMap,
            LatLng latLng,
            String title,
            String tag,
            int icon) {//___________________________________________________________________________ Start AddMarker
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
        marker.setTag(tag);
        return marker;
    }//_____________________________________________________________________________________________ End AddMarker


    public Boolean isInside(LatLng point, List<LatLng> latLngs) {//_________________________________ Start isInside
        return PolyUtil.containsLocation(point, latLngs, true);
    }//_____________________________________________________________________________________________ End isInside



    public ArrayList<LatLng> getCirclePoints(LatLng centre, double radius) {//______________________ Start getCirclePoints
        ArrayList<LatLng> points = new ArrayList<LatLng>();

        double EARTH_RADIUS = 6378100.0;
        double lat = centre.latitude * Math.PI / 180.0;
        double lon = centre.longitude * Math.PI / 180.0;

        for (double t = 0; t <= Math.PI * 2; t += 0.3) {
            double latPoint = lat + (radius / EARTH_RADIUS) * Math.sin(t);
            double lonPoint = lon + (radius / EARTH_RADIUS) * Math.cos(t) / Math.cos(lat);
            LatLng point = new LatLng(latPoint * 180.0 / Math.PI, lonPoint * 180.0 / Math.PI);
            points.add(point);

        }

        return points;
    }//_____________________________________________________________________________________________ End getCirclePoints


    public void AutoZoom(GoogleMap googleMap,List<LatLng> latLngs) {//______________________________ Start AutoZoom
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }//_____________________________________________________________________________________________ End AutoZoom



//    public Location getCurrentLocation(FragmentActivity context) {//___________________________________ Start getDeviceLocation
//        LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setNumUpdates(1)
//                .setInterval(1000);
//        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
//
//        Disposable subscription = locationProvider.getUpdatedLocation(request)
//                .subscribe(new Consumer<Location>() {
//                    @Override
//                    public void accept(Location location) throws Exception {
//                        return location;
//                    }
//                });
//
//    }//_____________________________________________________________________________________________ End getDeviceLocation
//
//
//    public void findAddress(Context c, String location, PublishSubject<String> Observables) {//_____ Start findAddress
//        if (location != null && !location.equalsIgnoreCase("")) {
//            Locale locale = new Locale("fa_IR");
//            Locale.setDefault(locale);
//            Geocoder geocoder = new Geocoder(c, locale);
//
//            try {
//                List<Address> addressList;
//                addressList = geocoder.getFromLocationName(location, 1);
//                if (addressList.size() > 0) {
//                    setML_FindAddress(new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude()));
//                    Observables.onNext("FindAddress");
//                } else
//                    Observables.onNext("NoAddress");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                Observables.onNext("NoAddress");
//            }
//        }
//    }//_____________________________________________________________________________________________ End findAddress


}
