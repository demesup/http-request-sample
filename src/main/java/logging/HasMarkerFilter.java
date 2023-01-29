package logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class HasMarkerFilter extends Filter<ILoggingEvent> {

  @Override
  public FilterReply decide(ILoggingEvent event) {    
    if (event.getMarker() != null) {
      return FilterReply.ACCEPT;
    } else {
      return FilterReply.DENY;
    }
  }
}