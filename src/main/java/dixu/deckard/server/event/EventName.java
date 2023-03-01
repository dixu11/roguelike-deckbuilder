package dixu.deckard.server.event;

import java.util.Arrays;

public interface EventName {
    String getName();

   default  <T> T getObject(){
       String stringName = getName();
       CoreEventName coreEventName = Arrays.stream(CoreEventName.values())
               .filter(e -> e.name().equals(stringName))
               .findAny()
               .orElse(null);

       if (coreEventName != null) {
           return (T) coreEventName;
       }

       ActionEventName actionEventName = Arrays.stream(ActionEventName.values())
               .filter(e -> e.name().equals(stringName))
               .findAny()
               .orElse(null);
       if (actionEventName != null) {
           return (T) actionEventName;
       } else {
           throw new IllegalStateException("CAN'T FOUND ENUM OF GIVEN NAME");
       }
   }
}
