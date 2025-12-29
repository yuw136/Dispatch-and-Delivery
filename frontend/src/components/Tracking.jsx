import { useEffect, useState, useRef } from "react";
import { APIProvider, Map, useMap } from "@vis.gl/react-google-maps";
import axios from "axios";
import { BASE_URL, TOKEN_KEY } from "../constants";

export default function Tracking(props) {
  const [order] = props.order;
  const [route, setRoute] = useState("");
  const [position, setPosition] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const id = order.id;
  const abortControllerRef = useRef(null);

  function DrawPosition({ route, position }) {
    const map = useMap();
    const polylineRef = useRef(null);
    const markerRef = useRef(null);
    const hasZoomedRef = useRef(false);

    useEffect(() => {
      if (!map || !route) return;
      const decodedRoute = google.maps.geometry.encoding.decodePath(route);

      // Remove old polyline if exists
      if (polylineRef.current) {
        polylineRef.current.setMap(null);
      }

      polylineRef.current = new google.maps.Polyline({
        path: decodedRoute,
        strokeColor: "#2563eb", //blue
        strokeOpacity: 0.8,
        strokeWeight: 4,
        map: map,
      });

      //auto zoom to fit the route
      if (!hasZoomedRef.current) {
        const bounds = new google.maps.LatLngBounds();
        decodedRoute.forEach((point) => bounds.extend(point));
        map.fitBounds(bounds);
        hasZoomedRef.current = true;
      }

      return () => {
        if (polylineRef.current) {
          polylineRef.current.setMap(null);
        }
      };
    }, [map, route]);

    useEffect(() => {
      if (!map || !position) return;
      if (!markerRef.current) {
        markerRef.current = new google.maps.Marker({
          position: { lat: position.lat, lng: position.lng },
          map: map,
          title: "current position",
          //   icon: "drone/robot",
        });
      } else {
        markerRef.current.setPosition({ lat: position.lat, lng: position.lng });
      }

      return () => {
        if (markerRef.current) {
          markerRef.current.setMap(null);
          markerRef.current = null;
        }
      };
    }, [map, position]);

    return null;
  }

  const fetchTrackingData = async (isInitialFetch = false) => {
    //cancel previous request(if no response for > 10s)
    if (!isInitialFetch && abortControllerRef.current) {
      abortControllerRef.current.abort();
    }

    abortControllerRef.current = new AbortController();

    try {
      if (isInitialFetch) {
        setIsLoading(true);
      }

      const res = await axios.get(
        `${BASE_URL}/dashboard/orders/tracking?order_id=${id}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem(TOKEN_KEY)}`,
            Accept: "application/json",
          },
          signal: abortControllerRef.current.signal,
          timeout: isInitialFetch ? 30000 : 10000,
        }
      );
      console.log("check response:", res.data);

      setRoute(res.data.route);
      setPosition(res.data.position);
    } catch (err) {
      if (axios.isCancel(err)) {
        console.log("position update request aborted");
      } else {
        console.error(err);
      }
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    let interval;

    const initializeTracking = async () => {
      await fetchTrackingData(true); // Wait for initial fetch to complete/timeout
      interval = setInterval(fetchTrackingData, 10000); // Then start polling
    };

    initializeTracking();

    return () => {
      if (interval) {
        clearInterval(interval);
      }
    };
  }, [id]);

  if (isLoading) {
    return <div>Loading tracking data...</div>;
  }

  //after loading show google map
  return (
    <APIProvider apiKey={import.meta.env.VITE_GOOGLE_MAP_API_KEY}>
      <Map defaultCenter={{ lat: 37.7749, lng: 122.4194 }} defaultZoom={12}>
        <DrawPosition route={route} position={position}></DrawPosition>
      </Map>
    </APIProvider>
  );
}
