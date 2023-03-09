package dixu.deckard.server.event;

import java.util.Arrays;

public interface EventType {
    String getType();

   default  <T> T getObject(){
       String stringType = getType();
       CoreEventType coreEventType = Arrays.stream(CoreEventType.values())
               .filter(e -> e.name().equals(stringType))
               .findAny()
               .orElse(null);

       if (coreEventType != null) {
           return (T) coreEventType;
       }

       ActionEventType actionEventType = Arrays.stream(ActionEventType.values())
               .filter(e -> e.name().equals(stringType))
               .findAny()
               .orElse(null);
       if (actionEventType != null) {
           return (T) actionEventType;
       } else {
           throw new IllegalStateException("CAN'T FOUND ENUM OF GIVEN NAME");
       }
   }
}
