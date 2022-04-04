while true; do
    read -p "Clean up? (y/n) " answer

    if [[ $answer =~ ^[Yy]$ ]] ;
    then
        echo "  [*]Deleting flows"
        sudo ovs-ofctl del-flows ovs-br0
        sudo ovs-ofctl del-flows ovs-br1

        echo "  [*]Removing switches and VETH links"
        sudo ovs-vsctl --if-exists del-port ovs-br0 ovs-br0_ovs-br1
        sudo ovs-vsctl --if-exists del-port ovs-br1 ovs-br1_ovs-br0
        sudo ip link delete ovs-br0_ovs-br1
        sudo ovs-vsctl --if-exists del-br ovs-br0
        sudo ovs-vsctl --if-exists del-br ovs-br1

        echo "  [*]Stopping containers"
        sudo docker stop myContainer1
        sudo docker stop myContainer2
        sudo docker stop myContainer3
        sudo docker rm myContainer1
        sudo docker rm myContainer2
        sudo docker rm myContainer3
    fi

    break
done

echo ""
while true; do
    read -p "Deploy scenario A? (y/n) " answer

    if [[ $answer =~ ^[Yy]$ ]] ;
    then
        echo "  [*]Deploying switches"
        sudo ovs-vsctl add-br ovs-br0
        sudo ifconfig ovs-br0 173.16.1.1 netmask 255.255.255.0 up

        echo "  [*]Running sender and receiver containers"
        sudo docker run -di --name myContainer1 --net=none alpine
        sudo docker run -di --name myContainer2 --net=none alpine
        sudo docker start myContainer1
        sudo docker start myContainer2

        echo "  [*]Creating container eth ports"
        sudo ovs-docker add-port ovs-br0 eth1 myContainer1 --ipaddress=173.16.1.2/24
        sudo ovs-docker add-port ovs-br0 eth1 myContainer2 --ipaddress=173.16.1.3/24

        echo "  [*]Setting SDN controllers"
        sudo ovs-vsctl set-controller ovs-br0 tcp:173.16.1.1:6653
        sudo ovs-vsctl set bridge ovs-br0 protocol=OpenFlow10
    fi

    break
done

echo ""
while true; do
    read -p "Deploy scenario B? (y/n) " answer

    if [[ $answer =~ ^[Yy]$ ]] ;
    then
        echo "  [*]Deploying switches"
        sudo ovs-vsctl add-br ovs-br0
        sudo ovs-vsctl add-br ovs-br1
        sudo ifconfig ovs-br0 173.16.1.1 netmask 255.255.255.0 up
        sudo ifconfig ovs-br1 173.16.1.2 netmask 255.255.255.0 up

        echo "  [*]Running sender and receiver containers"
        sudo docker run -di --name myContainer1 --net=none alpine
        sudo docker run -di --name myContainer2 --net=none alpine
        sudo docker run -di --name myContainer3 --net=none alpine
        sudo docker start myContainer1
        sudo docker start myContainer2
        sudo docker start myContainer3

        echo "  [*]Creating container eth ports"
        sudo ovs-docker add-port ovs-br0 eth1 myContainer1 --ipaddress=173.16.1.3/24
        sudo ovs-docker add-port ovs-br0 eth1 myContainer2 --ipaddress=173.16.1.4/24
        sudo ovs-docker add-port ovs-br1 eth2 myContainer2 --ipaddress=173.16.1.5/24
        sudo ovs-docker add-port ovs-br1 eth1 myContainer3 --ipaddress=173.16.1.6/24

        echo "  [*]Setting SDN controllers"
        sudo ovs-vsctl set-controller ovs-br0 tcp:173.16.1.1:6653
        sudo ovs-vsctl set-controller ovs-br1 tcp:173.16.1.2:6653
        sudo ovs-vsctl set bridge ovs-br0 protocol=OpenFlow10
        sudo ovs-vsctl set bridge ovs-br1 protocol=OpenFlow10

        echo "  [*]Setting up VETH links"
        sudo ip link add ovs-br0_ovs-br1 type veth peer name ovs-br1_ovs-br0
        sudo ifconfig ovs-br0_ovs-br1 up
        sudo ifconfig ovs-br1_ovs-br0 up
        sudo ovs-vsctl add-port ovs-br0 ovs-br0_ovs-br1
        sudo ovs-vsctl add-port ovs-br1 ovs-br1_ovs-br0
    fi

    break
done

