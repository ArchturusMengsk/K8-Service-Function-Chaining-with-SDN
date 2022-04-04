package apps.aletest.src.main.java.org.onosproject.aletest;

//ONOS imports
import org.onlab.packet.Ethernet;
import org.onosproject.core.ApplicationId;
import org.onosproject.net.*;
import org.onosproject.net.Device;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.device.PortStatistics;
import org.onosproject.net.flow.*;
import org.onosproject.net.flow.FlowEntry;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.FlowRuleProvider;
import org.onosproject.net.flow.FlowRuleOperations;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flow.instructions.Instructions;
import org.onosproject.net.host.HostService;
import org.onosproject.net.topology.TopologyEdge;
import org.onosproject.net.topology.TopologyService;
import org.onosproject.net.topology.TopologyVertex;
import org.onosproject.net.Port;
import org.onosproject.net.PortNumber;
import org.slf4j.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class installRulesTask {
    //class Task extends TimerTask {
        public void run() {
            while (!isExit()) {
                setRules();
                setExit(true);
            }
            //deleteRules();
        }
    //}

    /////////////////////////////////////////////////////SET RULES FUNCTION/////////////////////////////////////////////////////
    public void setRules() {
        //Get devices and store them inside 'devices'
        //Iterable<Device> devices = getDeviceService().getDevices();
        //int i = 0;
        /*
        //for every device
        for (Device device : devices) {
            i += 1;
            //Show current device
            log.info("###############################################################");
            log.info("Device Name(ID): " + device.id().toString());
            //Get ports for this device
            List<Port> ports = deviceService.getPorts(device.id());
            log.info("Port 1: " + ports.get(0).toString());
            log.info("Port 2: " + ports.get(1).toString());
            //add four flow rules
            buildFlowRule(device, ports.get(0), ports.get(1), "arp", 60010);
            buildFlowRule(device, ports.get(1), ports.get(0), "arp", 60020);
            buildFlowRule(device, ports.get(0), ports.get(1), "ipv4", 60030);
            buildFlowRule(device, ports.get(1), ports.get(0), "ipv4", 60040);
        }
        */
        Host sender = null;
        Host receiver = null;
        //Host forwarder1 = null;
        //Host forwarder2 = null;
        //iterate all hosts
        for (Host container : getHostService().getHosts()) {
            log.info(container.toString());
            if (container.ipAddresses().toString().equals("[10.10.10.2]")) {
                sender = container;
            }
            //else if (container.ipAddresses().toString().equals("[10.10.20.3]")) {
            //    receiver = container;
            //}

            else if (container.ipAddresses().toString().equals("[10.10.10.4]")) {
                //forwarder1 = container;
                receiver = container;
            }

            //else if (container.ipAddresses().toString().equals("[10.10.20.5]")) {
            //    forwarder2 = container;
            //}
        }
        //buildFlowRule(sender.location().port(), forwarder1.location().port(), forwarder1.location().deviceId(), "arp", 60010);
        //buildFlowRule(receiver.location().port(), forwarder2.location().port(), forwarder2.location().deviceId(), "arp", 60020);
        //buildFlowRule(sender.location().port(), forwarder1.location().port(), forwarder1.location().deviceId(), "ipv4", 60030);
        //buildFlowRule(receiver.location().port(), forwarder2.location().port(), forwarder2.location().deviceId(), "ipv4", 60040);
        buildFlowRule(sender.location().port(), receiver.location().port(), receiver.location().deviceId(), "arp", 60010);
        buildFlowRule(sender.location().port(), receiver.location().port(), receiver.location().deviceId(), "ipv4", 60020);
        buildFlowRule(receiver.location().port(), sender.location().port(), sender.location().deviceId(), "arp", 60030);
        buildFlowRule(receiver.location().port(), sender.location().port(), sender.location().deviceId(), "ipv4", 60040);

    }

    ////////////////////////////////////////////////////BUILD RULES FUNCTION////////////////////////////////////////////////////
    //TODO: Selector Sending (Add EthSrc) Treatment Sending (Add ModL2Dst?, ModL3Dst?) Flow Rule Sending (Add For Device)
    public void buildFlowRule(PortNumber in_port, PortNumber out_port, DeviceId toDeviceId, String TrafficType, int Priority) {
        if (TrafficType.toLowerCase() == "arp") {
            log.info("Installing ARP redirection rule");
            TrafficSelector selectorSending = DefaultTrafficSelector.builder()
                    .matchEthType(Ethernet.TYPE_ARP)
                    .matchInPort(in_port)
                    .build();
            TrafficTreatment treatmentSending = DefaultTrafficTreatment.builder()
                    .setOutput(out_port)
                    .build();
            FlowRule flowRuleSending = DefaultFlowRule.builder()
                    .forDevice(toDeviceId)
                    .fromApp(getAppId())
                    .withSelector(selectorSending)
                    .withTreatment(treatmentSending)
                    .withPriority(Priority)
                    .makePermanent()
                    .build();
            FlowRuleOperations flowRuleOperationsSending = FlowRuleOperations.builder()
                    .add(flowRuleSending)
                    .build();
            flowRuleService.apply(flowRuleOperationsSending);
            log.info("New rule added to device(ID): " + toDeviceId.toString());
            //log.info("Traffic of type " + TrafficType + " entering port " + in_port.toString() + " will be redirected to port " + out_port.toString());
        }

        else if (TrafficType.toLowerCase() == "ipv4") {
            log.info("Installing IPv4 redirection rule");
            TrafficSelector selectorSending = DefaultTrafficSelector.builder()
                    .matchEthType(Ethernet.TYPE_IPV4)
                    .matchInPort(in_port)
                    .build();
            TrafficTreatment treatmentSending = DefaultTrafficTreatment.builder()
                    .setOutput(out_port)
                    .build();
            FlowRule flowRuleSending = DefaultFlowRule.builder()
                    .forDevice(toDeviceId)
                    .fromApp(getAppId())
                    .withSelector(selectorSending)
                    .withTreatment(treatmentSending)
                    .withPriority(Priority)
                    .makePermanent()
                    .build();
            FlowRuleOperations flowRuleOperationsSending = FlowRuleOperations.builder()
                    .add(flowRuleSending)
                    .build();
            flowRuleService.apply(flowRuleOperationsSending);
            log.info("New successfully rule added to device(ID): " + toDeviceId.toString());
            //log.info("Traffic of type " + TrafficType + " entering port " + in_port.toString() + " will be redirected to port " + out_port.toString());
        }

        else {
            log.info("Traffic type " + TrafficType + " not implemented yet");
        }
    }

    public void deleteRules() {
        //delete all flow rules from this app
        log.info("Deleting rules from Ale Test application");
        flowRuleService.removeFlowRulesById(getAppId());
        /*
        //Get devices and store them inside 'devices'
        Iterable<Device> devices = getDeviceService().getDevices();
        int i = 0;
        //for every device
        for (Device device : devices) {
            i += 1;
            log.info("Device(ID) " + device.id().toString() + " flow rules being deleted...");

        }
         */
    }

    /////////////////////////////////////////////////////GETTERS & SETTERS/////////////////////////////////////////////////////

    //PortStats getters & setters
    public PortStatistics getPortStats() {
        return portStats;
    }

    public void setPortStats(PortStatistics portStats) {
        this.portStats = portStats;
    }

    private PortStatistics portStats;

    //Port getters & setters
    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    private Long port;

    //DeviceService getters & setters
    public DeviceService getDeviceService() {
        return deviceService;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    protected DeviceService deviceService;

    //Device getters & setters
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    private Device device;

    //Logger getters & setters
    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    private Logger log;

    //PortNumber getters & setters
    private PortNumber portNumber;

    public PortNumber getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(PortNumber portNumber) {
        this.portNumber = portNumber;
    }

    //flowEntries getters & setters
    private Iterable<FlowEntry> flowEntries;

    public Iterable<FlowEntry> getFlowEntries() {
        return flowEntries;
    }

    public void setFlowEntries(Iterable<FlowEntry> flowEntries) {
        this.flowEntries = flowEntries;
    }

    //flowRuleService getters & setters
    private FlowRuleService flowRuleService;

    public FlowRuleService getFlowRuleService() {
        return flowRuleService;
    }

    public void setFlowRuleService(FlowRuleService flowRuleService) {
        this.flowRuleService = flowRuleService;
    }

    //ApplicationId getters & setters
    private ApplicationId appId;

    public ApplicationId getAppId() {
        return appId;
    }

    public void setAppId(ApplicationId appId) {
        this.appId = appId;
    }

    //Timer getters & setters
    /*
    public void schedule() {
        this.getTimer().schedule(new Task(), 0, 1000);
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    private Timer timer = new Timer();
    */
    //Exit status getters & setters
    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    private boolean exit;

    //Delay getters & setters
    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    private long delay;

    //HostService getters & setters
    public HostService getHostService() {
        return hostService;
    }

    public void setHostService(HostService hostService) {
        this.hostService = hostService;
    }

    private HostService hostService;
}
