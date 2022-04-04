package apps.aletest.src.main.java.org.onosproject.aletest;

//ONOS imports
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.host.HostService;
import org.onosproject.net.topology.TopologyService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Java imports
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

@Component(immediate = true)
public class aleTrafficRedirect {
    //Console logger
    private final Logger log = LoggerFactory.getLogger(getClass());

    //Different required ONOS services
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected HostService hostService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected DeviceService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected FlowRuleService flowRuleService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected TopologyService topologyService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected CoreService coreService;

    private installRulesTask task = new installRulesTask();

    //When activating aletest library
    @Activate
    protected void activate() {

        log.info("Started");

        try {
            //Timer timer = new Timer();
            task.setDelay(3);
            task.setExit(false);
            task.setLog(log);
            //task.setTimer(timer);
            task.setDeviceService(deviceService);
            task.setFlowRuleService(flowRuleService);
            task.setHostService(hostService);
            task.setDeviceService(deviceService);
            task.setAppId(coreService.registerApplication("org.onosproject.aletest"));
            task.run();
            //task.schedule();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //When deactivating aletest library
    @Deactivate
    protected void deactivate() {
        try {
            task.setExit(true);
            //task.getTimer().cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Stopped");
    }

    //Hash Map
    public Map<String, aleTrafficRedirect> getMap() {
        return map;
    }

    public void setMap(Map<String, aleTrafficRedirect> map) {
        this.map = map;
    }

    private Map<String, aleTrafficRedirect> map = new HashMap<String, aleTrafficRedirect>();
}
