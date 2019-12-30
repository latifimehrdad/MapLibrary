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

    public static void MlMap_DrawPolylines(GoogleMap googleMap, List<LatLng> latLngList, int StrokeWidth, int StrokeColor) {
        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(latLngList));
        stylePolyline(polyline, StrokeWidth, StrokeColor);
    }

    private static void stylePolyline(Polyline polyline, int StrokeWidth, int StrokeColor) {
        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(StrokeWidth);
        polyline.setColor(StrokeColor);
        polyline.setJointType(JointType.ROUND);
    }

    public static void MlMap_DrawPolygon(GoogleMap googleMap, List<LatLng> latLngList, int StrokeWidth, int StrokeColor, int FillColor) {
        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .addAll(latLngList));
        stylePolygon(polygon, false, StrokeWidth, StrokeColor, FillColor, 0, 0);
    }

    private static void stylePolygon(Polygon polygon, boolean Pattern, int StrokeWidth, int StrokeColor, int FillColor, int PatternDashLenghtPx, int PatternGapLenghtPx) {
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
    }

    public static void MlMap_DrawCircle(GoogleMap googleMap, LatLng latLng, double radius, int StrokeWidth, int StrokeColor, int FillColor) {
        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeWidth(StrokeWidth)
                .strokeColor(StrokeColor)
                .fillColor(FillColor));
    }

    public static float[] MlMap_MeasureDistance(LatLng OldLatLng, LatLng NewLatLng) {
        float[] results = new float[1];
        Location.distanceBetween(OldLatLng.latitude, OldLatLng.longitude, NewLatLng.latitude, NewLatLng.longitude, results);
        return results;
    }

    public static Marker MlMap_AddMarker(GoogleMap googleMap, LatLng latLng, String title, String tag, int icon) {
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
        marker.setTag(tag);
        return marker;
    }

    public static Boolean MlMap_isInside(LatLng point, List<LatLng> latLngs) {
        return PolyUtil.containsLocation(point, latLngs, true);
    }

    public static ArrayList<LatLng> MlMap_getCirclePoints(LatLng centre, double radius) {
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
    }

    public static void MlMap_AutoZoom(GoogleMap googleMap, List<LatLng> latLngs) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }


//    public Location getCurrentLocation(FragmentActivity context) {
//        LocationRequest request = LocationRequest.create()
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
//    }

//    public void findAddress(Context c, String location, PublishSubject<String> Observables) {
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
//    }

}
