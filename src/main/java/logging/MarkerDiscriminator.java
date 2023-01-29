package logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;
import org.slf4j.Marker;

public class MarkerDiscriminator extends AbstractDiscriminator<ILoggingEvent> {

    @Override
    public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
        final Marker marker = iLoggingEvent.getMarker();
        return marker == null ? "logs" : marker.toString();
    }

    @Override
    public String getKey() {
        return "filename";
    }

}