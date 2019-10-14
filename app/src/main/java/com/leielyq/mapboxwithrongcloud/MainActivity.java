package com.leielyq.mapboxwithrongcloud;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.china.maps.ChinaMapView;
import com.mapbox.mapboxsdk.plugins.china.shift.ShiftLocation;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Use the Mapbox Core Library to receive updates when the device changes location.
 */
public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener, MapboxMap.OnCameraIdleListener, MapboxMap.OnMapClickListener {

    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 10000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationChangeListeningActivityLocationCallback callback =
            new LocationChangeListeningActivityLocationCallback(this);

    int REQUEST_CODE_AUTOCOMPLETE = 123;
    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Location shiftedDeviceLocation = mapboxMap.getLocationComponent().getLastKnownLocation();



                double latitude = shiftedDeviceLocation.getLatitude();
                double longitude = shiftedDeviceLocation.getLongitude();
                String provider = mapboxMap.getLocationComponent().getLastKnownLocation().getProvider();

                Uri uri = Uri.parse("https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/geojson(%7B%22type%22%3A%22Point%22%2C%22coordinates%22%3A%5B" + longitude + "%2C" + latitude + "%5D%7D)/" + longitude + "," + latitude + ",17,0,60/300x200?access_token=pk.eyJ1IjoibGVpZWx5cSIsImEiOiJjazFsbzR4ZGgwNzRmM25wY3doM2ZxcWM2In0.ByPOPdfO_jxy8ofzH-qJgw");


                Intent intent = new Intent();
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("address", provider);
                intent.putExtra("locuri", uri.toString());
                setResult(234, intent);
                finish();

//                https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/geojson(%7B%22type%22%3A%22Point%22%2C%22coordinates%22%3A%5B-73.99%2C40.7%5D%7D)/-73.99,40.70,12/500x300?access_token=pk.eyJ1IjoibGVpZWx5cSIsImEiOiJjazFsbzR4ZGgwNzRmM25wY3doM2ZxcWM2In0.ByPOPdfO_jxy8ofzH-qJgw
                mAccessToken = "pk.eyJ1IjoibGVpZWx5cSIsImEiOiJjazFsbzV0NTMwNzV4M2lsbzllODdlM296In0.7YfACRsimwYWVFkvRWAREA";
//                Intent intent = new PlacePicker.IntentBuilder()
//                        .accessToken(mAccessToken)
//                        .placeOptions(
//                                PlacePickerOptions.builder()
//                                        .statingCameraPosition(
//                                                new CameraPosition.Builder()
//                                                        .target(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(),
//                                                                mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()))
//                                                        .zoom(16)
//                                                        .build())
//                                        .build())
//                        .build(MainActivity.this);
//                PlaceOptions ningbo = PlaceOptions.builder()
//
//                        .build();
//
//                Intent intent = new PlaceAutocomplete.IntentBuilder()
//                        .accessToken(mAccessToken)
//                        .placeOptions(ningbo)
//                        .build(MainActivity.this);
//                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);


            }
        });
    }

    private void pos() {
        int top = mapView.getTop();
        int left = mapView.getLeft();
        RectF box = new RectF(left, top, left + mapView.getWidth(), top + mapView.getHeight());
        List<Feature> features = mapboxMap.queryRenderedFeatures(box, "building");


        if (features.size() > 0) {
            String featureId = features.get(0).id();

            for (int a = 0; a < features.size(); a++) {
                if (featureId.equals(features.get(a).id())) {
                    if (features.get(a).geometry() instanceof Polygon) {

                        List<LatLng> list = new ArrayList<>();
                        for (int i = 0; i < ((Polygon) features.get(a).geometry()).coordinates().size(); i++) {
                            for (int j = 0;
                                 j < ((Polygon) features.get(a).geometry()).coordinates().get(i).size(); j++) {
                                list.add(new LatLng(
                                        ((Polygon) features.get(a).geometry()).coordinates().get(i).get(j).latitude(),
                                        ((Polygon) features.get(a).geometry()).coordinates().get(i).get(j).longitude()
                                ));
                            }
                        }

                        mapboxMap.addPolygon(new PolygonOptions()
                                .addAll(list)
                                .fillColor(Color.parseColor("#8A8ACB"))
                        );
                    }
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

//        mapboxMap.setMinZoomPreference(15);
        mapboxMap.setStyle(Style.OUTDOORS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        mapboxMap.addOnCameraIdleListener(MainActivity.this);
                        mapboxMap.getLocationComponent().zoomWhileTracking(15);
                        mapboxMap.addOnMapClickListener(MainActivity.this);
                        // Use the map target's coordinates to make a reverse geocoding search
                        final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;

                        GeoJsonSource source = style.getSourceAs("dropped-marker-source-id");

                        if (source != null) {
                            source.setGeoJson(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));
                        }


                    }
                });
    }


    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

// Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();


        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "定位权限",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
        } else {
            Toast.makeText(this, "定位权限被拒绝", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onCameraIdle() {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                updateOutline(style);
            }
        });
    }

    /**
     * Query the map for a building Feature in the map's building layer. The query happens in the middle of the
     * map ("the target"). If there's a building Feature in the middle of the map, its coordinates are turned
     * into a list of Point objects so that a LineString can be created.
     *
     * @return the LineString built via the building's coordinates
     */
    private LineString getBuildingFeatureOutline(@NonNull Style style) {
// Retrieve the middle of the map
        final PointF pixel = mapboxMap.getProjection().toScreenLocation(new LatLng(
                mapboxMap.getCameraPosition().target.getLatitude(),
                mapboxMap.getCameraPosition().target.getLongitude()
        ));

        List<Point> pointList = new ArrayList<>();

// Check whether the map style has a building layer
        if (style.getLayer("building") != null) {

// Retrieve the building Feature that is displayed in the middle of the map
            List<Feature> features = mapboxMap.queryRenderedFeatures(pixel, "building");
            if (features.size() > 0) {
                if (features.get(0).geometry() instanceof Polygon) {
                    Polygon buildingFeature = (Polygon) features.get(0).geometry();
// Build a list of Point objects from the building Feature's coordinates
                    if (buildingFeature != null) {
                        for (int i = 0; i < buildingFeature.coordinates().size(); i++) {
                            for (int j = 0;
                                 j < buildingFeature.coordinates().get(i).size(); j++) {
                                pointList.add(Point.fromLngLat(
                                        buildingFeature.coordinates().get(i).get(j).longitude(),
                                        buildingFeature.coordinates().get(i).get(j).latitude()
                                ));
                            }
                        }
                    }
                }
// Create a LineString from the list of Point objects
            }
        } else {
            Toast.makeText(this, "没有可用", Toast.LENGTH_SHORT).show();
        }
        return LineString.fromLngLats(pointList);
    }

    /**
     * Update the FeatureCollection used by the building outline LineLayer. Then refresh the map.
     */
    private void updateOutline(@NonNull Style style) {
// Update the data source used by the building outline LineLayer and refresh the map
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(new Feature[]
                {Feature.fromGeometry(getBuildingFeatureOutline(style))});
        GeoJsonSource source = style.getSourceAs("source");
        if (source != null) {
            source.setGeoJson(featureCollection);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        // Convert LatLng coordinates to screen pixel and only query the rendered features.
        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(pixel);

        // Get the first feature within the list if one exist
        if (features.size() > 0) {
            Feature feature = features.get(0);
            // Ensure the feature has properties defined
            if (feature.properties() != null) {
                for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                    // Log all the properties
                    Log.d("123", String.format("%s = %s", entry.getKey(), entry.getValue()));
                }
            }
        }

        return true;
    }

    private static class LocationChangeListeningActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MainActivity> activityWeakReference;

        LocationChangeListeningActivityLocationCallback(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MainActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

// Create a Toast which displays the new location's coordinates
                Toast.makeText(activity, String.format("新位置%s1,,%s2",
                        String.valueOf(result.getLastLocation().getLatitude()),
                        String.valueOf(result.getLastLocation().getLongitude())),
                        Toast.LENGTH_SHORT).show();

// Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {

                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());

                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
// Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}