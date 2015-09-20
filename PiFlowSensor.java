import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


public class PiFlowSensor {
 public static void main(String args[]) throws InterruptedException {
		
		System.out.println("<PiFlowSensor> GPIO PIN 2 Listening  ... started.");
        
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled, 
		// this PIN numbering is specific for pi4j and not Raspi pin numbering
		// GPIO_02 points to chip #PIN 13 in Raspi 2
        final GpioPinDigitalInput pinTwo = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
		

        // create and register gpio pin listener
        pinTwo.addListener(new GpioPinListenerDigital() {
			
			long startTime = System.currentTimeMillis();
			long curTime = startTime;
			int count = 0;
			double totalMl=0;
			double lastSecMl=0;
			PinState state = PinState.HIGH; 
				
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				
				curTime = System.currentTimeMillis();
				long duration = curTime -startTime ;
				state = event.getState();
				
								
				// count RISING pulses in a second
				if (duration < 1000 && (state == PinState.HIGH )) {
					curTime = System.currentTimeMillis();
					count++;
				} else if  (duration > 1000 && (event.getState() == PinState.HIGH )) {
					lastSecMl = 2.25*count;  //2.25 mL per cycle as per product documentation of flow sensor YF-S201
					totalMl = totalMl + lastSecMl;
					System.out.println(curTime + "  Consumed water in last second = " + lastSecMl + " ml  and Total Litres  = " + totalMl/1000 + " litres  SecondCount="+ count);
					
					startTime = System.currentTimeMillis();
					count=0;
				} else{
					
				}
			
                // display pin state on console
                
				
				
            }
            
        });
        
        System.out.println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.");
        
        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(500);
        }
        
        
    }
}