echo ""
while true; do
    read -p "Deploy scenario C? (y/n) " answer

    if [[ $answer =~ ^[Yy]$ ]] ;
    then
        echo "  [*]Deploying switches"
        sudo ovs-vsctl add-br ovs-br0
        sudo ovs-vsctl add-br ovs-br1
        sudo ifconfig ovs-br0 10.10.10.1 netmask 255.255.255.0 up
        sudo ifconfig ovs-br1 10.10.20.1 netmask 255.255.255.0 up

        echo "  [*]Running sender and receiver containers"
        sudo docker run -di --name myContainer1 --net=none sender:v0.1
        sudo docker run -di --name myContainer2 --net=none forwarder:v0.3
        sudo docker run -di --name myContainer3 --net=none sender:v0.1
        sudo docker start myContainer1
        sudo docker start myContainer2
        sudo docker start myContainer3

        echo "  [*]Creating container eth ports"
        sudo ovs-docker add-port ovs-br0 eth1 myContainer1 --ipaddress=10.10.10.2/24
        sudo ovs-docker add-port ovs-br0 eth1 myContainer2 --ipaddress=10.10.10.4/24
        sudo ovs-docker add-port ovs-br1 eth2 myContainer2 --ipaddress=10.10.20.5/24
        sudo ovs-docker add-port ovs-br1 eth1 myContainer3 --ipaddress=10.10.20.3/24

        #echo "  [*]Setting up sender"
        #sudo docker exec myContainer1 route add default dev eth1

        echo "  [*]Setting up forwarder"
        #sudo docker exec -di --privileged myContainer2 python3 forward_from_eth1_to_eth2.py
        #sudo docker exec -di --privileged myContainer2 python3 forward_from_eth2_to_eth1.py
        sudo docker exec -di --privileged myContainer2 python3 forwarder.py
        sudo docker exec -di --privileged myContainer1 route add -host 10.10.20.3 dev eth1
        sudo docker exec -di --privileged myContainer3 route add -host 10.10.10.2 dev eth1
        #sudo docker exec myContainer2 sysctl -w net.ipv4.ip_forward=1
        #sudo docker exec myContainer2 sysctl -p
        #sudo docker exec myContainer2 iptables -N FORWARD
        #sudo docker exec myContainer2 iptables -N POSTROUTING
        #sudo docker exec myContainer2 echo 1 > /proc/sys/net/ipv4/ip_forward
        #sudo docker exec myContainer2 iptables -t nat -A POSTROUTING -o dst_if -j MASQUERADE
        #sudo docker exec myContainer2 iptables -I FORWARD -i src_if -o dst_if -j ACCEPT
        #sudo docker exec myContainer2 iptables -A FORWARDER -i eth1 -j ACCEPT
        #sudo docker exec myContainer2 iptables -t filter -A FORWARDER -o eth1 -j MASQUERADE
        #sudo docker exec myContainer2 iptables -t filter -A FORWARDER -o eth2 -j MASQUERADE
        #sudo docker exec myContainer2 route add -host 173.16.1.3 dev eth1
        #sudo docker exec myContainer2 route add -host 173.16.1.6 dev eth2

        echo "  [*]Setting SDN controllers"
        sudo ovs-vsctl set-controller ovs-br0 tcp:10.10.10.1:6653
        sudo ovs-vsctl set-controller ovs-br1 tcp:10.10.20.1:6653
        sudo ovs-vsctl set bridge ovs-br0 protocol=OpenFlow10
        sudo ovs-vsctl set bridge ovs-br1 protocol=OpenFlow10

        echo "  [*]Setting up VETH links"
        sudo ip link add ovs-br0_ovs-br1 type veth peer name ovs-br1_ovs-br0
        sudo ifconfig ovs-br0_ovs-br1 up
        sudo ifconfig ovs-br1_ovs-br0 up
        sudo ovs-vsctl add-port ovs-br0 ovs-br0_ovs-br1
        sudo ovs-vsctl add-port ovs-br1 ovs-br1_ovs-br0

        echo "  [*]Deleting flows"
        sudo ovs-ofctl del-flows ovs-br0
        sudo ovs-ofctl del-flows ovs-br1

        echo "  [*]Add flow rules"
        sudo ovs-ofctl add-flow ovs-br0 arp,priority=40000,in_port=1,actions=output:2
        sudo ovs-ofctl add-flow ovs-br0 arp,priority=40000,in_port=2,actions=output:1
        sudo ovs-ofctl add-flow ovs-br0 icmp,priority=40000,in_port=1,actions=output:2
        sudo ovs-ofctl add-flow ovs-br0 icmp,priority=40000,in_port=2,actions=output:1
        sudo ovs-ofctl add-flow ovs-br1 arp,priority=40000,in_port=1,actions=output:2
        sudo ovs-ofctl add-flow ovs-br1 arp,priority=40000,in_port=2,actions=output:1
        sudo ovs-ofctl add-flow ovs-br1 icmp,priority=40000,in_port=1,actions=output:2
        sudo ovs-ofctl add-flow ovs-br1 icmp,priority=40000,in_port=2,actions=output:1
    fi

    break
done

echo ""
while true; do
    read -p "Deploy scenario D? (y/n) " answer

    if [[ $answer =~ ^[Yy]$ ]] ;
    then
        echo "  [*]Deploying switches"
        sudo ovs-vsctl add-br ovs-br0
        sudo ifconfig ovs-br0 173.16.1.1 netmask 255.255.255.0 up

        echo "  [*]Running sender and receiver containers"
        sudo docker run -di --name myContainer1 --net=none alpine
        sudo docker run -di --name myContainer2 --net=none alpine
        sudo docker start myContainer1
        sudo docker start myContainer2

        echo "  [*]Creating container eth ports"
        sudo ovs-docker add-port ovs-br0 eth1 myContainer1 --ipaddress=10.10.10.2/24
        sudo ovs-docker add-port ovs-br0 eth1 myContainer2 --ipaddress=10.10.10.4/24

        echo "  [*]Setting SDN controllers"
        sudo ovs-vsctl set-controller ovs-br0 tcp:173.16.1.1:6653
        sudo ovs-vsctl set bridge ovs-br0 protocol=OpenFlow10
    fi

    break
done